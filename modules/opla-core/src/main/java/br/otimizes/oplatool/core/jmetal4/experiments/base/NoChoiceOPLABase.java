package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic.NoChoice;
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
public class NoChoiceOPLABase {

    private static final Logger LOGGER = Logger.getLogger(NoChoiceOPLABase.class);
    private final Persistence mp;
    private final EdCalculation c;

    public NoChoiceOPLABase(Persistence mp, EdCalculation c) {
        this.mp = mp;
        this.c = c;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(File.separator) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(NSGAIIConfigs configs) throws Exception {
        int populationSize;
        Experiment experiment;
        int maxEvaluations;
        double mutationProbability;
        double mutationLocalProbability;
        double crossoverProbability;
        String[] testePadroes;
        List<String> testeOperadores;


        String experimentId;
        int numberObjectives;
        int numeroFuncoesObjetivo;


        int runsNumber = configs.getNumberOfRuns();
        populationSize = configs.getPopulationSize();
        maxEvaluations = configs.getMaxEvaluation();
        crossoverProbability = configs.getCrossoverProbability();
        mutationProbability = configs.getMutationProbability();
        mutationLocalProbability = 0.9;
        numberObjectives = configs.getOplaConfigs().getNumberOfObjectives();

        String context = "OPLA";

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
            }


            Algorithm algorithm;
            Result result = new Result();
            experiment = mp.save(plaName, "NoChoice", configs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfigurations conf = new ExperimentConfigurations(experiment.getId(), "NoChoice", configs);
            mp.save(conf);
            Crossover crossover;
            Mutation mutation;
            Mutation operatorLocal;
            Selection selection;
            HashMap<String, Object> parameters;
            algorithm = new NoChoice(problem);
            SolutionSet todasRuns = new SolutionSet();
            algorithm.setInputParameter("populationSize", populationSize);
            algorithm.setInputParameter("maxEvaluations", maxEvaluations);
            parameters = new HashMap<>();
            parameters.put("probability", crossoverProbability);
            crossover = CrossoverFactory.getCrossoverOperator("PLACrossoverOperator", parameters);
            parameters = new HashMap<>();
            parameters.put("probability", mutationProbability);
            mutation = MutationFactory.getMutationOperator("PLAMutationOperator", parameters, configs);
            operatorLocal = MutationFactory.getMutationOperatorPatterns("PLAPatternsMutation", parameters,
                    configs);
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);
            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("operatorLocal", operatorLocal);
            algorithm.addOperator("selection", selection);

            if (configs.isLog())
                logInformations(pla, configs, configs.getPopulationSize(), configs.getMaxEvaluations(),
                        configs.getCrossoverProbability(), configs.getMutationProbability());

            List<String> selectedObjectiveFunctions = configs.getOplaConfigs().getSelectedObjectiveFunctions();
            mp.saveObjectivesNames(selectedObjectiveFunctions, experiment.getId());

            result.setPlaName(plaName);

            long[] time = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {
                // jcn
                // System.out.println("nova rodada - oplacore - classe
                // nsgaii_opla_feat_mut");
                // Cria uma execução. Cada execução está ligada a um
                // experimento.
                Execution execution = new Execution(experiment);
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
                new OPLASolutionSet(resultFront).saveVariablesToFile("VAR_" + runs + "_", infos, configs.getLogger(), true);
                execution.setInfos(infos);
                execution.setAllMetrics(allMetrics);
                todasRuns = todasRuns.union(resultFront);
                OPLABaseUtils.saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);

            }

            todasRuns = problem.removeDominated(todasRuns);
            todasRuns = problem.removeRepeated(todasRuns);


            configs.getLogger().putLog("------ All Runs - Non-dominated solutions --------", Level.INFO);
            List<Info> funResults = result.getInfos(todasRuns.getSolutionSet(), experiment);

            if (configs.getNumberOfRuns() > 1) {
                new OPLASolutionSet(todasRuns).saveVariablesToFile("VAR_All_", funResults, configs.getLogger(), true);
            }

            List<Info> infos = result.getInfos(todasRuns.getSolutionSet(), null, experiment);
            mp.save(infos);
            Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(funResults, todasRuns.getSolutionSet(), null, experiment,
                    selectedObjectiveFunctions);
            mp.save(allMetrics);

            CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), null);
            mp.saveEuclideanDistance(c.calculate(experiment.getId(), configs.getOplaConfigs().getNumberOfObjectives()), experiment.getId());
            OPLABaseUtils.saveHypervolume(experiment.getId(), null, todasRuns, plaName);
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

