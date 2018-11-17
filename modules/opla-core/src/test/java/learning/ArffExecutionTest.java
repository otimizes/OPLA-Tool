package learning;

import org.junit.Assert;
import org.junit.Test;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;

public class ArffExecutionTest {

    @Test
    public void main() throws Exception {
        ArffExecution arffExecution = new ArffExecution(new double[][]{{1, 1, 1}, {2, 2, 2}});

        Assert.assertNotNull(arffExecution.toString());
    }

    @Test
    public void getDataWithoutClass() throws Exception {
        ArffExecution arffExecution = new ArffExecution(new double[][]{{802,36,25}, {752,30,26}, {728,26,27}, {40, 30, 24}, {700, 40, 20}, {400, 30, 25}});
        System.out.println(arffExecution);

        SimpleKMeans kMeans = new SimpleKMeans();
        kMeans.setSeed(arffExecution.getObjectives().length-1);
        kMeans.setPreserveInstancesOrder(false);
        kMeans.setNumClusters(2);
        kMeans.setMaxIterations(50);
        kMeans.buildClusterer(arffExecution.getDataWithoutClass());

        ClusterEvaluation clusterEvaluation = new ClusterEvaluation();
        clusterEvaluation.setClusterer(kMeans);
        clusterEvaluation.evaluateClusterer(arffExecution.getDataWithoutClass());
        System.out.println(clusterEvaluation.clusterResultsToString());

        Assert.assertNotNull(clusterEvaluation.clusterResultsToString());
    }
}
