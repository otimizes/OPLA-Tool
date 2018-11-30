package jmetal4.experiments;

import arquitetura.io.ReaderConfig;
import br.ufpr.dinf.gres.loglog.Level;
import database.Database;
import database.Result;
import exceptions.MissingConfigurationException;
import jmetal4.core.Algorithm;
import jmetal4.core.SolutionSet;
import jmetal4.metaheuristics.nsgaII.NSGAII;
import jmetal4.operators.crossover.Crossover;
import jmetal4.operators.crossover.CrossoverFactory;
import jmetal4.operators.mutation.Mutation;
import jmetal4.operators.mutation.MutationFactory;
import jmetal4.operators.selection.Selection;
import jmetal4.operators.selection.SelectionFactory;
import jmetal4.problems.OPLA;
import jmetal4.util.JMException;
import learning.Clustering;
import learning.ClusteringAlgorithm;
import learning.Moment;
import metrics.AllMetrics;
import org.apache.log4j.Logger;
import persistence.*;
import results.Execution;
import results.Experiment;
import results.FunResults;
import results.InfoResult;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class NSGAII_OPLA_FeatMut {

    private static final Logger LOGGER = Logger.getLogger(NSGAII_OPLA_FeatMut.class);

    public static int populationSize;
    public static int maxEvaluations;
    public static double mutationProbability;
    public static double crossoverProbability;
    private static Connection connection;
    private static AllMetricsPersistenceDependency allMetricsPersistenceDependencies;
    private static MetricsPersistence mp;
    private static Result result;
    private NSGAIIConfig configs;
    private String experiementId;
    private int numberObjectives;
    private ClusteringAlgorithm clusteringAlgorithm;

    public NSGAII_OPLA_FeatMut(NSGAIIConfig config) {
        this.configs = config;
    }

    public NSGAII_OPLA_FeatMut() {
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(File.separator) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void setConfigs(NSGAIIConfig configs) {
        this.configs = configs;
    }

    public void execute() throws Exception {

        intializeDependencies();

        int runsNumber = this.configs.getNumberOfRuns();
        populationSize = this.configs.getPopulationSize();
        maxEvaluations = this.configs.getMaxEvaluation();
        crossoverProbability = this.configs.getCrossoverProbability();
        mutationProbability = this.configs.getMutationProbability();
        this.numberObjectives = this.configs.getOplaConfigs().getNumberOfObjectives();
        this.clusteringAlgorithm = this.configs.getClusteringAlgorithm();

        String context = "OPLA";

        String plas[] = this.configs.getPlas().split(",");
        String xmiFilePath;

        for (String pla : plas) {
            LOGGER.info("Criando uma PLA em: " + pla);
            xmiFilePath = pla;
            OPLA problem = null;
            String plaName = getPlaName(pla);
            LOGGER.info("Nome da PLA: " + plaName);

            try {
                problem = new OPLA(xmiFilePath, this.configs);
            } catch (Exception e) {
                LOGGER.error(e);
                e.printStackTrace();
                this.configs.getLogger()
                        .putLog(String.format("Error when try read architecture %s. %s", xmiFilePath, e.getMessage()));
                throw new JMException("Ocorreu um erro durante geração de PLAs");
            }

            Experiment experiement = mp.createExperimentOnDb(plaName, "NSGAII", configs.getDescription());
            ExperimentConfs conf = new ExperimentConfs(experiement.getId(), "NSGAII", configs);
            conf.save();
            LOGGER.info("Salvou configurações do experimento");

            Algorithm algorithm;
            SolutionSet todasRuns = new SolutionSet();

            Crossover crossover;
            Mutation mutation;
            Selection selection;

            HashMap<String, Object> parameters;

            algorithm = new NSGAII(problem);

            // Algorithm parameters
            algorithm.setInputParameter("populationSize", populationSize);
            algorithm.setInputParameter("maxEvaluations", maxEvaluations);

            // Mutation and Crossover
            parameters = new HashMap<String, Object>();
            parameters.put("probability", crossoverProbability);
            parameters.put("numberOfObjectives", numberObjectives);
            crossover = CrossoverFactory.getCrossoverOperator("PLACrossover", parameters, configs);

            parameters = new HashMap<String, Object>();
            parameters.put("probability", mutationProbability);
            mutation = MutationFactory.getMutationOperator("PLAFeatureMutation", parameters, this.configs);

            // Selection Operator
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            // Add the operators to the algorithm
            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            if (this.configs.isLog())
                logInforamtions(context, pla);

            List<String> selectedObjectiveFunctions = this.configs.getOplaConfigs().getSelectedObjectiveFunctions();
            mp.saveObjectivesNames(selectedObjectiveFunctions, experiement.getId());
            LOGGER.info("Salvou funções objetivo selecionadas");


            result.setPlaName(plaName);

            long time[] = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {

                // Cria uma execução. Cada execução está ligada a um
                // experiemento.
                Execution execution = new Execution(experiement);
                execution.setRuns(runs);
                setDirToSaveOutput(experiement.getId(), execution.getId());

                // Execute the Algorithm
                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominadas(resultFront);
                resultFront = problem.removeRepetidas(resultFront);

                List<FunResults> funResults = result.getObjectives(resultFront.getSolutionSet(), execution,
                        experiement);
                List<InfoResult> infoResults = result.getInformations(resultFront.getSolutionSet(), execution,
                        experiement);
                AllMetrics allMetrics = result.getMetrics(funResults, resultFront.getSolutionSet(), execution,
                        experiement, selectedObjectiveFunctions);
                execution.setTime(estimatedTime);

                // Clustering OBS: Needs to be a priori for filter the PLAs to save
                if (Moment.INTERACTIVE.equals(this.configs.getClusteringMoment()) || Moment.BOTH.equals(this.configs.getClusteringMoment())) {
                    Clustering clustering = new Clustering(resultFront, this.clusteringAlgorithm);
                    resultFront = clustering.run();
                    for (int id : clustering.getIdsFilteredSolutions()) {
                        funResults.remove(id);
                        infoResults.remove(id);
                        allMetrics.remove(id);
                    }
                }
                // Clustering

                resultFront.saveVariablesToFile("VAR_" + runs + "_", funResults, this.configs.getLogger(), true);

                execution.setFuns(funResults);
                execution.setInfos(infoResults);
                execution.setAllMetrics(allMetrics);

                // Interactive OBS: Needs to be a posteriori for visualization of the PLAs on PAPYRUS
                if (this.configs.getInteractive() && runs < this.configs.getMaxInteractions())
                    this.configs.getInteractiveFunction().run(resultFront, execution);
                // Interactive

                ExecutionPersistence persistence = new ExecutionPersistence(allMetricsPersistenceDependencies);
                try {
                    persistence.persist(execution);
                    persistence = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // armazena as solucoes de todas runs
                todasRuns = todasRuns.union(resultFront);

                // Util.copyFolder(experiement.getId(), execution.getId());
                // Util.moveAllFilesToExecutionDirectory(experiementId,
                // execution.getId());

                saveHypervolume(experiement.getId(), execution.getId(), resultFront, plaName);
            }

            todasRuns = problem.removeDominadas(todasRuns);
            todasRuns = problem.removeRepetidas(todasRuns);

            this.configs.getLogger().putLog("------ All Runs - Non-dominated solutions --------", Level.INFO);
            List<FunResults> funResults = result.getObjectives(todasRuns.getSolutionSet(), null, experiement);

            // Clustering OBS: Needs to be a priori for filter the PLAs to save
            if (Moment.POSTERIORI.equals(this.configs.getClusteringMoment()) || Moment.BOTH.equals(this.configs.getClusteringMoment())) {
                Clustering clustering = new Clustering(todasRuns, this.clusteringAlgorithm);
                todasRuns = clustering.run();
                for (int id : clustering.getIdsFilteredSolutions()) {
                    funResults.remove(id);
                }
            }
            // Clustering

            LOGGER.info("saveVariablesToFile()");
            todasRuns.saveVariablesToFile("VAR_All_", funResults, this.configs.getLogger(), true);

            mp.saveFunAll(funResults);

            List<InfoResult> infoResults = result.getInformations(todasRuns.getSolutionSet(), null, experiement);
            mp.saveInfoAll(infoResults);
            LOGGER.info("saveInfoAll()");

            AllMetrics allMetrics = result.getMetrics(funResults, todasRuns.getSolutionSet(), null, experiement,
                    selectedObjectiveFunctions);
            mp.persisteMetrics(allMetrics, this.configs.getOplaConfigs().getSelectedObjectiveFunctions());
            LOGGER.info("getMetrics()");
            mp = null;

            setDirToSaveOutput(experiement.getId(), null);

            LOGGER.info("DistanceEuclideanPersistence.calculate()");
            CalculaEd c = new CalculaEd();
            DistanceEuclideanPersistence.save(c.calcula(this.experiementId, this.numberObjectives), this.experiementId);
            infoResults = null;
            funResults = null;

            // Util.moveAllFilesToExecutionDirectory(experiementId, null);
            LOGGER.info("saveHypervolume()");
            saveHypervolume(experiement.getId(), null, todasRuns, plaName);
        }

        // Util.moveResourceToExperimentFolder(this.experiementId);

    }

    private void logInforamtions(String context, String pla) {
        logarPainel(context, pla);
        logarConsole(context, pla);

    }

    private void logarPainel(String context, String pla) {
        configs.getLogger().putLog("\n================ NSGAII ================", Level.INFO);
        configs.getLogger().putLog("Context: " + context, Level.INFO);
        configs.getLogger().putLog("PLA: " + pla, Level.INFO);
        configs.getLogger().putLog("Params:", Level.INFO);
        configs.getLogger().putLog("\tPop -> " + populationSize, Level.INFO);
        configs.getLogger().putLog("\tMaxEva -> " + maxEvaluations, Level.INFO);
        configs.getLogger().putLog("\tCross -> " + crossoverProbability, Level.INFO);
        configs.getLogger().putLog("\tMuta -> " + mutationProbability, Level.INFO);

        long heapSize = Runtime.getRuntime().totalMemory();
        heapSize = (heapSize / 1024) / 1024;
        configs.getLogger().putLog("Heap Size: " + heapSize + "Mb\n");
    }

    private void logarConsole(String context, String pla) {
        LOGGER.info("================ NSGAII ================");
        LOGGER.info("Context: " + context);
        LOGGER.info("PLA: " + pla);
        LOGGER.info("Params:");
        LOGGER.info("tPop -> " + populationSize);
        LOGGER.info("tMaxEva -> " + maxEvaluations);
        LOGGER.info("tCross -> " + crossoverProbability);
        LOGGER.info("tMuta -> " + mutationProbability);
        LOGGER.info("================ NSGAII ================");
    }

    private void intializeDependencies() {
        LOGGER.info("Inicializando dependências");
        result = new Result();
        Database.setPathToDB(this.configs.getPathToDb());

        try {
            connection = Database.getConnection();
            allMetricsPersistenceDependencies = new AllMetricsPersistenceDependency(connection);
            mp = new MetricsPersistence(allMetricsPersistenceDependencies);
        } catch (ClassNotFoundException | MissingConfigurationException | SQLException e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new RuntimeException();
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    private void setDirToSaveOutput(String experimentID, String executionID) {
        this.experiementId = experimentID;
        CommonOPLAFeatMut.setDirToSaveOutput(experimentID, executionID);
    }

    private void saveHypervolume(String experimentID, String executionID, SolutionSet allSolutions, String plaName) {
        String dir;
        if (executionID != null)
            dir = ReaderConfig.getDirExportTarget() + System.getProperty("file.separator") + experimentID + System.getProperty("file.separator") + executionID + "/Hypervolume/";
        else
            dir = ReaderConfig.getDirExportTarget() + System.getProperty("file.separator") + experimentID + "/Hypervolume/";

        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();

        allSolutions.printObjectivesToFile(dir + "/hypervolume.txt");
    }

}
