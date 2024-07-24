package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic.Bestof2;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverFactory;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.MutationFactory;
import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
import br.otimizes.oplatool.core.jmetal4.operators.selection.SelectionFactory;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
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
public class BestOf2OPLABase {

    private static final Logger LOGGER = Logger.getLogger(BestOf2OPLABase.class);
    private final Persistence persistence;
    private final EdCalculation edCalculation;

    public BestOf2OPLABase(Persistence persistence, EdCalculation edCalculation) {
        this.persistence = persistence;
        this.edCalculation = edCalculation;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(File.separator) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(NSGAIIConfigs configs) throws Exception {
        int runsNumber = configs.getNumberOfRuns();
        String[] plas = configs.getPlas().split(",");
        for (String xmiFilePath : plas) {
            String plaName = getPlaName(xmiFilePath);
            OPLA problem = AlgorithmBaseUtils.getOPLAProblem(configs, xmiFilePath);
            Result result = new Result();
            Experiment experiment = persistence.save(plaName, "Bestof2", configs.getDescription(), OPLAThreadScope.hash.get());
            SolutionSet allRuns = new SolutionSet();
            Algorithm algorithm = getAlgorithm(problem, configs, experiment);
            if (configs.isLog())
                logInformation(xmiFilePath, configs, configs.getPopulationSize(), configs.getMaxEvaluations(),
                        configs.getCrossoverProbability(), configs.getMutationProbability());
            List<String> selectedObjectiveFunctions = configs.getOplaConfigs().getSelectedObjectiveFunctions();
            persistence.saveObjectivesNames(selectedObjectiveFunctions, experiment.getId());
            result.setPlaName(plaName);
            long[] time = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {
                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                resultFront = problem.removeDominated(resultFront);
                resultFront = problem.removeRepeated(resultFront);
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                Execution execution = AlgorithmBaseUtils.getExecution(configs, result, experiment, selectedObjectiveFunctions, runs, resultFront, estimatedTime, persistence);
                allRuns = allRuns.union(resultFront);
                AlgorithmBaseUtils.saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);
            }
            allRuns = problem.removeDominated(allRuns);
            allRuns = problem.removeRepeated(allRuns);
            AlgorithmBaseUtils.logAndSave(configs, plaName, result, experiment, allRuns, selectedObjectiveFunctions, persistence, edCalculation);
        }
    }

    private Algorithm getAlgorithm(OPLA problem, NSGAIIConfigs configs, Experiment experiment) throws JMException {
        ExperimentConfigurations conf = new ExperimentConfigurations(experiment.getId(), "BestOf2", configs);
        persistence.save(conf);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("probability", configs.getCrossoverProbability());
        Crossover crossover = CrossoverFactory.getCrossoverOperator("PLACrossoverOperator", parameters, configs);
        parameters = new HashMap<>();
        parameters.put("probability", configs.getMutationProbability());
        Mutation mutation = MutationFactory.getMutationOperator("PLAMutationOperator", parameters, configs);
        Mutation operatorLocal = MutationFactory.getMutationOperatorPatterns("PLAPatternsMutation", parameters,
                configs);
        Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament", null);
        Algorithm algorithm = new Bestof2(problem);
        return AlgorithmBaseUtils.getAlgorithm(configs, crossover, mutation, operatorLocal, selection, algorithm);
    }

    private void logInformation(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations,
                                double crossoverProbability, double mutationProbability) {
        logPanel(pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
        logConsole(pla, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
    }

    private void logPanel(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations,
                          double crossoverProbability, double mutationProbability) {
        configs.getLogger().putLog("\n================ BestOf2 ================", Level.INFO);
        AlgorithmBaseUtils.putLogContext(pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
    }

    private void logConsole(String pla, int populationSize, int maxEvaluations,
                            double crossoverProbability, double mutationProbability) {
        LOGGER.info("================ BestOf2 ================");
        AlgorithmBaseUtils.logContext(pla, populationSize, maxEvaluations, crossoverProbability, mutationProbability, LOGGER);
        LOGGER.info("================ BestOf2 ================");
    }
}

