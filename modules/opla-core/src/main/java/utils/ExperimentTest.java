package utils;

import arquitetura.representation.Architecture;
import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.encodings.solutionType.ArchitectureSolutionType;
import jmetal4.problems.OPLA;
import learning.Clustering;
import learning.ClusteringAlgorithm;
import liquibase.util.csv.CSVReader;

import java.io.*;
import java.util.*;

public class ExperimentTest {


    public static SolutionSet getSolutionSetFromObjectiveList(List<Objective> objectives, String pla) {
        Long id = Objects.equals(pla, "agm") ? 7127396432L : 7837731112L;
        SolutionSet solutionSet = new SolutionSet();
        Architecture architecture = new Architecture("Teste");
        objectives.forEach(objective -> {
            if (objective.getExecution() != null && Objects.equals(objective.getExecution().getId(), id)) {
                Solution solution = new Solution();
                solution.createObjective(3);
                String[] split = objective.getObjectives().split("\\|");
                solution.setObjective(0, Double.parseDouble(split[0]));
                solution.setObjective(1, Double.parseDouble(split[1]));
                solution.setObjective(2, Double.parseDouble(split[2]));

                try {
                    solution.setType(new ArchitectureSolutionType(new OPLA()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                solution.setProblem(new OPLA());
                solution.getOPLAProblem().setArchitecture_(architecture);
                solution.getOPLAProblem().setSelectedMetrics(Arrays.asList("featureDriven", "aclass", "coe"));

                solution.setSolutionName(objective.getSolutionName());
                solution.setExecutionId(objective.getExecution() != null ? objective.getExecution().getId() : 0);
                solution.setNumberOfObjectives(3);
                solutionSet.getSolutionSet().add(solution);
            }
        });
        return solutionSet;
    }

    public static List<Objective> getObjectivesFromFile(String filename) throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        List<Objective> objectives = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            objectives.add(new Objective(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]));
        }
        return objectives;
    }

    public static String getNormalizedNewObjectivesOfClusteredSolutions(SolutionSet run) {
        List<List<Double>> result = MathUtils.normalize(run);
        Long lastExec = null;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            builder.append(result.get(i).toString().replace("[", "").replace("]", "").replaceAll(",", "") + "\n");
            if (!run.getSolutionSet().get(i).getExecutionId().equals(lastExec)) {
                lastExec = run.getSolutionSet().get(i).getExecutionId();
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public static List<List<Double>> getListXYZ(SolutionSet result) {
        List<List<Double>> listXYZ = new ArrayList<>();
        listXYZ.add(new ArrayList<>());
        listXYZ.add(new ArrayList<>());
        listXYZ.add(new ArrayList<>());
        result.getSolutionSet().forEach(r -> {
            listXYZ.get(0).add(r.getObjective(0));
            listXYZ.get(1).add(r.getObjective(1));
            listXYZ.get(2).add(r.getObjective(2));
        });
        return listXYZ;
    }

    public static List<Double> calculateHypervolume(SolutionSet run) throws IOException {
        String newObjectivesOfClusteredSolutions = getNormalizedNewObjectivesOfClusteredSolutions(run);
        File tempFile = File.createTempFile("opla-", "-test");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
        bw.write(newObjectivesOfClusteredSolutions);
        bw.close();

        List<Double> hypervolume = hypervolume("1.01 1.01 1.01", tempFile.toString());

        tempFile.deleteOnExit();
        return hypervolume;
    }

    public static List<Double> hypervolume(String referencePoint, String pathToFile) throws IOException {
        String hyperVolumeBin = getHome() + "/bins/./hv";
        //String hyperVolumeBin = UserHome.getOplaUserHome() + "bins/hv";
        ProcessBuilder builder = new ProcessBuilder(hyperVolumeBin, "-r", referencePoint, pathToFile);
        builder.redirectErrorStream(true);
        Process p = builder.start();


        return inheritIO(p.getInputStream());

    }

    public static String getHome() {
        return System.getProperties().get("user.home") + "/oplatool";
    }

    public static List<Double> inheritIO(final InputStream src) {
        List<Double> values = new ArrayList<>();

        Scanner sc = new Scanner(src);
        while (sc.hasNextLine())
            values.add(Double.valueOf(sc.nextLine()));

        return values;
    }

    public void showMinPointEpsilonPossibilities(SolutionSet solutionSet) throws Exception {
        for (int minPoints = 3; minPoints <= 3; minPoints++) {
            for (double epsilon = 0.3; epsilon <= 0.4; epsilon += 0.01) {
                System.out.println("-----------------------------------------------------------------------------------------------");
                System.out.println("MINPOINTS: " + minPoints + "; " + "EPSILON: " + epsilon);
                Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
                clustering.setEpsilon(epsilon);
                clustering.setMinPoints(minPoints);
                SolutionSet run = clustering.run();
            }
        }
    }
}
