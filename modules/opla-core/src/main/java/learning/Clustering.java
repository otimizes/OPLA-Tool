package learning;

import jmetal4.core.SolutionSet;
import org.apache.log4j.Logger;
import weka.clusterers.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class Clustering implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Clustering.class);

    private SolutionSet resultFront;
    private ClusteringAlgorithms algorithm;
    private AbstractClusterer clusterer;
    private ArffExecution arffExecution;

    private Integer numClusters = 3;

    public Clustering() {
    }

    public Clustering(SolutionSet resultFront, ClusteringAlgorithms algorithm) {
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        this.arffExecution = new ArffExecution(resultFront.writeObjectivesToMatrix());
    }

    public SolutionSet run() throws Exception {
        switch (algorithm) {
            case KMEANS:
                return kMeans();
            case DBSCAN:
                return dbscan();
            case OPTICS:
                return optics();
        }
        return null;
    }

    public SolutionSet kMeans() throws Exception {
        clusterer = new SimpleKMeans();
        getKMeans().setSeed(arffExecution.getObjectives().length);
        getKMeans().setPreserveInstancesOrder(true);
        getKMeans().setNumClusters(numClusters);
        getKMeans().buildClusterer(arffExecution.getDataWithoutClass());

        ClusterEvaluation clusterEvaluation = new ClusterEvaluation();
        clusterEvaluation.setClusterer(getKMeans());
        clusterEvaluation.evaluateClusterer(arffExecution.getDataWithoutClass());

        LOGGER.info(clusterEvaluation.clusterResultsToString());

        int[] assignments = getKMeans().getAssignments();
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < assignments.length; i++) {

            LOGGER.info("Cluster " + assignments[i] + " -> " + getKMeans().getClusterCentroids().get(assignments[i]) + " : " + arffExecution.getData().instance(i));
            if (assignments[i] >= 1) {
                toRemove.add(i);
            }
        }

        Collections.reverse(toRemove);
        toRemove.forEach(resultFront::remove);
        return resultFront;
    }

    public SolutionSet dbscan() throws Exception {
        clusterer = new DBSCAN();
        getDBSCAN().setMinPoints(1);
//        Config 1:
        getDBSCAN().setEpsilon(0.4);
        getDBSCAN().setMinPoints(1);
        getDBSCAN().setOptions(new String[]{"-A", "weka.core.MinkowskiDistance"});
        getDBSCAN().buildClusterer(arffExecution.getDataWithoutClass());

        return null;
    }

    public SolutionSet optics() throws Exception {
        clusterer = new OPTICS();
        getOPTICS().setShowGUI(false);
        getOPTICS().buildClusterer(arffExecution.getDataWithoutClass());

        return null;
    }

    public ClusterEvaluation getClusterEvaluation() throws Exception {
        ClusterEvaluation clusterEvaluation = new ClusterEvaluation();
        clusterEvaluation.setClusterer(clusterer);
        clusterEvaluation.evaluateClusterer(arffExecution.getDataWithoutClass());
        return clusterEvaluation;
    }

    public SimpleKMeans getKMeans() {
        return ((SimpleKMeans) clusterer);
    }

    public DBSCAN getDBSCAN() {
        return ((DBSCAN) clusterer);
    }

    public OPTICS getOPTICS() {
        return ((OPTICS) clusterer);
    }

    public SolutionSet getResultFront() {
        return resultFront;
    }

    public void setResultFront(SolutionSet resultFront) {
        this.resultFront = resultFront;
    }

    public ClusteringAlgorithms getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(ClusteringAlgorithms algorithm) {
        this.algorithm = algorithm;
    }

    public AbstractClusterer getClusterer() {
        return clusterer;
    }

    public void setClusterer(AbstractClusterer clusterer) {
        this.clusterer = clusterer;
    }

    public ArffExecution getArffExecution() {
        return arffExecution;
    }

    public void setArffExecution(ArffExecution arffExecution) {
        this.arffExecution = arffExecution;
    }

    public Integer getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(Integer numClusters) {
        this.numClusters = numClusters;
    }
}
