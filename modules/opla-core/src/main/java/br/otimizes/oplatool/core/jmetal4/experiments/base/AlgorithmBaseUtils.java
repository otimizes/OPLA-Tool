package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.persistence.Persistence;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.otimizes.oplatool.domain.config.UserHome;
import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;
import br.otimizes.oplatool.domain.entity.Info;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ObjectiveFunctionDomain;
import br.otimizes.oplatool.persistence.service.OPLACommand;
import br.ufpr.dinf.gres.loglog.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlgorithmBaseUtils {

    public static OPLA getOPLAProblem(ExperimentCommonConfigs experimentCommonConfigs, String xmiFilePath) throws JMException {
        OPLA problem;
        try {
            problem = new OPLA(xmiFilePath, experimentCommonConfigs);
        } catch (Exception e) {
            e.printStackTrace();
            experimentCommonConfigs.getLogger()
                    .putLog(String.format("Error when try read architecture %s. %s", xmiFilePath, e.getMessage()));
            throw new JMException("Ocorreu um erro durante geração de PLAs");
        }
        return problem;
    }

    public static Algorithm getAlgorithm(NSGAIIConfigs configs, Crossover crossover, Mutation mutation, Mutation operatorLocal, Selection selection, Algorithm algorithm) {
        algorithm.setInputParameter("populationSize", configs.getPopulationSize());
        algorithm.setInputParameter("maxEvaluations", configs.getMaxEvaluation());
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("operatorLocal", operatorLocal);
        algorithm.addOperator("selection", selection);
        return algorithm;
    }

    public static void logContext(String pla, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability, Logger logger) {
        logger.info("Context: OPLA");
        logger.info("PLA: " + pla);
        logger.info("Params:");
        logger.info("tPop -> " + populationSize);
        logger.info("tMaxEva -> " + maxEvaluations);
        logger.info("tCross -> " + crossoverProbability);
        logger.info("tMuta -> " + mutationProbability);
    }


    public static void putLogContext(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
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

    public static Execution getExecution(ExperimentCommonConfigs configs, Result result, Experiment experiment, List<String> selectedObjectiveFunctions,
                                         int runs, SolutionSet resultFront, long estimatedTime, Persistence persistence) {
        Execution execution = new Execution(experiment);
        CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), execution.getId());
        execution = persistence.save(execution);
        List<Info> infos = result.getInfos(resultFront.getSolutionSet(), execution, experiment);
        infos = persistence.save(infos);
        execution.setInfos(infos);
        Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(infos, resultFront.getSolutionSet(), execution,
                experiment, selectedObjectiveFunctions);
        execution.setTime(estimatedTime);
        new OPLASolutionSet(resultFront).saveVariablesToFile("VAR_" + runs + "_", infos, configs.getLogger(), true);
        execution.setInfos(infos);
        execution.setAllMetrics(allMetrics);
        return execution;
    }

    public static void logContext(PAESConfigs configs, String context, String pla) {
        configs.getLogger().putLog("\n================ PAES ================", Level.INFO);
        configs.getLogger().putLog("Context: " + context, Level.INFO);
        configs.getLogger().putLog("PLA: " + pla, Level.INFO);
        configs.getLogger().putLog("Params:", Level.INFO);
        configs.getLogger().putLog("\tMaxEva -> " + configs.getMaxEvaluations(), Level.INFO);
        configs.getLogger().putLog("\tMuta -> " + configs.getMutationProbability(), Level.INFO);

        long heapSize = Runtime.getRuntime().totalMemory();
        heapSize = (heapSize / 1024) / 1024;
        configs.getLogger().putLog("Heap Size: " + heapSize + "Mb\n");
    }

    public static void logAndSave(ExperimentCommonConfigs configs, String plaName, Result result, Experiment experiment, SolutionSet allRuns, List<String> selectedObjectiveFunctions, Persistence persistence, EdCalculation edCalculation) throws Exception {
        configs.getLogger().putLog("------ All Runs - Non-dominated solutions --------", Level.INFO);
        List<Info> funResults = result.getInfos(allRuns.getSolutionSet(), experiment);

        if (configs.getNumberOfRuns() > 1) {
            new OPLASolutionSet(allRuns).saveVariablesToFile("VAR_All_", funResults, configs.getLogger(), true);
        }

        List<Info> infos = result.getInfos(allRuns.getSolutionSet(), null, experiment);
        persistence.save(infos);
        Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(funResults, allRuns.getSolutionSet(), null, experiment,
                selectedObjectiveFunctions);
        persistence.save(allMetrics);

        CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), null);
        persistence.saveEuclideanDistance(edCalculation.calculate(experiment.getId(), configs.getOplaConfigs().getNumberOfObjectives()), experiment.getId());
        saveHypervolume(experiment.getId(), null, allRuns, plaName);
    }


    public static void saveHypervolume(String experimentID, String executionID, SolutionSet allSolutions, String plaName) {
        String dir;
        if (executionID != null)
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + FileConstants.FILE_SEPARATOR + executionID + FileConstants.FILE_SEPARATOR + "fitness" + FileConstants.FILE_SEPARATOR;
        else
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + FileConstants.FILE_SEPARATOR + "fitness" + FileConstants.FILE_SEPARATOR;

        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();

        if (executionID != null) {
            for (Solution solution : allSolutions.getSolutionSet()) {
                solution.setExecutionId(executionID);
                solution.setExperimentId(experimentID);
            }
        }
        new OPLASolutionSet(allSolutions).printObjectivesToFile(dir + FileConstants.FILE_SEPARATOR + "fitness.txt");
        if (executionID == null) {
            String referencePoint = Arrays.stream(allSolutions.get(0).getObjectives()).mapToObj(s -> "1.01").collect(Collectors.joining(" "));
            try {
                OPLACommand.execCmd(UserHome.getOPLAHV() + " -r \"" + referencePoint + "\" " + dir + "fitness.hypervolume >> " + dir + "fitness.hypervolume.final");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
