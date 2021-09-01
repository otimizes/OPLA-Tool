package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverFactory;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.MutationFactory;
import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
import br.otimizes.oplatool.core.jmetal4.operators.selection.SelectionFactory;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.paes.PAES;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.persistence.ExperimentConfigurations;
import br.otimizes.oplatool.core.persistence.Persistence;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;
import br.otimizes.oplatool.domain.entity.Info;
import br.otimizes.oplatool.domain.entity.objectivefunctions.ObjectiveFunctionDomain;
import br.ufpr.dinf.gres.loglog.Level;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PAESOPLABase implements AlgorithmBase<PAESConfigs> {

    private final Persistence persistence;
    private final EdCalculation edCalculation;

    public PAESOPLABase(Persistence persistence, EdCalculation edCalculation) {
        this.persistence = persistence;
        this.edCalculation = edCalculation;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(FileConstants.FILE_SEPARATOR) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(PAESConfigs experimentCommonConfigs) throws Exception {
        int runsNumber = experimentCommonConfigs.getNumberOfRuns();
        int maxEvaluations = experimentCommonConfigs.getMaxEvaluation();
        int archiveSize = experimentCommonConfigs.getArchiveSize();
        int biSections = 5;
        double crossoverProbability = experimentCommonConfigs.getCrossoverProbability();
        double mutationProbability = experimentCommonConfigs.getMutationProbability();
        int numberObjectives = experimentCommonConfigs.getOplaConfigs().getNumberOfObjectives();
        String context = "OPLA";

        Experiment experiment;
        String[] plas = experimentCommonConfigs.getPlas().split(",");
        String xmiFilePath;

        for (String pla : plas) {
            xmiFilePath = pla;
            OPLA problem;
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
            experiment = persistence.save(plaName, "PAES", experimentCommonConfigs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfigurations conf = new ExperimentConfigurations(experiment.getId(), "PAES", experimentCommonConfigs);
            persistence.save(conf);


            Algorithm algorithm;
            SolutionSet allRuns = new SolutionSet();

            Crossover crossover;
            Mutation mutation;
            Selection selection;

            HashMap<String, Object> parameters;

            algorithm = new PAES(problem);

            algorithm.setInputParameter("maxEvaluations", maxEvaluations);
            algorithm.setInputParameter("archiveSize", archiveSize);
            algorithm.setInputParameter("biSections", biSections);

            parameters = new HashMap<>();
            parameters.put("probability", crossoverProbability);
            crossover = CrossoverFactory.getCrossoverOperator("PLACrossoverOperator", parameters, experimentCommonConfigs);

            parameters = new HashMap<>();
            parameters.put("probability", mutationProbability);
            mutation = MutationFactory.getMutationOperator("PLAMutationOperator", parameters, experimentCommonConfigs);
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            if (experimentCommonConfigs.isLog())
                logInformations(experimentCommonConfigs, context, pla);

            List<String> selectedObjectiveFunctions = experimentCommonConfigs.getOplaConfigs().getSelectedObjectiveFunctions();
            persistence.saveObjectivesNames(experimentCommonConfigs.getOplaConfigs().getSelectedObjectiveFunctions(), experiment.getId());

            result.setPlaName(plaName);

            long[] time = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {
                Execution execution = new Execution(experiment);
                CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), execution.getId());

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominated(resultFront);
                resultFront = problem.removeRepeated(resultFront);

                execution.setTime(estimatedTime);

                List<Info> Info = result.getInfos(resultFront.getSolutionSet(), execution, experiment);
                Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(Info, resultFront.getSolutionSet(), execution, experiment, selectedObjectiveFunctions);

                new OPLASolutionSet(resultFront).saveVariablesToFile("VAR_" + runs + "_", Info, experimentCommonConfigs.getLogger(), true);

                execution.setInfos(Info);
                execution.setAllMetrics(allMetrics);

                persistence.save(execution);
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
            persistence.save(infos);
            Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(funResults, allRuns.getSolutionSet(), null, experiment,
                    selectedObjectiveFunctions);
            persistence.save(allMetrics);

            CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), null);
            persistence.saveEuclideanDistance(edCalculation.calculate(experiment.getId(), experimentCommonConfigs.getOplaConfigs().getNumberOfObjectives()), experiment.getId());
            OPLABaseUtils.saveHypervolume(experiment.getId(), null, allRuns, plaName);
        }
    }


    private void logInformations(PAESConfigs configs, String context, String pla) {
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
}
