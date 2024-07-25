//package br.otimizes.oplatool.core.jmetal4.learning;
//
//import br.otimizes.oplatool.architecture.builders.ArchitectureBuilders;
//import br.otimizes.oplatool.common.exceptions.JMException;
//import br.otimizes.oplatool.core.jmetal4.core.Solution;
//import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
//import br.otimizes.oplatool.core.jmetal4.experiments.OPLAConfigs;
//import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIConfigs;
//import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveFunction;
//import br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII.NSGAII;
//import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
//import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverFactory;
//import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
//import br.otimizes.oplatool.core.jmetal4.operators.mutation.MutationFactory;
//import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
//import br.otimizes.oplatool.core.jmetal4.operators.selection.SelectionFactory;
//import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
//import br.otimizes.isearchai.learning.Clustering;
//import br.otimizes.isearchai.learning.ClusteringAlgorithm;
//import br.otimizes.isearchai.learning.Moment;
//import br.otimizes.oplatool.domain.config.FileConstants;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.Arrays;
//import java.util.HashMap;
//import static org.junit.Assert.*;
//
//public class SubjectiveAnalyzeRankingTest {
//
//    private String resourcesPath;
//    private int interaction = 0;
//
//    @Before
//    public void init() {
//        resourcesPath = getResourcesPath();
//    }
//
//    @Test
//    public void interactiveFunction1Test() throws Exception {
//        String path = resourcesPath + FileConstants.FILE_SEPARATOR + "subjectiveAnalyzeRankingFiles/interactiveFunction1Test.csv";
//        boolean existsInteractiveFunction1Test = new File(path).exists();
//        if (!existsInteractiveFunction1Test) {
//            createSubjectiveAnalyzeRankingTestFile(path);
//            String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
//            String xmiFilePath = agm + FileConstants.FILE_SEPARATOR + "agm1.smty";
//            NSGAIIConfigs configs = getNsgaiiConfigs();
//            configs.setInteractiveFunction(getInteractiveFunction1(path));
//            NSGAII algorithm = getAlgorithm(xmiFilePath, configs);
//            SolutionSet solutionSet = algorithm.execute();
//            logObjectivesFromSolutions(solutionSet, path);
//        }
//        assertTrue(existsInteractiveFunction1Test);
//    }
//
//    private InteractiveFunction getInteractiveFunction1(String path) {
//        return solutionSet -> {
//            Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
//            clustering.setNumClusters(solutionSet.getSolutionSet().get(0).numberOfObjectives() + 1);
//            clustering.run();
//            for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
//                Solution solution = solutionSet.getSolutionSet().get(i);
//                if (Math.random() * 10 < 5) {
//                    if (solution.getObjective(0) < 3) {
//                        solution.setEvaluation(2);
//                    } else if (solution.getObjective(0) >= 3 && solution.getObjective(0) <= 5) {
//                        solution.setEvaluation(3);
//                    } else {
//                        solution.setEvaluation(4);
//                    }
//                }
//            }
//            logObjectivesFromSolutions(solutionSet, path);
//            return solutionSet;
//        };
//    }
//
//    @Test
//    public void interactiveFunction2Test() throws Exception {
//        String path = resourcesPath + FileConstants.FILE_SEPARATOR + "subjectiveAnalyzeRankingFiles/interactiveFunction2Test.csv";
//        boolean existsInteractiveFunction2Test = new File(path).exists();
//        if (!existsInteractiveFunction2Test) {
//            createSubjectiveAnalyzeRankingTestFile(path);
//            String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
//            String xmiFilePath = agm + FileConstants.FILE_SEPARATOR + "agm1.smty";
//            NSGAIIConfigs configs = getNsgaiiConfigs();
//            configs.setInteractiveFunction(getInteractiveFunction2(path));
//            NSGAII algorithm = getAlgorithm(xmiFilePath, configs);
//            SolutionSet solutionSet = algorithm.execute();
//            logObjectivesFromSolutions(solutionSet, path);
//        }
//        assertTrue(existsInteractiveFunction2Test);
//    }
//
//    private InteractiveFunction getInteractiveFunction2(String path) {
//        return solutionSet -> {
//            Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
//            clustering.setNumClusters(solutionSet.getSolutionSet().get(0).numberOfObjectives() + 1);
//            clustering.run();
//            for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
//                Solution solution = solutionSet.getSolutionSet().get(i);
//                if (Math.random() * 10 < 5) {
//                    if (solution.getObjective(0) < 3) {
//                        solution.setEvaluation(4);
//                    } else if (solution.getObjective(0) >= 3 && solution.getObjective(0) <= 5) {
//                        solution.setEvaluation(3);
//                    } else {
//                        solution.setEvaluation(2);
//                    }
//                }
//            }
//            logObjectivesFromSolutions(solutionSet, path);
//            return solutionSet;
//        };
//    }
//
//    private void createSubjectiveAnalyzeRankingTestFile(String path) {
//        try {
//            FileWriter fileWriter = new FileWriter(path, false);
//            fileWriter.write("INTERACTION, ID, COE, FM, ACLASS, CLUSTER, ED, SCORE\n");
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void logObjectivesFromSolutions(SolutionSet solutionSet, String path) {
//        interaction++;
//        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
//            Solution solution = solutionSet.getSolutionSet().get(i);
//            double[] objectives = solution.getObjectives();
//            String log = interaction + ", " + i + ", " + objectives[0] + ", " + objectives[1] + ", " + objectives[2] + ", "
//                    + solution.getClusterId() + ", "
//                    + getEd(solution) + ", "
//                    + solution.getEvaluation() + "\n";
//            appendToFile(path, log);
//        }
//    }
//
//    private double getEd(Solution solution) {
//        return Math.sqrt(Math.pow(solution.getObjective(0), 2)
//                + Math.pow(solution.getObjective(1), 2)
//                + Math.pow(solution.getObjective(2), 2));
//    }
//
//    private void appendToFile(String path, String log) {
//        try {
//            System.out.println(log);
//            Files.write(Paths.get(path), log.getBytes(), StandardOpenOption.APPEND);
//        } catch (IOException e) {
//            //exception handling left as an exercise for the reader
//        }
//    }
//
//    private String getResourcesPath() {
//        String path = "src/test/resources";
//        File file = new File(path);
//        return file.getAbsolutePath();
//    }
//
//    private NSGAII getAlgorithm(String xmiFilePath, NSGAIIConfigs configs) throws Exception {
//        NSGAII algorithm = getNsgaii(xmiFilePath, configs);
//
//        Crossover crossover = getCrossover(configs);
//        algorithm.addOperator("crossover", crossover);
//
//        Mutation mutation = getMutation(configs);
//        algorithm.addOperator("mutation", mutation);
//
//        Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament", null);
//        algorithm.addOperator("selection", selection);
//        return algorithm;
//    }
//
//    private NSGAII getNsgaii(String xmiFilePath, NSGAIIConfigs configs) throws Exception {
//        OPLA opla = new OPLA(xmiFilePath, configs);
//
//        NSGAII algorithm = new NSGAII(opla);
//        algorithm.setInputParameter("populationSize", configs.getPopulationSize());
//        algorithm.setInputParameter("maxEvaluations", configs.getMaxEvaluation());
//        algorithm.setInputParameter("interactiveFunction", configs.getInteractiveFunction());
//        algorithm.setInputParameter("maxInteractions", configs.getMaxInteractions());
//        algorithm.setInputParameter("firstInteraction", configs.getFirstInteraction());
//        algorithm.setInputParameter("intervalInteraction", configs.getIntervalInteraction());
//        algorithm.setInputParameter("interactive", configs.getInteractive());
//        algorithm.setInputParameter("clusteringMoment", configs.getClusteringMoment());
//        algorithm.setInputParameter("clusteringAlgorithm", configs.getClusteringAlgorithm());
//        return algorithm;
//    }
//
//    private Crossover getCrossover(NSGAIIConfigs configs) throws JMException {
//        HashMap<String, Object> parametersCrossover = new HashMap<>();
//        parametersCrossover.put("probability", configs.getCrossoverProbability());
//        parametersCrossover.put("numberOfObjectives", configs.getOplaConfigs().getNumberOfObjectives());
//        return CrossoverFactory.getCrossoverOperator("PLACrossoverOperator", parametersCrossover, configs);
//    }
//
//    private Mutation getMutation(NSGAIIConfigs configs) throws JMException {
//        HashMap<String, Object> parametersMutation = new HashMap<>();
//        parametersMutation.put("probability", configs.getMutationProbability());
//        return MutationFactory.getMutationOperator("PLAMutationOperator", parametersMutation, configs);
//    }
//
//    private static NSGAIIConfigs getNsgaiiConfigs() {
//        NSGAIIConfigs configs = new NSGAIIConfigs();
//        configs.setPopulationSize(200);
//        configs.setInteractive(true);
//        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
//        configs.setClusteringMoment(Moment.INTERACTIVE);
//        configs.setMaxEvaluations(3000);
//        configs.setArchitectureBuilder(ArchitectureBuilders.SMARTY);
//        configs.setDescription("agm");
//        configs.setFirstInteraction(3);
//        configs.setMaxInteractions(3);
//        configs.setIntervalInteraction(3);
//        configs.disableCrossover();
//        configs.setMutationProbability(0.9);
//        configs.setArchitectureBuilder(ArchitectureBuilders.SMARTY);
//        configs.setMutationOperators(Arrays.asList(
//                "FEATURE_DRIVEN_OPERATOR",
//                "MOVE_METHOD_MUTATION",
//                "MOVE_ATTRIBUTE_MUTATION",
//                "MOVE_OPERATION_MUTATION",
//                "ADD_CLASS_MUTATION",
//                "ADD_MANAGER_CLASS_MUTATION",
//                "FEATURE_DRIVEN_OPERATOR_FOR_CLASS"
//        ));
//        OPLAConfigs oplaConfigs = new OPLAConfigs();
//        oplaConfigs.setSelectedObjectiveFunctions(Arrays.asList("COE", "FM", "ACLASS"));
//        configs.setOplaConfigs(oplaConfigs);
//        return configs;
//    }
//}
