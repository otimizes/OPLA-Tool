package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII.NSGAIIASP;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverFactory;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.MutationFactory;
import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
import br.otimizes.oplatool.core.jmetal4.operators.selection.SelectionFactory;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.learning.Moment;
import br.otimizes.oplatool.core.persistence.ExperimentConfigurations;
import br.otimizes.oplatool.core.persistence.Persistence;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;
import br.otimizes.oplatool.domain.entity.Info;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ObjectiveFunctionDomain;
import br.ufpr.dinf.gres.loglog.Level;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NSGAIIAPSOPLABase implements AlgorithmBase<NSGAIIConfigs> {

    private static final Logger LOGGER = Logger.getLogger(NSGAIIAPSOPLABase.class);

    private final Persistence mp;
    private final EdCalculation c;

    public NSGAIIAPSOPLABase(Persistence mp, EdCalculation c) {
        this.mp = mp;
        this.c = c;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(File.separator) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(NSGAIIConfigs experimentCommonConfigs) throws Exception {
        Experiment experiment;
        String[] plas = experimentCommonConfigs.getPlas().split(",");
        String xmiFilePath;

        for (String pla : plas) {
            xmiFilePath = pla;
            OPLA problem = null;
            String plaName = getPlaName(pla);
            try {
                problem = new OPLA(xmiFilePath, experimentCommonConfigs);
            } catch (Exception e) {
                e.printStackTrace();
                experimentCommonConfigs.getLogger()
                        .putLog(String.format("Error when try read architecture %s. %s", xmiFilePath, e.getMessage()));
                throw new JMException("Ocorreu um erro durante geração de PLAs");
            }
            Result result = new Result();
            experiment = mp.save(plaName, "NSGAII", experimentCommonConfigs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfigurations conf = new ExperimentConfigurations(experiment.getId(), "NSGAII", experimentCommonConfigs);
            mp.save(conf);

            SolutionSet allRuns = new SolutionSet();
            Algorithm algorithm = new NSGAIIASP(problem);
            algorithm.setInputParameter("populationSize", experimentCommonConfigs.getPopulationSize());
            algorithm.setInputParameter("maxEvaluations", experimentCommonConfigs.getMaxEvaluation());
            algorithm.setInputParameter("interactiveFunction", experimentCommonConfigs.getInteractiveFunction());
            algorithm.setInputParameter("maxInteractions", experimentCommonConfigs.getMaxInteractions());
            algorithm.setInputParameter("firstInteraction", experimentCommonConfigs.getFirstInteraction());
            algorithm.setInputParameter("intervalInteraction", experimentCommonConfigs.getIntervalInteraction());
            algorithm.setInputParameter("interactive", experimentCommonConfigs.getInteractive());
            algorithm.setInputParameter("clusteringMoment", experimentCommonConfigs.getClusteringMoment());
            algorithm.setInputParameter("clusteringAlgorithm", experimentCommonConfigs.getClusteringAlgorithm());

            HashMap<String, Object> parametersCrossover = new HashMap<>();
            parametersCrossover.put("probability", experimentCommonConfigs.getCrossoverProbability());
            parametersCrossover.put("numberOfObjectives", experimentCommonConfigs.getOplaConfigs().getNumberOfObjectives());
            Crossover crossover = CrossoverFactory.getCrossoverOperator("PLACrossoverOperator", parametersCrossover, experimentCommonConfigs);
            algorithm.addOperator("crossover", crossover);

            HashMap<String, Object> parametersMutation = new HashMap<>();
            parametersMutation.put("probability", experimentCommonConfigs.getMutationProbability());
            Mutation mutation = MutationFactory.getMutationOperator("PLAMutationOperator", parametersMutation, experimentCommonConfigs);
            algorithm.addOperator("mutation", mutation);

            Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament", null);
            algorithm.addOperator("selection", selection);

            if (experimentCommonConfigs.isLog())
                logInformations(pla, experimentCommonConfigs, experimentCommonConfigs.getPopulationSize(), experimentCommonConfigs.getMaxEvaluations(),
                        experimentCommonConfigs.getCrossoverProbability(), experimentCommonConfigs.getMutationProbability());

            List<String> selectedObjectiveFunctions = experimentCommonConfigs.getOplaConfigs().getSelectedObjectiveFunctions();
            mp.saveObjectivesNames(selectedObjectiveFunctions, experiment.getId());
            result.setPlaName(plaName);
            long[] time = new long[experimentCommonConfigs.getNumberOfRuns()];

            for (int runs = 0; runs < experimentCommonConfigs.getNumberOfRuns(); runs++) {
                Execution execution = new Execution(experiment);
                execution.setRuns(runs);
                CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), execution.getId());

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();

                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominated(resultFront);
                resultFront = problem.removeRepeated(resultFront);

                execution = mp.save(execution);
                List<Info> infos = result.getInfos(resultFront.getSolutionSet(), execution, experiment);
                infos = mp.save(infos);
                execution.setInfos(infos);
                Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(infos, resultFront.getSolutionSet(), execution,
                        experiment, selectedObjectiveFunctions);
                execution.setTime(estimatedTime);
                new OPLASolutionSet(resultFront).saveVariablesToFile("VAR_" + runs + "_", infos, experimentCommonConfigs.getLogger(), true);
                execution.setInfos(infos);
                execution.setAllMetrics(allMetrics);
                allRuns = allRuns.union(resultFront);
                OPLABaseUtils.saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);
            }

            allRuns = problem.removeDominated(allRuns);
            allRuns = problem.removeRepeated(allRuns);

            experimentCommonConfigs.getLogger().putLog("------ All Runs - Non-dominated solutions --------", Level.INFO);
            List<Info> funResults = result.getInfos(allRuns.getSolutionSet(), experiment);

            if (experimentCommonConfigs.getNumberOfRuns() > 1) {
                new OPLASolutionSet(allRuns).saveVariablesToFile("VAR_All_", funResults, experimentCommonConfigs.getLogger(), true);
            }

            List<Info> infos = result.getInfos(allRuns.getSolutionSet(), null, experiment);
            mp.save(infos);
            Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(funResults, allRuns.getSolutionSet(), null, experiment,
                    selectedObjectiveFunctions);
            mp.save(allMetrics);

            CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), null);
            mp.saveEuclideanDistance(c.calculate(experiment.getId(), experimentCommonConfigs.getOplaConfigs().getNumberOfObjectives()), experiment.getId());
            OPLABaseUtils.saveHypervolume(experiment.getId(), null, allRuns, plaName);

            if (Moment.POSTERIORI.equals(experimentCommonConfigs.getClusteringMoment())) {
                experimentCommonConfigs.getInteractiveFunction().run(allRuns);
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

}
