package br.otimizes.oplatool.core.jmetal4.learning;

import br.otimizes.oplatool.architecture.builders.ArchitectureBuilders;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.experiments.OPLAConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIConfigs;
import br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII.NSGAII;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.Crossover;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverFactory;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.Mutation;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.MutationFactory;
import br.otimizes.oplatool.core.jmetal4.operators.selection.Selection;
import br.otimizes.oplatool.core.jmetal4.operators.selection.SelectionFactory;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.learning.ClusteringAlgorithm;
import br.otimizes.oplatool.core.learning.Moment;
import br.otimizes.oplatool.core.learning.SubjectiveAnalyzeAlgorithm;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationYamlConfig;
import br.otimizes.oplatool.domain.config.FileConstants;
import org.junit.Test;
import weka.classifiers.Evaluation;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class SubjectiveAnalyzeAlgorithmTest {

    @Test
    public void testMMWithMLP() throws Exception {
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);

        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        String xmiFilePath = agm + FileConstants.FILE_SEPARATOR + "MMAtual.smty";
        NSGAIIConfigs configs = getNsgaiiConfigs();
        NSGAII algorithm;
        configs.setInteractiveFunction(solutionSet -> {
            int i = 0;
            for (Solution solution : solutionSet.getSolutionSet()) {
                System.out.println("Print " + i);
                Architecture architecture = (Architecture) solution.getDecisionVariables()[0];
                Set<Class> allClasses = architecture.getAllClasses();
                Class userClass = allClasses.stream().filter(clazz -> "User".equals(clazz.getName())).min(Comparator.comparing(Element::getName)).orElse(null);
                Class userMgrClass = allClasses.stream().filter(clazz -> "UserMgr".equals(clazz.getName())).min(Comparator.comparing(Element::getName)).orElse(null);
                if (userClass != null && (userClass.getAllMethods().size() != 2 || userClass.getAllAttributes().size() != 3)) {
                    System.out.println(":: User freezed by DM :" + userClass.getAllMethods().size() + " - " + userClass.getAllAttributes().size());
                    userClass.setComments("freeze");
                }
                if (userMgrClass != null && (userMgrClass.getAllMethods().size() != 1 || userMgrClass.getAllAttributes().size() != 1)) {
                    System.out.println(":: UserMgr freezed by DM :" + userMgrClass.getAllMethods().size() + " - " + userMgrClass.getAllAttributes().size());
                    userMgrClass.setComments("freeze");
                }
                i++;
            }
            return solutionSet;
        });
        algorithm = getAlgorithm(xmiFilePath, configs);
        SolutionSet solutionSet = algorithm.execute();
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            Solution solution = solutionSet.getSolutionSet().get(i);
            Architecture architecture = (Architecture) solution.getDecisionVariables()[0];
            if (!architecture.getFreezedElements().isEmpty()) {
                System.out.println(":: Solution " + i + " contains " + architecture.getFreezedElements().size() + " freezed solutions.");
                for (Element freezedElement : architecture.getFreezedElements()) {
                    System.out.println(":::: " + freezedElement.getName() + " is freezed on resulting solution " + i);
                }
            }
        }

        applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = algorithm.getSubjectiveAnalyzeAlgorithm();
        List<Element> truePositive = subjectiveAnalyzeAlgorithm.getNotFreezedElements().stream()
                .filter(element -> {
                    if (!(element instanceof Class)) return false;
                    Class clazz = (Class) element;
                    return (clazz.getName().equals("User") && (clazz.getAllMethods().size() != 2 || clazz.getAllAttributes().size() != 3))
                            || (clazz.getName().equals("UserMgr") && (clazz.getAllMethods().size() != 1 || clazz.getAllAttributes().size() != 1));
                })
                .collect(Collectors.toList());

        System.out.println("::True Positive:: " + truePositive.size() + " items.");
        System.out.println(":::: " + truePositive.stream().map(Element::getName).collect(Collectors.joining(",")));

        List<Element> falsePositive = subjectiveAnalyzeAlgorithm.getNotFreezedElements().stream()
                .filter(element -> {
                    if (!(element instanceof Class)) return false;
                    Class clazz = (Class) element;
                    return (clazz.getName().equals("User") && (clazz.getAllMethods().size() == 2 || clazz.getAllAttributes().size() == 3))
                            || (clazz.getName().equals("UserMgr") && (clazz.getAllMethods().size() == 1 || clazz.getAllAttributes().size() == 1));
                })
                .collect(Collectors.toList());

        System.out.println("::False Positive:: " + falsePositive.size() + " items.");
        System.out.println(":::: " + falsePositive.stream().map(Element::getName).collect(Collectors.joining(",")));

        Evaluation architectureEval = subjectiveAnalyzeAlgorithm.getArchitectureEvaluation();
        if (architectureEval != null) {
            System.out.println(architectureEval.toClassDetailsString("Test::testMMWithoutMLP()->getArchitectureEvaluation()"));
            assertTrue(architectureEval.pctCorrect() > 90);
        }
    }

    private NSGAII getAlgorithm(String xmiFilePath, NSGAIIConfigs configs) throws Exception {
        OPLA opla = new OPLA(xmiFilePath, configs);

        NSGAII algorithm = new NSGAII(opla);
        algorithm.setInputParameter("populationSize", configs.getPopulationSize());
        algorithm.setInputParameter("maxEvaluations", configs.getMaxEvaluation());
        algorithm.setInputParameter("interactiveFunction", configs.getInteractiveFunction());
        algorithm.setInputParameter("maxInteractions", configs.getMaxInteractions());
        algorithm.setInputParameter("firstInteraction", configs.getFirstInteraction());
        algorithm.setInputParameter("intervalInteraction", configs.getIntervalInteraction());
        algorithm.setInputParameter("interactive", configs.getInteractive());
        algorithm.setInputParameter("clusteringMoment", configs.getClusteringMoment());
        algorithm.setInputParameter("clusteringAlgorithm", configs.getClusteringAlgorithm());

        HashMap<String, Object> parametersCrossover = new HashMap<>();
        parametersCrossover.put("probability", configs.getCrossoverProbability());
        parametersCrossover.put("numberOfObjectives", configs.getOplaConfigs().getNumberOfObjectives());
        Crossover crossover = CrossoverFactory.getCrossoverOperator("PLACrossoverOperator", parametersCrossover, configs);
        algorithm.addOperator("crossover", crossover);

        HashMap<String, Object> parametersMutation = new HashMap<>();
        parametersMutation.put("probability", configs.getMutationProbability());
        Mutation mutation = MutationFactory.getMutationOperator("PLAMutationOperator", parametersMutation, configs);
        algorithm.addOperator("mutation", mutation);

        Selection selection = SelectionFactory.getSelectionOperator("BinaryTournament", null);
        algorithm.addOperator("selection", selection);
        return algorithm;
    }

    private static NSGAIIConfigs getNsgaiiConfigs() {
        NSGAIIConfigs configs = new NSGAIIConfigs();
        configs.setPopulationSize(10);
        configs.setInteractive(true);
        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
        configs.setClusteringMoment(Moment.POSTERIORI);
        configs.setMaxEvaluations(200);
        configs.setArchitectureBuilder(ArchitectureBuilders.SMARTY);
        configs.setDescription("mm");
        configs.setFirstInteraction(3);
        configs.setMaxInteractions(3);
        configs.setIntervalInteraction(3);
        configs.disableCrossover();
        configs.setMutationProbability(0.9);
        configs.setArchitectureBuilder(ArchitectureBuilders.SMARTY);
        configs.setMutationOperators(Arrays.asList(
                "FEATURE_DRIVEN_OPERATOR",
                "MOVE_METHOD_MUTATION",
                "MOVE_ATTRIBUTE_MUTATION",
                "MOVE_OPERATION_MUTATION",
                "ADD_CLASS_MUTATION",
                "ADD_MANAGER_CLASS_MUTATION"
        ));
        OPLAConfigs oplaConfigs = new OPLAConfigs();
        oplaConfigs.setSelectedObjectiveFunctions(Arrays.asList("COE", "FM", "ACLASS"));
        configs.setOplaConfigs(oplaConfigs);
        return configs;
    }
}
