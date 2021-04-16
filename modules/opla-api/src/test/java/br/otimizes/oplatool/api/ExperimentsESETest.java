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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExperimentsESETest {

    public static void main(String... args) throws Exception {
        String currentDir = "oplatool-config2-agm1";
//        FileConstants.USER_HOME = "/home/wmfsystem/Documents/experimentos-20210409T113119Z-001/" + currentDir;
        System.setProperty("user.home", "/home/wmfsystem/Documents/experimentos-20210409T113119Z-001/" + currentDir);
//        FileConstants.CONFIG_PATH = FileConstants.USER_HOME;
        String dir = FileConstants.USER_HOME + "/output";

        ApplicationYamlConfig applicationYamlConfig = new ApplicationYamlConfig();
        applicationYamlConfig.setDirectoryToExportModels(dir);
        OPLAThreadScope.config.set(applicationYamlConfig);
        File dirOutput = new File(dir);
        File dirUser = Arrays.stream(dirOutput.listFiles()).filter(file -> file.isDirectory()).findFirst().orElse(null);
        File dirSolutions = Arrays.stream(dirUser.listFiles()).filter(file -> file.isDirectory()).findFirst().orElse(null);
//        String fitnessStr = readFile(dirSolutions + "/1/fitness/fitness.txt");
//        List<String> fitness = Arrays.stream(fitnessStr.split("\n")).collect(Collectors.toList());

        SolutionSet allSolutions = new SolutionSet();
        NSGAIIConfigs configs = getNsgaiiConfigs();
        OPLA opla = new OPLA(Arrays.stream(dirUser.listFiles()).filter(file -> file.isFile()).findFirst().orElse(null).getPath(), configs);

        for (File file : Arrays.stream(dirSolutions.listFiles()).sorted().collect(Collectors.toList())) {
            if (file.isFile() && file.getName().contains(".smty") && !file.getName().contains("ALL")) {
                try {
                    Architecture architecture = new ArchitectureBuilderSMarty().create(file.getPath());
                    ObjectiveFunctions[] values = new ObjectiveFunctions[]{ObjectiveFunctions.ACLASS, ObjectiveFunctions.FM, ObjectiveFunctions.COE};
                    SolutionSet solutionSet = new SolutionSet();
                    Solution solution = new Solution(values.length);
                    String id = file.getName().replace("VAR_", "");
                    id = id.substring(0, id.indexOf("_"));
                    solution.setExecutionId(id);
                    solution.setProblem(opla);
                    solution.setDecisionVariables(new Variable[]{architecture});
                    solutionSet.setCapacity(1);
                    solutionSet.add(solution);

                    opla.setSelectedMetrics(new ArrayList<>());
                    solution.setNumberOfObjectives(values.length);
                    for (int i = 0; i < values.length; i++) {
                        opla.getSelectedMetrics().add(values[i].toString());
                    }

                    opla.evaluate(solution);
                    String obj1 = String.valueOf(solution.getObjective(0));
                    String obj2 = String.valueOf(solution.getObjective(1));
                    String obj3 = String.valueOf(solution.getObjective(2));

                    SolutionSet solutionSet1 = new SolutionSet(1);
                    solutionSet1.add(solution);
                    allSolutions = allSolutions.union(solutionSet1);
                    System.out.println("FILE: " + file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        removeNonDominatedAndSaveLog(dirSolutions, allSolutions, opla);
        System.out.println("aqui");
    }

    static String readFile(String path)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    private static void removeNonDominatedAndSaveLog(File dirSolutions, SolutionSet solutionSet, OPLA opla) throws IOException {
        ArrayList<List<Solution>> lists = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            lists.add(new ArrayList<>());
        }
        for (Solution solution : solutionSet.getSolutionSet()) {
            int i = Integer.parseInt(solution.getExecutionId());
            lists.get(i).add(solution);
        }

        File file = new File(dirSolutions.getPath() + "/non-dominated-solutions");
        if (file.exists()) file.delete();
        file.createNewFile();
        SolutionSet allSolutions = new SolutionSet(1);
        for (List<Solution> fromLists : lists) {
            if (fromLists.isEmpty()) continue;
            SolutionSet solutionSet1 = new SolutionSet(fromLists.size());
            allSolutions = allSolutions.union(solutionSet1);
        }

        allSolutions = opla.removeDominadas(solutionSet);
        allSolutions = opla.removeRepetidas(solutionSet);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < allSolutions.size(); i++) {
            Solution solution = allSolutions.get(i);
            Architecture architecture = (Architecture) solution.getDecisionVariables()[0];
            architecture.save(architecture, dirSolutions.getPath().substring(dirSolutions.getPath()
                    .indexOf("output") + 6) + "/VAR_ALL_" + Arrays.stream(solution.getObjectives()).mapToObj(String::valueOf)
                    .collect(Collectors.joining("-")) + "_", i + "");
            stringBuilder.append(architecture.getName() + "\n");
            System.out.println("A solução " + architecture.getName() + " é não dominada");
        }
        try {
            Files.write(file.toPath(), stringBuilder.toString().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
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
        configs.setDescription("agm1");
        configs.disableCrossover();
        configs.setMutationProbability(0.8);
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
        oplaConfigs.setSelectedObjectiveFunctions(Arrays.asList("ACLASS", "FM", "COE"));
        configs.setOplaConfigs(oplaConfigs);
        return configs;
    }
}
