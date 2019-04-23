package learning;

import br.ufpr.dinf.gres.opla.entity.Objective;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import utils.ExperimentTest;

import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

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

        List<List<Double>> listXYZ = ExperimentTest.getListXYZ(run);
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
        clustering.getResultFront().getSolutionSet().stream().sorted(Comparator.comparing(Solution::getSolutionName)).forEach(solution -> {
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
        Clustering.LOGGER.setLevel(Level.ALL);
    }

    @Test
    public void agm() throws IOException {
        LOGGER.info("AGM");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives,null, 7127396432L);

//        rCommand(solutionSet);
        assertEquals(11, solutionSet.getSolutionSet().size());
    }

    @Test
    public void agmOnDBSCAN() throws Exception {
        LOGGER.info("AGM DBSCAN");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives,null, 7127396432L);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
        SolutionSet run = clustering.run();

//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(11, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(4, run.getSolutionSet().size());
        assertEquals(7, solutionSet.getSolutionSet().size() - run.getSolutionSet().size());
        assertEquals(2, clustering.getNumClusters());
    }

    @Test
    public void agmOnKMeans() throws Exception {
        LOGGER.info("AGM KMEANS");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_27112018.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives,null, 7127396432L);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
        SolutionSet run = clustering.run();

//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(11, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(4, run.getSolutionSet().size());
        assertEquals(7, solutionSet.getSolutionSet().size() - run.getSolutionSet().size());
        assertEquals(2, clustering.getNumClusters());
    }

    @Test
    public void bet() throws IOException {
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives,null, 7837731112L);

        LOGGER.info("BET");
//        rCommand(solutionSet);

        assertEquals(17, solutionSet.getSolutionSet().size());
    }

    @Test
    public void betOnDBSCAN() throws Exception {
        LOGGER.info("BET DBSCAN");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives,null, 7837731112L);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.DBSCAN);
        SolutionSet run = clustering.run();


//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(17, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(9, run.getSolutionSet().size());
        assertEquals(8, solutionSet.getSolutionSet().size() - run.getSolutionSet().size());
        assertEquals(2, clustering.getNumClusters());
    }

    @Test
    public void betOnKMeans() throws Exception {
        LOGGER.info("BET KMEANS");
        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("bet_objectives_02122018.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives,null, 7837731112L);

        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithm.KMEANS);
        SolutionSet run = clustering.run();

//        showMinPointEpsilonPossibilities(solutionSet);
//        getSolutionsByClusteringMinObjective(clustering);
//        rCommand(run);

        assertEquals(17, solutionSet.getSolutionSet().size());
        assertEquals(clustering.getBestPerformingCluster(), run.getSolutionSet());
        assertEquals(9, run.getSolutionSet().size());
        assertEquals(8, solutionSet.getSolutionSet().size() - run.getSolutionSet().size());
        assertEquals(2, clustering.getNumClusters());
    }

}
