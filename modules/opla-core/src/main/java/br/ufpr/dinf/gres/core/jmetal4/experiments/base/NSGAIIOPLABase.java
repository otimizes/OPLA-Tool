package br.ufpr.dinf.gres.core.jmetal4.experiments.base;

import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;
import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Algorithm;
import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.database.Result;
import br.ufpr.dinf.gres.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.ufpr.dinf.gres.core.jmetal4.experiments.EdCalculation;
import br.ufpr.dinf.gres.core.jmetal4.metaheuristics.nsgaII.NSGAII;
import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.Crossover;
import br.ufpr.dinf.gres.core.jmetal4.operators.crossover.CrossoverFactory;
import br.ufpr.dinf.gres.core.jmetal4.operators.mutation.Mutation;
import br.ufpr.dinf.gres.core.jmetal4.operators.mutation.MutationFactory;
import br.ufpr.dinf.gres.core.jmetal4.operators.selection.Selection;
import br.ufpr.dinf.gres.core.jmetal4.operators.selection.SelectionFactory;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import br.ufpr.dinf.gres.core.learning.Moment;
import br.ufpr.dinf.gres.core.persistence.ExperimentConfs;
import br.ufpr.dinf.gres.core.persistence.Persistence;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.Info;
import br.ufpr.dinf.gres.domain.entity.objectivefunctions.ObjectiveFunctionDomain;
import br.ufpr.dinf.gres.loglog.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NSGAIIOPLABase implements AlgorithmBase<NSGAIIConfigs> {

    private static final Logger LOGGER = Logger.getLogger(NSGAIIOPLABase.class);

    private final Persistence mp;
    private final EdCalculation c;

    public NSGAIIOPLABase(Persistence mp, EdCalculation c) {
        this.mp = mp;
        this.c = c;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(File.separator) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(NSGAIIConfigs configs) throws Exception {
        Experiment experiment;
        String[] plas = configs.getPlas().split(",");
        String xmiFilePath;

        for (String pla : plas) {
            xmiFilePath = pla;
            OPLA problem = null;
            String plaName = getPlaName(pla);
            try {
                problem = new OPLA(xmiFilePath, configs);
            } catch (Exception e) {
                e.printStackTrace();
                configs.getLogger()
                        .putLog(String.format("Error when try read architecture %s. %s", xmiFilePath, e.getMessage()));
                throw new JMException("Ocorreu um erro durante geração de PLAs");
            }
            Result result = new Result();
            experiment = mp.saveExperiment(plaName, "NSGAII", configs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfs conf = new ExperimentConfs(experiment.getId(), "NSGAII", configs);
            mp.save(conf);

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
                logInformations(pla, configs, configs.getPopulationSize(), configs.getMaxEvaluations(),
                        configs.getCrossoverProbability(), configs.getMutationProbability());

            List<String> selectedObjectiveFunctions = configs.getOplaConfigs().getSelectedObjectiveFunctions();
            mp.saveObjectivesNames(selectedObjectiveFunctions, experiment.getId());
            result.setPlaName(plaName);
            long[] time = new long[configs.getNumberOfRuns()];

            for (int runs = 0; runs < configs.getNumberOfRuns(); runs++) {
                Execution execution = new Execution(experiment);
                execution.setRuns(runs);
                CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), execution.getId());

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();

                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominadas(resultFront);
                resultFront = problem.removeRepetidas(resultFront);

                execution = mp.save(execution);
                List<Info> infos = result.getInfos(resultFront.getSolutionSet(), execution, experiment);
                infos = mp.saveInfoAll(infos);
                execution.setInfos(infos);
                Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(infos, resultFront.getSolutionSet(), execution,
                        experiment, selectedObjectiveFunctions);
                execution.setTime(estimatedTime);
                new OPLASolutionSet(resultFront).saveVariablesToFile("VAR_" + runs + "_", infos, configs.getLogger(), true);
                execution.setInfos(infos);
                execution.setAllMetrics(allMetrics);
                allRuns = allRuns.union(resultFront);
                saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);
            }

            allRuns = problem.removeDominadas(allRuns);
            allRuns = problem.removeRepetidas(allRuns);

            configs.getLogger().putLog("------ All Runs - Non-dominated solutions --------", Level.INFO);
            List<Info> funResults = result.getInfos(allRuns.getSolutionSet(), experiment);

            if (configs.getNumberOfRuns() > 1) {
                new OPLASolutionSet(allRuns).saveVariablesToFile("VAR_All_", funResults, configs.getLogger(), true);
            }

            List<Info> infos = result.getInfos(allRuns.getSolutionSet(), null, experiment);
            mp.saveInfoAll(infos);
            Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(funResults, allRuns.getSolutionSet(), null, experiment,
                    selectedObjectiveFunctions);
            mp.save(allMetrics, configs.getOplaConfigs().getSelectedObjectiveFunctions());

            CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), null);
            mp.saveDistance(c.calculate(experiment.getId(), configs.getOplaConfigs().getNumberOfObjectives()), experiment.getId());
            saveHypervolume(experiment.getId(), null, allRuns, plaName);

            SaveStringToFile.getInstance().deleteTempFolder();

            if (Moment.POSTERIORI.equals(configs.getClusteringMoment())) {
                configs.getInteractiveFunction().run(allRuns);
            }
        }
    }

    private void logInformations(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
        logPanel(pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
        logConsole(pla, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
    }

    private void logPanel(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
        configs.getLogger().putLog("\n================ NSGAII ================", Level.INFO);
        configs.getLogger().putLog("Context: OPLA", Level.INFO);
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

    private void logConsole(String pla, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
        LOGGER.info("================ NSGAII ================");
        LOGGER.info("Context: OPLA");
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

        new OPLASolutionSet(allSolutions).printObjectivesToFile(dir + "/hypervolume.txt");
    }

}
