package br.otimizes.oplatool.api;

import br.otimizes.oplatool.architecture.builders.ArchitectureBuilders;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.common.Variable;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.experiments.OPLAConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIConfigs;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctions;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.learning.ClusteringAlgorithm;
import br.otimizes.oplatool.core.learning.Moment;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationYamlConfig;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ExperimentsESETest {

    public static void main(String... args) throws Exception {
        String currentDir = "oplatool-config1-agm1";
        FileConstants.USER_HOME = "/home/wmfsystem/Documents/experimentos-20210409T113119Z-001/" + currentDir;
        FileConstants.CONFIG_PATH = FileConstants.USER_HOME;
        String dir = FileConstants.USER_HOME + "/output";

        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels(dir);
        OPLAThreadScope.config.set(applicationYamlConfig);
        File dirOutput = new File(dir);
        File dirUser = Arrays.stream(dirOutput.listFiles()).filter(file -> file.isDirectory()).findFirst().orElse(null);
        File dirSolutions = Arrays.stream(dirUser.listFiles()).filter(file -> file.isDirectory()).findFirst().orElse(null);

        SolutionSet allSolutions = new SolutionSet();
        NSGAIIConfigs configs = getNsgaiiConfigs();
        OPLA opla = new OPLA(Arrays.stream(dirUser.listFiles()).filter(file -> file.isFile()).findFirst().orElse(null).getPath(), configs);

        for (File file : dirSolutions.listFiles()) {
            if (file.isFile() && file.getName().contains(".smty") && !file.getName().contains("ALL")) {
                try {
                    OPLA solutionOPLA = new OPLA(file.getPath(), configs);
                    ObjectiveFunctions[] values = new ObjectiveFunctions[]{ObjectiveFunctions.COE, ObjectiveFunctions.FM, ObjectiveFunctions.CLASS};
                    SolutionSet solutionSet = new SolutionSet();
                    Solution solution = new Solution(values.length);
                    solution.setProblem(opla);
                    solution.setDecisionVariables(new Variable[]{solutionOPLA.getArchitecture_()});
                    solutionSet.setCapacity(1);
                    solutionSet.add(solution);

                    opla.setSelectedMetrics(new ArrayList<>());
                    solution.setNumberOfObjectives(values.length);
                    for (int i = 0; i < values.length; i++) {
                        opla.getSelectedMetrics().add(values[i].toString());
                    }

                    opla.evaluate(solution);
                    SolutionSet solutionSet1 = new SolutionSet(1);
                    solutionSet1.add(solution);
                    allSolutions = allSolutions.union(solutionSet1);
                    System.out.println("FILE: " + file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        allSolutions = opla.removeDominadas(allSolutions);
        allSolutions = opla.removeRepetidas(allSolutions);
        File file = new File(dirSolutions.getPath() + "/non-dominated-solutions");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < allSolutions.getSolutionSet().size(); i++) {
            Solution solution = allSolutions.get(i);
            Architecture architecture = (Architecture) solution.getDecisionVariables()[0];
            architecture.save(architecture, dirSolutions.getPath().substring(dirSolutions.getPath()
                    .indexOf("output") + 6) + "/VAR_ALL_" + Arrays.stream(solution.getObjectives()).mapToObj(String::valueOf)
                    .collect(Collectors.joining("-")) + "_", i + "");
            stringBuilder.append(architecture.getName() + "\n");
            System.out.println("A solução " + architecture.getName() + " é não dominada");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(stringBuilder.toString());
        } finally {
            if (writer != null) writer.close();
        }
        System.out.println("here");
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
