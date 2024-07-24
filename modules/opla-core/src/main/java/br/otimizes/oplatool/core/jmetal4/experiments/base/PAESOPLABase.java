package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.database.Result;
import br.otimizes.oplatool.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.otimizes.oplatool.core.jmetal4.experiments.EdCalculation;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.paes.PAES;
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
        String[] plas = experimentCommonConfigs.getPlas().split(",");

        for (String xmiFilePath : plas) {
            String plaName = getPlaName(xmiFilePath);
            OPLA problem = AlgorithmBaseUtils.getOPLAProblem(experimentCommonConfigs, xmiFilePath);
            Result result = new Result();
            Experiment experiment = persistence.save(plaName, "PAES", experimentCommonConfigs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfigurations conf = new ExperimentConfigurations(experiment.getId(), "PAES", experimentCommonConfigs);
            persistence.save(conf);
            SolutionSet allRuns = new SolutionSet();
            Algorithm algorithm = getAlgorithm(experimentCommonConfigs, problem);

            if (experimentCommonConfigs.isLog())
                AlgorithmBaseUtils.logContext(experimentCommonConfigs, "OPLA", xmiFilePath);

            List<String> selectedObjectiveFunctions = experimentCommonConfigs.getOplaConfigs().getSelectedObjectiveFunctions();
            persistence.saveObjectivesNames(experimentCommonConfigs.getOplaConfigs().getSelectedObjectiveFunctions(), experiment.getId());

            result.setPlaName(plaName);

            long[] time = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominated(resultFront);
                resultFront = problem.removeRepeated(resultFront);

                Execution execution = AlgorithmBaseUtils.getExecution(experimentCommonConfigs, result, experiment, selectedObjectiveFunctions, runs, resultFront, estimatedTime, persistence);

                persistence.save(execution);
                allRuns = allRuns.union(resultFront);
                AlgorithmBaseUtils.saveHypervolume(experiment.getId(), execution.getId(), resultFront, plaName);
            }
            allRuns = problem.removeDominated(allRuns);
            allRuns = problem.removeRepeated(allRuns);

            AlgorithmBaseUtils.logAndSave(experimentCommonConfigs, plaName, result, experiment, allRuns, selectedObjectiveFunctions, persistence, edCalculation);
        }
    }

    private Algorithm getAlgorithm(PAESConfigs experimentCommonConfigs, OPLA problem) throws JMException {
        Algorithm algorithm = new PAES(problem);
        algorithm.setInputParameter("maxEvaluations", experimentCommonConfigs.getMaxEvaluation());
        algorithm.setInputParameter("archiveSize", experimentCommonConfigs.getArchiveSize());
        algorithm.setInputParameter("biSections", 5);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("probability", experimentCommonConfigs.getCrossoverProbability());
        Crossover crossover = CrossoverFactory.getCrossoverOperator("PLACrossoverOperator", parameters, experimentCommonConfigs);
        parameters = new HashMap<>();
        parameters.put("probability", experimentCommonConfigs.getMutationProbability());
        Mutation mutation = MutationFactory.getMutationOperator("PLAMutationOperator", parameters, experimentCommonConfigs);
        Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament", null);
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);
        return algorithm;
    }
}
