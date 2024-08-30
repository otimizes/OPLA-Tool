package br.otimizes.oplatool.core.jmetal4.learning;

import br.otimizes.isearchai.learning.ml.basis.Moment;
import br.otimizes.isearchai.learning.ml.clustering.ClusteringAlgorithm;
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
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationYamlConfig;
import br.otimizes.oplatool.domain.config.FileConstants;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import weka.classifiers.Evaluation;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

@Category(MachineLearningTests.class)
public class NSGATestII {

    @Test
    public void testMMWithMLP() throws Exception {
        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels("");
        applicationYamlConfig.setDirectoryToSaveModels("");
        applicationYamlConfig.setPathToTemplateModelsDirectory("");
        OPLAThreadScope.setConfig(applicationYamlConfig);
        
        String agm = Thread.currentThread().getContextClassLoader().getResource("PLASMarty").getFile();
        String xmiFilePath = agm + FileConstants.FILE_SEPARATOR + "agm1.smty";
        NSGAIIConfigs configs = getNsgaiiConfigs();
        NSGAII algorithm = getAlgorithm(xmiFilePath, configs);
        SolutionSet solutionSet = algorithm.execute();

        for (Solution solution : solutionSet.getSolutionSet()) {
            System.out.println(Arrays.toString(solution.getObjectives()));
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
        configs.setInteractive(false);
        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
        configs.setClusteringMoment(Moment.POSTERIORI);
        configs.setMaxEvaluations(200);
        configs.setArchitectureBuilder(ArchitectureBuilders.SMARTY);
        configs.setDescription("agm1");
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
