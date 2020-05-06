package br.ufpr.dinf.gres.core.jmetal4.experiments;

import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.core.jmetal4.core.Algorithm;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.database.Result;
import br.ufpr.dinf.gres.core.jmetal4.metaheuristics.paes.PAES;
import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.Crossover;
import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.CrossoverFactory;
import br.ufpr.dinf.gres.core.jmetal4.operators.mutation.Mutation;
import br.ufpr.dinf.gres.core.jmetal4.operators.mutation.MutationFactory;
import br.ufpr.dinf.gres.core.jmetal4.operators.selection.Selection;
import br.ufpr.dinf.gres.core.jmetal4.operators.selection.SelectionFactory;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import br.ufpr.dinf.gres.core.persistence.ExperimentConfs;
import br.ufpr.dinf.gres.core.persistence.Persistence;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ObjectiveFunctionDomain;
import br.ufpr.dinf.gres.loglog.Level;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PAES_OPLA_FeatMut implements AlgorithmBaseExecution<PaesConfigs> {

    public static int populationSize;
    public static int maxEvaluations;
    public static double mutationProbability;
    public static double crossoverProbability;
    private static Persistence mp;
    private static Result result;
    public String dirToSaveOutput; //Diretório que sera criado dentro do diretorio configurado no arquivo de configuracao

    private PaesConfigs configs;
    private String experiementId;
    private int numberObjectives;
    private final CalculaEd c;

    public PAES_OPLA_FeatMut(CalculaEd c) {
        this.c = c;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(System.getProperty("file.separator")) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(PaesConfigs configs) throws Exception {

        intializeDependencies();

        int runsNumber = this.configs.getNumberOfRuns();
        maxEvaluations = this.configs.getMaxEvaluation();
        int archiveSize = this.configs.getArchiveSize();
        int biSections = 5;
        mutationProbability = this.configs.getMutationProbability();
        this.numberObjectives = this.configs.getOplaConfigs().getNumberOfObjectives();
        String context = "OPLA";

        String plas[] = this.configs.getPlas().split(",");
        String xmiFilePath;

        for (String pla : plas) {
            xmiFilePath = pla;
            OPLA problem = null;
            String plaName = getPlaName(pla);

            try {
                problem = new OPLA(xmiFilePath, this.configs);
            } catch (Exception e) {
                this.configs.getLogger().putLog(String.format("Error when try read architecture %s. %s", xmiFilePath, e.getMessage()));
            }

            Experiment experiement = mp.saveExperiment(plaName, "PAES", configs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfs conf = new ExperimentConfs(experiement.getId(), "PAES", configs);
            mp.save(conf);

            Algorithm algorithm;
            SolutionSet todasRuns = new SolutionSet();

            Crossover crossover;
            Mutation mutation;
            Selection selection;

            HashMap<String, Object> parameters;

            algorithm = new PAES(problem);

            // Algorithm parameters
            algorithm.setInputParameter("maxEvaluations", maxEvaluations);
            algorithm.setInputParameter("archiveSize", archiveSize);
            algorithm.setInputParameter("biSections", biSections);

            // Mutation and Crossover
            parameters = new HashMap<String, Object>();
            parameters.put("probability", crossoverProbability);
            //TODO avaliar configurações na execução do algoritmo
            crossover = CrossoverFactory.getCrossoverOperator("PLACrossover", parameters, this.configs);

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
            mp.saveObjectivesNames(this.configs.getOplaConfigs().getSelectedObjectiveFunctions(), experiement.getId());

            result.setPlaName(plaName);

            long time[] = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {

                // Cria uma execução. Cada execução está ligada a um
                // experiemento.
                Execution execution = new Execution(experiement);
                setDirToSaveOutput(experiement.getId(), execution.getId());

                // Execute the Algorithm
                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominadas(resultFront);
                resultFront = problem.removeRepetidas(resultFront);

                execution.setTime(estimatedTime);

                List<Info> Info = result.getInformations(resultFront.getSolutionSet(), execution, experiement);
                Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(Info, resultFront.getSolutionSet(), execution, experiement, selectedObjectiveFunctions);

                ((OPLASolutionSet) resultFront).saveVariablesToFile("VAR_" + runs + "_", Info, this.configs.getLogger(), true);

                execution.setInfos(Info);
                execution.setAllMetrics(allMetrics);

                mp.save(execution);
                // armazena as solucoes de todas runs
                todasRuns = todasRuns.union(resultFront);

                //Util.copyFolder(experiement.getId(), execution.getId());
                //Util.moveAllFilesToExecutionDirectory(experiementId, execution.getId());

                saveHypervolume(experiement.getId(), execution.getId(), resultFront, plaName);

            }

            todasRuns = problem.removeDominadas(todasRuns);
            todasRuns = problem.removeRepetidas(todasRuns);

            configs.getLogger().putLog("------All Runs - Non-dominated solutions --------");
            List<Info> funResults = result.getObjectives(todasRuns.getSolutionSet(), null, experiement);

            ((OPLASolutionSet) todasRuns).saveVariablesToFile("VAR_All_", funResults, this.configs.getLogger(), true);


            List<Info> Info = result.getInformations(todasRuns.getSolutionSet(), null, experiement);
            mp.saveInfoAll(Info);

            Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(funResults, todasRuns.getSolutionSet(), null, experiement,
                    selectedObjectiveFunctions);
            mp.save(allMetrics, this.configs.getOplaConfigs().getSelectedObjectiveFunctions());
            mp = null;

            setDirToSaveOutput(experiement.getId(), null);

            mp.saveDistance(c.calcula(this.experiementId, this.numberObjectives), this.experiementId);
            Info = null;
            funResults = null;

            //Util.moveAllFilesToExecutionDirectory(experiementId, null);
            saveHypervolume(experiement.getId(), null, todasRuns, plaName);
        }

        //Util.moveResourceToExperimentFolder(this.experiementId);

    }

    private void intializeDependencies() throws Exception {
        result = new Result();
    }

    private void logInforamtions(String context, String pla) {
        configs.getLogger().putLog("\n================ PAES ================", Level.INFO);
        configs.getLogger().putLog("Context: " + context, Level.INFO);
        configs.getLogger().putLog("PLA: " + pla, Level.INFO);
        configs.getLogger().putLog("Params:", Level.INFO);
        configs.getLogger().putLog("\tMaxEva -> " + maxEvaluations, Level.INFO);
        configs.getLogger().putLog("\tMuta -> " + mutationProbability, Level.INFO);

        long heapSize = Runtime.getRuntime().totalMemory();
        heapSize = (heapSize / 1024) / 1024;
        configs.getLogger().putLog("Heap Size: " + heapSize + "Mb\n");
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

        ((OPLASolutionSet) allSolutions).printObjectivesToFile(dir + "/hypervolume.txt");
    }


}
