package learning;

import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import liquibase.util.csv.CSVReader;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import utils.MathUtils;

import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.Assert.*;

public class ClusteringExperimentTest {

    public static final Logger LOGGER = Logger.getLogger(ClusteringExperimentTest.class);

    /**
     * Generate R Commands to experiment
     * @param run solutionSet
     * @throws IOException
     */
    public static void rCommand(SolutionSet run) throws IOException {
//        List<Double> hypervolume = calculateHypervolume(run);
//        System.out.println(hypervolume.toString().replace("[", "hv = c(").replace("]", ")"));
//        System.out.println("shapiro.test(hv)");
//        System.out.println("boxplot(hv)");

        List<List<Double>> listXYZ = getListXYZ(run);
        System.out.println(listXYZ.get(0).toString().replace("[", "x = c(").replace("]", ")"));
        String s = Integer.toHexString(new Color(new Random().nextInt(0xFFFFFF)).getRGB());
        System.out.println(listXYZ.get(1).toString().replace("[", "y = c(").replace("]", ")"));
        System.out.println(listXYZ.get(2).toString().replace("[", "z = c(").replace("]", ")"));
        System.out.println(("colors = c(" + listXYZ.get(0).stream().map(c -> "\"" + "blue" + "\",").collect(Collectors.joining()) + ")").replace(",)", ")"));
        System.out.println("s3d = scatterplot3d(x, y, z, type = \"p\", angle = 10, pch = 1, main = \"scatterplot3d\", zlab=\"COE\", ylab=\"ACLASS\", xlab=\"FM\", color=colors)");
    }

    /**
     * Log of clustering min points
     * @param clustering Clustered solution
     */
    private void getSolutionsByClusteringMinObjective(Clustering clustering) {

        System.out.println();
        System.out.println("Cluster by min TradeOff");
        clustering.getResultFront().getAllSolutions().stream().sorted(Comparator.comparing(Solution::getSolutionName)).forEach(solution -> {
//            System.out.print(solution.getSolutionName() + " - ");
//            System.out.print(solution.getObjective(0) + " ");
//            System.out.print(solution.getObjective(1) + " ");
//            System.out.print(solution.getObjective(2) + " - ");
            System.out.println(new DecimalFormat("#.##").format(clustering.euclidianDistance(solution)));
        });

        System.out.println();

        for (int i = 0; i < clustering.getNumObjectives(); i++) {
            System.out.println();
            System.out.println("Cluster by min objective " + getObjectiveName(i));
            clustering.getSolutionsByClusterWithMinObjective(i).forEach(solution -> {
                System.out.println("Cluster " + solution.getClusterId() + " " + solution.getSolutionName() + " - " + solution.toStringObjectives() + " - " + new DecimalFormat("#.##").format(clustering.euclidianDistance(solution)));
            });
            System.out.println();
        }
    }

    private String getObjectiveName(int i) {
        switch (i) {
            case 0: return "featureDriven";
            case 1: return "aclass";
            case 2: return "coe";
        }
        return null;
    }

    @Before
    public void init() {
        Clustering.LOGGER.setLevel(Level.OFF);
    }

    @Test
    public void agm() throws IOException {
        LOGGER.info("AGM");
        List<Objective> objectives = getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives, "agm");

//        rCommand(solutionSet);
        assertEquals(11, solutionSet.getSolutionSet().size());
    }

    @Test
    public void agmOnDBSCAN() throws Exception {
        LOGGER.info("AGM DBSCAN");
        List<Objective> objectives = getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives, "agm");

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
        SolutionSet run = clustering.run();

//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(11, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(4, run.getSolutionSet().size());
        assertEquals(7, run.getFilteredSolutions().size());
        assertEquals(2, clustering.getNumClusters());
    }

    @Test
    public void agmOnKMeans() throws Exception {
        LOGGER.info("AGM KMEANS");
        List<Objective> objectives = getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives, "agm");

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
        SolutionSet run = clustering.run();

//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(11, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(4, run.getSolutionSet().size());
        assertEquals(7, run.getFilteredSolutions().size());
        assertEquals(2, clustering.getNumClusters());
    }

    @Test
    public void bet() throws IOException {
        List<Objective> objectives = getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives, "bet");

        LOGGER.info("BET");
//        rCommand(solutionSet);

        assertEquals(17, solutionSet.getSolutionSet().size());
    }

    @Test
    public void betOnDBSCAN() throws Exception {
        LOGGER.info("BET DBSCAN");
        List<Objective> objectives = getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives, "bet");

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
        SolutionSet run = clustering.run();


//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(17, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(9, run.getSolutionSet().size());
        assertEquals(8, run.getFilteredSolutions().size());
        assertEquals(2, clustering.getNumClusters());
    }

    @Test
    public void betOnKMeans() throws Exception {
        LOGGER.info("BET KMEANS");
        List<Objective> objectives = getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = getSolutionSetFromObjectiveList(objectives, "bet");

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
        SolutionSet run = clustering.run();

//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(17, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(9, run.getSolutionSet().size());
        assertEquals(8, run.getFilteredSolutions().size());
        assertEquals(2, clustering.getNumClusters());
    }

    private SolutionSet getSolutionSetFromObjectiveList(List<Objective> objectives, String pla) {
        Long id = Objects.equals(pla, "agm") ? 7127396432L : 7837731112L;
        SolutionSet solutionSet = new SolutionSet();
        objectives.forEach(objective -> {
            if (objective.getExecution() != null && Objects.equals(objective.getExecution().getId(), id)) {
                Solution solution = new Solution();
                solution.createObjective(3);
                String[] split = objective.getObjectives().split("\\|");
                solution.setObjective(0, Double.parseDouble(split[0]));
                solution.setObjective(1, Double.parseDouble(split[1]));
                solution.setObjective(2, Double.parseDouble(split[2]));
                solution.setSolutionName(objective.getSolutionName());
                solution.setExecutionId(objective.getExecution() != null ? objective.getExecution().getId() : 0);
                solution.setNumberOfObjectives(3);
                solutionSet.getSolutionSet().add(solution);
            }
        });
        return solutionSet;
    }

    private List<Objective> getObjectivesFromFile(String filename) throws IOException {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource(filename).getFile());
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        List<Objective> objectives = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            objectives.add(new Objective(nextLine[0], nextLine[1], nextLine[2], nextLine[3], nextLine[4], nextLine[5]));
        }
        return objectives;
    }

    private String getNormalizedNewObjectivesOfClusteredSolutions(SolutionSet run) {
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

    private List<Double> calculateHypervolume(SolutionSet run) throws IOException {
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

    private static String getHome() {
        return System.getProperties().get("user.home") + "/oplatool";
    }

    private static List<Double> inheritIO(final InputStream src) {
        List<Double> values = new ArrayList<>();

        Scanner sc = new Scanner(src);
        while (sc.hasNextLine())
            values.add(Double.valueOf(sc.nextLine()));

        return values;
    }

    private void showMinPointEpsilonPossibilities(SolutionSet solutionSet) throws Exception {
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
