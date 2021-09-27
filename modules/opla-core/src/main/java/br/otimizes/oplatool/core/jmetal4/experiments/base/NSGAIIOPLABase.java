package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII.NSGAII;
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
public class NSGAIIOPLABase implements AlgorithmBase<NSGAIIConfigs> {

    private static final Logger LOGGER = Logger.getLogger(NSGAIIOPLABase.class);

    private final Persistence persistence;
    private final EdCalculation edCalculation;

    public NSGAIIOPLABase(Persistence persistence, EdCalculation edCalculation) {
        this.persistence = persistence;
        this.edCalculation = edCalculation;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(File.separator) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(NSGAIIConfigs experimentCommonConfigs) throws Exception {
        String[] plas = experimentCommonConfigs.getPlas().split(",");

        for (String xmiFilePath : plas) {
            String plaName = getPlaName(xmiFilePath);
            OPLA problem = AlgorithmBaseUtils.getOPLAProblem(experimentCommonConfigs, xmiFilePath);
            Result result = new Result();
            Experiment experiment = persistence.save(plaName, "NSGAII", experimentCommonConfigs.getDescription(), OPLAThreadScope.hashOnPosteriori.get());
            ExperimentConfigurations conf = new ExperimentConfigurations(experiment.getId(), "NSGAII", experimentCommonConfigs);
            persistence.save(conf);

            SolutionSet allRuns = new SolutionSet();
            Algorithm algorithm = getAlgorithm(problem, experimentCommonConfigs);
            if (experimentCommonConfigs.isLog())
                logInformation(xmiFilePath, experimentCommonConfigs, experimentCommonConfigs.getPopulationSize(), experimentCommonConfigs.getMaxEvaluations(),
                        experimentCommonConfigs.getCrossoverProbability(), experimentCommonConfigs.getMutationProbability());

            List<String> selectedObjectiveFunctions = experimentCommonConfigs.getOplaConfigs().getSelectedObjectiveFunctions();
            persistence.saveObjectivesNames(selectedObjectiveFunctions, experiment.getId());
            result.setPlaName(plaName);
            long[] time = new long[experimentCommonConfigs.getNumberOfRuns()];

            for (int runs = 0; runs < experimentCommonConfigs.getNumberOfRuns(); runs++) {

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();

                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominated(resultFront);
                resultFront = problem.removeRepeated(resultFront);

                Execution execution = AlgorithmBaseUtils.getExecution(experimentCommonConfigs, result, experiment, selectedObjectiveFunctions, runs, resultFront, estimatedTime, persistence);
                allRuns = allRuns.union(resultFront);
                AlgorithmBaseUtils.saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);
            }

            allRuns = problem.removeDominated(allRuns);
            allRuns = problem.removeRepeated(allRuns);

            AlgorithmBaseUtils.logAndSave(experimentCommonConfigs, plaName, result, experiment, allRuns, selectedObjectiveFunctions, persistence, edCalculation);

            if (Moment.POSTERIORI.equals(experimentCommonConfigs.getClusteringMoment())) {
                experimentCommonConfigs.getInteractiveFunction().run(allRuns);
            }
        }
    }

    private Algorithm getAlgorithm(OPLA problem, NSGAIIConfigs experimentCommonConfigs) throws JMException {
        Algorithm algorithm = new NSGAII(problem);
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
        return algorithm;
    }

    private void logInformation(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
        logPanel(pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
        logConsole(pla, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
    }

    private void logPanel(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
        configs.getLogger().putLog("\n================ NSGAII ================", Level.INFO);
        AlgorithmBaseUtils.putLogContext(pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
    }

    private void logConsole(String pla, int populationSize, int maxEvaluations, double crossoverProbability, double mutationProbability) {
        LOGGER.info("================ NSGAII ================");
        AlgorithmBaseUtils.logContext(pla, populationSize, maxEvaluations, crossoverProbability, mutationProbability, LOGGER);
        LOGGER.info("================ NSGAII ================");
    }
}
