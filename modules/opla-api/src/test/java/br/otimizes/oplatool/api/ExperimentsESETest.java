package br.otimizes.oplatool.api;

import br.otimizes.oplatool.architecture.builders.ArchitectureBuilderSMarty;
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExperimentsESETest {

    public static void main(String... args) throws Exception {
        String currentDir = "oplatool-config4-mm1";
        System.setProperty("user.home", "/home/wmfsystem/Documents/experimentos-20210409T113119Z-001/" + currentDir);
        String dir = FileConstants.USER_HOME + "/output";

        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels(dir);
        OPLAThreadScope.config.set(applicationYamlConfig);
        File dirOutput = new File(dir);
        File dirUser = Arrays.stream(dirOutput.listFiles()).filter(file -> file.isDirectory()).findFirst().orElse(null);
        File dirSolutions = Arrays.stream(dirUser.listFiles()).filter(file -> file.isDirectory()).findFirst().orElse(null);
        String fitnessStr = readFile(dirSolutions + "/1/fitness/fitness.txt");
        List<String> fitness = Arrays.stream(fitnessStr.split("\n")).collect(Collectors.toList());

        SolutionSet allSolutions = new SolutionSet();
        NSGAIIConfigs configs = getNsgaiiConfigs();
        OPLA opla = new OPLA(Arrays.stream(dirUser.listFiles()).filter(file -> file.isFile()).findFirst().orElse(null).getPath(), configs);
        opla.setSelectedMetrics(new ArrayList<>());
        List<String> selectedMetrics = opla.getSelectedMetrics();

        for (int i = 0; i < configs.getObjectiveFuncions().size(); i++) {
            opla.getSelectedMetrics().add(configs.getObjectiveFuncions().get(i));
        }

        List<File> collect = Arrays.stream(dirSolutions.listFiles()).sorted().filter(file -> {
            return file.isFile() && file.getName().contains(".smty") && !file.getName().contains("ALL");
        }).collect(Collectors.toList());
        SolutionSet solutionSet = new SolutionSet(collect.size());
        for (File file : collect) {
            OPLA oplaArchitecture = new OPLA(file.getPath());
            oplaArchitecture.setSelectedMetrics(opla.getSelectedMetrics());
            Solution solution = new Solution(opla);
            String id = file.getName().replace("VAR_", "");
            id = id.substring(id.indexOf("_"));
            solution.setExecutionId(id);
            solution.setNumberOfObjectives(configs.getObjectiveFuncions().size());
            solution.setProblem(oplaArchitecture);
            solution.setDecisionVariables(new Variable[]{oplaArchitecture.architecture_});
            solutionSet.add(solution);

        }
        for (Solution solution: solutionSet.getSolutionSet()) {
            try {
                opla.evaluate(solution);
                String obj1 = String.valueOf(solution.getObjective(0));
                String obj2 = String.valueOf(solution.getObjective(1));
                String obj3 = String.valueOf(solution.getObjective(2));

                boolean contains = fitness.stream().filter(f ->
                        f.contains(obj1) && f.contains(obj2) && f.contains(obj3)).count() > 0;

                if (contains) {
                    System.out.println("tem");
                }

                SolutionSet solutionSet1 = new SolutionSet(1);
                solutionSet1.add(solution);
                allSolutions = allSolutions.union(solutionSet1);
                System.out.println("FILE: " + solution.getProblem().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        allSolutions = oplaArchitecture.removeDominadas(allSolutions);
//        allSolutions = oplaArchitecture.removeRepetidas(allSolutions);
        System.out.println("aqui");
    }

    static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    private static void removeNonDominatedAndSaveLog(File dirSolutions, SolutionSet allSolutions, OPLA opla) throws IOException {
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
        configs.setPopulationSize(100);
        configs.setInteractive(true);
        configs.setClusteringAlgorithm(ClusteringAlgorithm.KMEANS);
        configs.setClusteringMoment(Moment.POSTERIORI);
        configs.setMaxEvaluations(30000);
        configs.setArchitectureBuilder(ArchitectureBuilders.SMARTY);
        configs.setDescription("mm");
        configs.disableCrossover();
        configs.setMutationProbability(0.9);
        configs.setArchitectureBuilder(ArchitectureBuilders.SMARTY);
        configs.setCrossoverProbability(0.4);
        configs.setCrossoverOperators(Arrays.asList("PLA_FEATURE_DRIVEN_CROSSOVER", "PLA_COMPLEMENTARY_CROSSOVER"));
        configs.setMutationOperators(Arrays.asList(
                "FEATURE_DRIVEN_OPERATOR",
                "MOVE_METHOD_MUTATION",
                "MOVE_ATTRIBUTE_MUTATION",
                "MOVE_OPERATION_MUTATION",
                "ADD_CLASS_MUTATION",
                "ADD_MANAGER_CLASS_MUTATION"
        ));
        OPLAConfigs oplaConfigs = new OPLAConfigs();
        oplaConfigs.setSelectedObjectiveFunctions(Arrays.asList("ACLASS", "COE", "FM"));
        configs.setOplaConfigs(oplaConfigs);
        return configs;
    }
}
