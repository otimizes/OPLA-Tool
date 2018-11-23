package learning;

import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import org.apache.log4j.Logger;
import weka.clusterers.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author WmfSystem
 * Class that encompasses the methods of clustering, such as K-Means, DBSCAN and OPTICS
 */
public class Clustering implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(Clustering.class);

    private SolutionSet resultFront;
    private ClusteringAlgorithms algorithm;
    private AbstractClusterer clusterer;
    private ArffExecution arffExecution;
    private List<Solution> filteredSolutions = new ArrayList<>();
    private List<Integer> idsFilteredSolutions = new ArrayList<>();
    private Double indexToFilter;

    /**
     * K-Means Parameters
     */
    private Integer numClusters = 3;

    /**
     * DBSCAN and OPTICS Parameters
     */
    private Integer minPoints = 1;
    private Double epsilon = 0.4;

    public Clustering() {
    }

    public Clustering(SolutionSet resultFront, ClusteringAlgorithms algorithm) {
        this.resultFront = resultFront;
        this.algorithm = algorithm;
        this.arffExecution = new ArffExecution(resultFront.writeObjectivesToMatrix());
    }

    /**
     * Execution Method
     * @return Solution Set
     * @throws Exception Default Exception
     */
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

    /**
     * K-Means Execution Method
      * @return Solution Set
     * @throws Exception Default Exception
     */
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

    /**
     * DBSCAN Execution Method
     * - Observations:
     *      The only measure that changes the solution is as follows:
     *      getDBSCAN().setOptions(new String[]{"-A", "weka.core.ManhattanDistance"});
     * @return Solution Set
     * @throws Exception
     */
    public SolutionSet dbscan() throws Exception {
        clusterer = new DBSCAN();
        getDBSCAN().setMinPoints(getMinPoints());
        getDBSCAN().setEpsilon(getEpsilon());
        getDBSCAN().buildClusterer(arffExecution.getDataWithoutClass());

        return getFilteredSolutionSet();
    }

    /**
     * Method not completed, because in the current version of Weka, the OPTICS does not present results
     * @return Nothing
     * @throws Exception Default Exception
     */
    public SolutionSet optics() throws Exception {
        clusterer = new OPTICS();
        getOPTICS().setShowGUI(false);
        getOPTICS().buildClusterer(arffExecution.getDataWithoutClass());

        return null;
    }

    /**
     * Filtered Solution Set by attribute indexToFilter
      * @return Solution Set Filtered
     * @throws Exception Default Exception
     */
    private SolutionSet getFilteredSolutionSet() throws Exception {
        double[] assignments = getClusterEvaluation().getClusterAssignments();
        for (int i = 0; i < getClusterEvaluation().getClusterAssignments().length; i++) {
            LOGGER.info("Cluster " + assignments[i] + " -> " + assignments[i] + " : " + arffExecution.getData().instance(i));
            if (assignments[i] >= getIndexToFilter()) {
                idsFilteredSolutions.add(i);
                resultFront.get(i).setClusterId(assignments[i]);
                filteredSolutions.add(resultFront.get(i));

            }
        }

        Collections.reverse(idsFilteredSolutions);
        idsFilteredSolutions.forEach(resultFront::remove);
        return resultFront;
    }

    /**
     * Cluster Evaluation Object for analysis of results
     * @return Clustes Evaluation Objetc
     * @throws Exception Default Exception
     */
    public ClusterEvaluation getClusterEvaluation() throws Exception {
        ClusterEvaluation clusterEvaluation = new ClusterEvaluation();
        clusterEvaluation.setClusterer(clusterer);
        clusterEvaluation.evaluateClusterer(arffExecution.getDataWithoutClass());
        return clusterEvaluation;
    }

    /**
     * Cast the Clusterer to SimpleKMeans Object
     * @return SimpleKMeans Object
     */
    public SimpleKMeans getKMeans() {
        return ((SimpleKMeans) clusterer);
    }

    /**
     * Cast the Clusterer to DBSCAN Object
     * @return DBSCAN Object
     */
    public DBSCAN getDBSCAN() {
        return ((DBSCAN) clusterer);
    }

    /**
     * Cast the Clusterer to OPTICS Object
     * @return OPTICS Object
     */
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

    /**
     * Get Index to Filter
     * Set by default: 1 -> K-Means, 2 -> DBSCAN and OPTICS
     * @return Index to Filter Value
     */
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

    public List<Integer> getIdsFilteredSolutions() {
        return idsFilteredSolutions;
    }

    public void setIdsFilteredSolutions(List<Integer> idsFilteredSolutions) {
        this.idsFilteredSolutions = idsFilteredSolutions;
    }
}
