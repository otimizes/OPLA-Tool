package learning;

import static org.junit.Assert.*;

import org.junit.Test;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;

public class ArffWithKMeansExecutionTest {

    @Test
    public void main() throws Exception {
        ArffExecution arffExecution = new ArffExecution(new double[][]{{1, 1, 1}, {2, 2, 2}});

        assertNotNull(arffExecution.toString());
    }

    @Test
    public void getDataWithoutClass() throws Exception {
        ArffExecution arffExecution = new ArffExecution(new double[][]{{802, 36, 25}, {752, 30, 26}, {728, 26, 27}, {40, 30, 24}, {700, 40, 20}, {400, 30, 25}});

        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setSeed(arffExecution.getObjectives().length - 1);
        kMeans.setPreserveInstancesOrder(true);
        kMeans.setNumClusters(3);
        kMeans.buildClusterer(arffExecution.getDataWithoutClass());

        ClusterEvaluation clusterEvaluation = new ClusterEvaluation();
        clusterEvaluation.setClusterer(kMeans);
        clusterEvaluation.evaluateClusterer(arffExecution.getDataWithoutClass());
        System.out.println(clusterEvaluation.clusterResultsToString());

        int[] assignments = kMeans.getAssignments();
        for (int i = 0; i < assignments.length; i++) {
            System.out.println("Cluster " + assignments[i] + " -> " + kMeans.getClusterCentroids().get(assignments[i]) + " : " + arffExecution.getData().instance(i));
        }

        assertNotNull(clusterEvaluation.clusterResultsToString());
        assertEquals(arffExecution.getAtts().size(), 4);
        assertEquals(arffExecution.getAttrIndices(), 3);
        assertEquals(arffExecution.getData().size(), 6);
    }
}
