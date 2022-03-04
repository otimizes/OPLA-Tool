package br.otimizes.oplatool.api.gateway;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import br.otimizes.oplatool.api.dto.OptimizationDto;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.experiments.base.AlgorithmBaseUtils;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIConfigs;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic.UntilBest;
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
import br.ufpr.dinf.gres.loglog.Level;

@Component
public class UntilBestGateway implements IGateway<NSGAIIConfigs> {

    public UntilBestGateway(Persistence persistence, EdCalculation edCalculation) {
        this.persistence = persistence;
        this.edCalculation = edCalculation;
    }

    public void execute(OptimizationDto optimizationDto) {
        GatewayUtils.addListener();
        NSGAIIConfigs configs = new NSGAIIConfigs();
        GatewayUtils.setConfigs(optimizationDto, configs);
        configs.setPopulationSize(optimizationDto.getPopulationSize());
        try {
            this.execute(configs);
        } catch (Exception e) {
            e.printStackTrace();
            GatewayUtils.log("ERROR");
        }
        GatewayUtils.log("Fin");
    }

    private static final Logger LOGGER = Logger.getLogger(UntilBestGateway.class);
    private final Persistence persistence;
    private final EdCalculation edCalculation;

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
            Experiment experiment = persistence.save(plaName, "UntilBest", configs.getDescription(),
                    OPLAThreadScope.hash.get());
            ExperimentConfigurations conf = new ExperimentConfigurations(experiment.getId(), "UntilBest", configs);
            persistence.save(conf);
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
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominated(resultFront);
                resultFront = problem.removeRepeated(resultFront);

                Execution execution = AlgorithmBaseUtils.getExecution(configs, result, experiment,
                        selectedObjectiveFunctions, runs, resultFront, estimatedTime, persistence);
                allRuns = allRuns.union(resultFront);
                AlgorithmBaseUtils.saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);
            }
            allRuns = problem.removeDominated(allRuns);
            allRuns = problem.removeRepeated(allRuns);
            AlgorithmBaseUtils.logAndSave(configs, plaName, result, experiment, allRuns, selectedObjectiveFunctions,
                    persistence, edCalculation);
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
        Algorithm algorithm = new UntilBest(problem);
        return AlgorithmBaseUtils.getAlgorithm(configs, crossover, mutation, operatorLocal, selection, algorithm);
    }

    private void logInformation(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations,
            double crossoverProbability, double mutationProbability) {
        logPanel(pla, configs, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
        logConsole(pla, populationSize, maxEvaluations, crossoverProbability, mutationProbability);
    }

    private void logPanel(String pla, NSGAIIConfigs configs, int populationSize, int maxEvaluations,
            double crossoverProbability, double mutationProbability) {
        configs.getLogger().putLog("\n================ UntilBest ================", Level.INFO);
        AlgorithmBaseUtils.putLogContext(pla, configs, populationSize, maxEvaluations, crossoverProbability,
                mutationProbability);
    }

    private void logConsole(String pla, int populationSize, int maxEvaluations, double crossoverProbability,
            double mutationProbability) {
        LOGGER.info("================ UntilBest ================");
        AlgorithmBaseUtils.logContext(pla, populationSize, maxEvaluations, crossoverProbability, mutationProbability,
                LOGGER);
        LOGGER.info("================ UntilBest ================");
    }
}
