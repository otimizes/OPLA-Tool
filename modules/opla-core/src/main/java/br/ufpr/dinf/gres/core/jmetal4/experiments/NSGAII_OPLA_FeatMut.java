package br.ufpr.dinf.gres.core.jmetal4.experiments;

import br.ufpr.dinf.gres.architecture.io.OPLAThreadScope;
import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Algorithm;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.database.Result;
import br.ufpr.dinf.gres.core.jmetal4.metaheuristics.nsgaII.NSGAII;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctions;
import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.Crossover;
import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.CrossoverFactory;
import br.ufpr.dinf.gres.core.jmetal4.operators.mutation.Mutation;
import br.ufpr.dinf.gres.core.jmetal4.operators.mutation.MutationFactory;
import br.ufpr.dinf.gres.core.jmetal4.operators.selection.Selection;
import br.ufpr.dinf.gres.core.jmetal4.operators.selection.SelectionFactory;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import br.ufpr.dinf.gres.domain.entity.AllMetrics;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.core.learning.Moment;
import br.ufpr.dinf.gres.core.persistence.ExperimentConfs;
import br.ufpr.dinf.gres.core.persistence.Persistence;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.BaseObjectiveFunction;
import br.ufpr.dinf.gres.loglog.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NSGAII_OPLA_FeatMut implements AlgorithmBaseExecution<NSGAIIConfig> {

    private static final Logger LOGGER = Logger.getLogger(NSGAII_OPLA_FeatMut.class);

    private final Persistence mp;
    private final CalculaEd c;


    public NSGAII_OPLA_FeatMut(Persistence mp, CalculaEd c) {
        this.mp = mp;
        this.c = c;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(File.separator) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(NSGAIIConfig configs) throws Exception {

        Experiment experiment = null;

        String context = "OPLA";

        String[] plas = configs.getPlas().split(",");
        String xmiFilePath;

        for (String pla : plas) {
            LOGGER.info("Criando uma PLA em: " + pla);
            xmiFilePath = pla;
            OPLA problem = null;
            String plaName = getPlaName(pla);
            LOGGER.info("Nome da PLA: " + plaName);

            try {
                problem = new OPLA(xmiFilePath, configs);
            } catch (Exception e) {
                LOGGER.error(e);
                e.printStackTrace();
                configs.getLogger()
                        .putLog(String.format("Error when try read architecture %s. %s", xmiFilePath, e.getMessage()));
                throw new JMException("Ocorreu um erro durante geração de PLAs");
            }

            LOGGER.info("Inicializando dependências");
            Result result = new Result();

            experiment = mp.saveExperiment(plaName, "NSGAII", configs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfs conf = new ExperimentConfs(experiment.getId(), "NSGAII", configs);
            mp.save(conf);
            LOGGER.info("Salvou configurações do experimento");


            SolutionSet allRuns = new SolutionSet();
            Algorithm algorithm = new NSGAII(problem);
            algorithm.setInputParameter("populationSize", configs.getPopulationSize());
            algorithm.setInputParameter("maxEvaluations", configs.getMaxEvaluation());
            algorithm.setInputParameter("interactiveFunction", configs.getInteractiveFunction());
            algorithm.setInputParameter("maxInteractions", configs.getMaxInteractions());
            algorithm.setInputParameter("firstInteraction", configs.getFirstInteraction());
            algorithm.setInputParameter("intervalInteraction", configs.getIntervalInteraction());
            algorithm.setInputParameter("interactive", configs.getInteractive());
            algorithm.setInputParameter("clusteringMoment", configs.getClusteringMoment());
            algorithm.setInputParameter("clusteringAlgorithm", configs.getClusteringAlgorithm());

            // Mutation and Crossover
            HashMap<String, Object> parametersCrossover = new HashMap<>();
            parametersCrossover.put("probability", configs.getCrossoverProbability());
            parametersCrossover.put("numberOfObjectives", configs.getOplaConfigs().getNumberOfObjectives());
            Crossover crossover = CrossoverFactory.getCrossoverOperator("PLACrossover", parametersCrossover, configs);
            algorithm.addOperator("crossover", crossover);

            HashMap<String, Object> parametersMutation = new HashMap<>();
            parametersMutation.put("probability", configs.getMutationProbability());
            Mutation mutation = MutationFactory.getMutationOperator("PLAFeatureMutation", parametersMutation, configs);
            algorithm.addOperator("mutation", mutation);

            Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament", null);
            algorithm.addOperator("selection", selection);


            if (configs.isLog())
                logInforamtions(context, pla, configs, configs.getPopulationSize(), configs.getMaxEvaluations(),
                        configs.getCrossoverProbability(), configs.getMutationProbability());

            List<String> selectedObjectiveFunctions = configs.getOplaConfigs().getSelectedObjectiveFunctions();
            mp.saveObjectivesNames(selectedObjectiveFunctions, experiment.getId());
            LOGGER.info("Salvou funções objetivo selecionadas");


            result.setPlaName(plaName);

            long[] time = new long[configs.getNumberOfRuns()];

            for (int runs = 0; runs < configs.getNumberOfRuns(); runs++) {

                // Cria uma execução. Cada execução está ligada a um
                // experiemento.
                Execution execution = new Execution(experiment);
                execution.setRuns(runs);
                CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), execution.getId());

                // Execute the Algorithm
                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominadas(resultFront);
                resultFront = problem.removeRepetidas(resultFront);

                execution = mp.save(execution);
                List<Info> infos = result.getInformations(resultFront.getSolutionSet(), execution, experiment);
                infos = mp.saveInfoAll(infos);
                execution.setInfos(infos);
                Map<String, List<BaseObjectiveFunction>> allMetrics = result.getMetrics(infos, resultFront.getSolutionSet(), execution,
                        experiment, selectedObjectiveFunctions);
                execution.setTime(estimatedTime);

                if (Moment.POSTERIORI.equals(configs.getClusteringMoment())) {
                    configs.getInteractiveFunction().run(resultFront);
                }

                resultFront.saveVariablesToFile("VAR_" + runs + "_", infos, configs.getLogger(), true);

                execution.setInfos(infos);
                execution.setAllMetrics(allMetrics);


                // armazena as solucoes de todas runs
                allRuns = allRuns.union(resultFront);

                // Util.copyFolder(experiement.getId(), execution.getId());
                // Util.moveAllFilesToExecutionDirectory(experiementId,
                // execution.getId());

                saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);
            }

            allRuns = problem.removeDominadas(allRuns);
            allRuns = problem.removeRepetidas(allRuns);

            configs.getLogger().putLog("------ All Runs - Non-dominated solutions --------", Level.INFO);
            List<Info> funResults = result.getObjectives(allRuns.getSolutionSet(), null, experiment);

            if (configs.getNumberOfRuns() > 1) {
                LOGGER.info("saveVariablesToFile()");
                allRuns.saveVariablesToFile("VAR_All_", funResults, configs.getLogger(), true);
            }


            List<Info> infos = result.getInformations(allRuns.getSolutionSet(), null, experiment);
            infos = mp.saveInfoAll(infos);
            LOGGER.info("saveInfoAll()");

            Map<String, List<BaseObjectiveFunction>> allMetrics = result.getMetrics(funResults, allRuns.getSolutionSet(), null, experiment,
                    selectedObjectiveFunctions);
            mp.save(allMetrics, configs.getOplaConfigs().getSelectedObjectiveFunctions());
            LOGGER.info("getMetrics()");

            CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), null);

            LOGGER.info("DistanceEuclideanPersistence.calculate()");

            mp.saveDistance(c.calcula(experiment.getId(), configs.getOplaConfigs().getNumberOfObjectives()), experiment.getId());
            infos = null;
            funResults = null;

            // Util.moveAllFilesToExecutionDirectory(experiementId, null);
            LOGGER.info("saveHypervolume()");
            saveHypervolume(experiment.getId(), null, allRuns, plaName);
        }

        // Util.moveResourceToExperimentFolder(this.experiementId);

    }

    private void logInforamtions(String context, String pla, NSGAIIConfig configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
        logarPainel(context, pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
        logarConsole(context, pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);

    }

    private void logarPainel(String context, String pla, NSGAIIConfig configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
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

    private void logarConsole(String context, String pla, NSGAIIConfig configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
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
