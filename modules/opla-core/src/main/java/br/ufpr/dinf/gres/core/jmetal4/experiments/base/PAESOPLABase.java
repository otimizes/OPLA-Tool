package br.ufpr.dinf.gres.core.jmetal4.experiments.base;

import br.ufpr.dinf.gres.architecture.io.OPLAConfigThreadScopeReader;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Algorithm;
import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.database.Result;
import br.ufpr.dinf.gres.core.jmetal4.experiments.CommonOPLAFeatMut;
import br.ufpr.dinf.gres.core.jmetal4.experiments.EdCalculation;
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
public class PAESOPLABase implements AlgorithmBase<PAESConfigs> {

    private final Persistence mp;
    private final EdCalculation c;

    public PAESOPLABase(Persistence mp, EdCalculation c) {
        this.mp = mp;
        this.c = c;
    }

    private static String getPlaName(String pla) {
        int beginIndex = pla.lastIndexOf(System.getProperty("file.separator")) + 1;
        int endIndex = pla.length() - 4;
        return pla.substring(beginIndex, endIndex);
    }

    public void execute(PAESConfigs configs) throws Exception {
        int runsNumber = configs.getNumberOfRuns();
        int maxEvaluations = configs.getMaxEvaluation();
        int archiveSize = configs.getArchiveSize();
        int biSections = 5;
        double crossoverProbability = configs.getCrossoverProbability();
        double mutationProbability = configs.getMutationProbability();
        int numberObjectives = configs.getOplaConfigs().getNumberOfObjectives();
        String context = "OPLA";

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
            experiment = mp.save(plaName, "PAES", configs.getDescription(), OPLAThreadScope.hash.get());
            ExperimentConfs conf = new ExperimentConfs(experiment.getId(), "PAES", configs);
            mp.save(conf);


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
            crossover = CrossoverFactory.getCrossoverOperator("PLACrossover", parameters, configs);

            parameters = new HashMap<>();
            parameters.put("probability", mutationProbability);
            mutation = MutationFactory.getMutationOperator("PLAFeatureMutation", parameters, configs);
            parameters = null;
            selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters);

            algorithm.addOperator("crossover", crossover);
            algorithm.addOperator("mutation", mutation);
            algorithm.addOperator("selection", selection);

            if (configs.isLog())
                logInformations(configs, context, pla);

            List<String> selectedObjectiveFunctions = configs.getOplaConfigs().getSelectedObjectiveFunctions();
            mp.saveObjectivesNames(configs.getOplaConfigs().getSelectedObjectiveFunctions(), experiment.getId());

            result.setPlaName(plaName);

            long[] time = new long[runsNumber];

            for (int runs = 0; runs < runsNumber; runs++) {
                Execution execution = new Execution(experiment);
                CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), execution.getId());

                long initTime = System.currentTimeMillis();
                SolutionSet resultFront = algorithm.execute();
                long estimatedTime = System.currentTimeMillis() - initTime;
                time[runs] = estimatedTime;

                resultFront = problem.removeDominadas(resultFront);
                resultFront = problem.removeRepetidas(resultFront);

                execution.setTime(estimatedTime);

                List<Info> Info = result.getInfos(resultFront.getSolutionSet(), execution, experiment);
                Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(Info, resultFront.getSolutionSet(), execution, experiment, selectedObjectiveFunctions);

                new OPLASolutionSet(resultFront).saveVariablesToFile("VAR_" + runs + "_", Info, configs.getLogger(), true);

                execution.setInfos(Info);
                execution.setAllMetrics(allMetrics);

                mp.save(execution);
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
            mp.save(infos);
            Map<String, List<ObjectiveFunctionDomain>> allMetrics = result.getMetrics(funResults, allRuns.getSolutionSet(), null, experiment,
                    selectedObjectiveFunctions);
            mp.save(allMetrics);

            CommonOPLAFeatMut.setDirToSaveOutput(experiment.getId(), null);
            mp.saveEuclideanDistance(c.calculate(experiment.getId(), configs.getOplaConfigs().getNumberOfObjectives()), experiment.getId());
            saveHypervolume(experiment.getId(), null, allRuns, plaName);
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


    private void saveHypervolume(String experimentID, String executionID, SolutionSet allSolutions, String plaName) {
        String dir;
        if (executionID != null)
            dir = OPLAConfigThreadScopeReader.getDirectoryToExportModels() + System.getProperty("file.separator") + experimentID + System.getProperty("file.separator") + executionID + "/Hypervolume/";
        else
            dir = OPLAConfigThreadScopeReader.getDirectoryToExportModels() + System.getProperty("file.separator") + experimentID + "/Hypervolume/";

        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();

        new OPLASolutionSet(allSolutions).printObjectivesToFile(dir + "/hypervolume.txt");
    }


}
