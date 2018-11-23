package learning;

import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Logger;
import weka.clusterers.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Clustering implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Clustering.class);

    private SolutionSet resultFront;
    private ClusteringAlgorithms algorithm;
    private AbstractClusterer clusterer;
    private ArffExecution arffExecution;

    private Integer numClusters = 3;
    private Integer minPoints = 1;
    private Double epsilon = 0.4;
    private List<Solution> filteredSolutions = new ArrayList<>();
    private Double indexToFilter;

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

        return getFilteredSolutionSet();
    }

    public SolutionSet dbscan() throws Exception {
        clusterer = new DBSCAN();
        getDBSCAN().setMinPoints(getMinPoints());
        getDBSCAN().setEpsilon(getEpsilon());
//        A unica que altera a solução é esta medida
//        getDBSCAN().setOptions(new String[]{"-A", "weka.core.ManhattanDistance"});
        getDBSCAN().buildClusterer(arffExecution.getDataWithoutClass());

        return getFilteredSolutionSet();
    }

    public SolutionSet optics() throws Exception {
        clusterer = new OPTICS();
        getOPTICS().setShowGUI(false);
        getOPTICS().buildClusterer(arffExecution.getDataWithoutClass());

        return null;
    }

    private SolutionSet getFilteredSolutionSet() throws Exception {
        double[] assignments = getClusterEvaluation().getClusterAssignments();
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int i = 0; i < getClusterEvaluation().getClusterAssignments().length; i++) {
            LOGGER.info("Cluster " + assignments[i] + " -> " + assignments[i] + " : " + arffExecution.getData().instance(i));
            if (assignments[i] >= getIndexToFilter()) {
                toRemove.add(i);
                resultFront.get(i).setClusterId(assignments[i]);
                filteredSolutions.add(resultFront.get(i));
            }
        }

        Collections.reverse(toRemove);
        toRemove.forEach(resultFront::remove);
        return resultFront;
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

    public Integer getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(Integer minPoints) {
        this.minPoints = minPoints;
    }

    public Double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(Double epsilon) {
        this.epsilon = epsilon;
    }

    public List<Solution> getFilteredSolutions() {
        return filteredSolutions;
    }

    public void setFilteredSolutions(List<Solution> filteredSolutions) {
        this.filteredSolutions = filteredSolutions;
    }

    public double getIndexToFilter() {
        if (indexToFilter == null) {
            switch (algorithm) {
                case KMEANS:
                    return 1;
                case DBSCAN:
                case OPTICS:
                    return 2;
            }
        }
        return indexToFilter;
    }

    public void setIndexToFilter(double indexToFilter) {
        this.indexToFilter = indexToFilter;
    }
}
