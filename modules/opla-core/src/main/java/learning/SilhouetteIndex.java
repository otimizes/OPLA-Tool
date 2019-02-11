package learning;

import weka.clusterers.AbstractClusterer;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

/**
 * It measures the quality of a clustering. Determines how well each object lies within its cluster. A high average silhouette width indicates a good clustering.
 * Computes the average silhouette of observations for different values f k. The Optimal number of clusters k is one that maximize the average silhouette over a range of possible values
 * Kaufman and Rousseauw in 1990
 * Compute clustering algorithm (e.g., k-means clustering) for different values of k. For instance, by varying k from 1 to 10 clusters.
 * For each k, calculate the average silhouette of observations (avg.sil).
 * Plot the curve of avg.sil according to the number of clusters k.
 * The location of the maximum is considered as the appropriate number of clusters.
 */
public class SilhouetteIndex implements Serializable{

    /** Serialization */
    static final long serialVersionUID = -305533168492651330L;

    /** Clusters SI. */
    protected ArrayList<Double> m_clustersSilhouette;

    /** Global SI. */
    protected double m_globalSilhouette;

    /** Default constructor. */
    public SilhouetteIndex() {
        m_clustersSilhouette = new ArrayList<Double>();
        m_globalSilhouette = 0.0;
    }

    /**
     * Evaluates the clusterer quality, using the Silhouette-Index
     * algorithm.
     *
     * @param clusterer given clusterer.
     * @param instances dataset.
     */
    @SuppressWarnings("unchecked")
    public void evaluate(AbstractClusterer clusterer, Instances centroids,
                         Instances instances, DistanceFunction distanceFunction) throws Exception {

        if (clusterer == null || instances == null)
            throw new Exception("SilhouetteIndex: the clusterer or instances are null!");

        /*
         * Attributes each instance to your centroid.
         *
         * Note that this is not the right way to do, because there's
         * one way to get the instances already classified instead
         * of classify again. As long as I do not know how to accomplish
         * that, I'll classify again.
         */
        ArrayList<Instance>[] clusteredInstances =
                (ArrayList<Instance>[]) new ArrayList<?>[centroids.size()];

        /* Initialize. */
        for (int i = 0; i < centroids.size(); i++)
            clusteredInstances[i] = new ArrayList<>();

        /* Fills. */
        for (int i = 0; i < instances.size(); i++)
            clusteredInstances[ clusterer.clusterInstance( instances.get(i) ) ]
                    .add( instances.get(i) );

        /* For each centroid. */
        for (int i = 0; i < clusteredInstances.length; i++) {
            double centroidSilhouetteIndex = 0.0;

            /*
             * Calculate the distance between a given point to the others
             * within the same centroid.
             */
            for (int j = 0; j < clusteredInstances[i].size(); j++) {
                double pointSilhouetteIndex = 0.0;
                double meanDistSameC  = 0.0;
                double meanDistOtherC = 0.0;

                /* My reference point. */
                Instance i1 = clusteredInstances[i].get(j);

                /* For each other point, in the same centroid.. */
                for (int k = 0; k < clusteredInstances[i].size(); k++) {
                    /* Different point. */
                    if (k == j)
                        continue;

                    /* Gets the distance between p1 and p2. */
                    Instance i2 = clusteredInstances[i].get(k);
                    meanDistSameC += distanceFunction.distance(i1, i2);
                }

                /* Mean. */
                meanDistSameC /= (clusteredInstances[i].size() - 1) == 0 ? 1 : (clusteredInstances[i].size() - 1);

                /* Get the nearest cluster to the point j. */
                double minDistance = Double.MAX_VALUE;
                int minCentroid = 0;

                for (int k = 0; k < centroids.size(); k++) {
                    /* Other clusters, ;-). */
                    if (k == i)
                        continue;

                    /* Distance. */
                    Instance i2 = centroids.get(k);
                    double distance = distanceFunction.distance(i1, i2);

                    /* Checks if is lower. */
                    if (distance < minDistance) {
                        minDistance = distance;
                        minCentroid = k;
                    }
                }

                /*
                 * We already know which cluster is closest, so now we have to go
                 * through this cluster and get the average distance from all points
                 * to point p1.
                 */
                for (int k = 0; k < clusteredInstances[minCentroid].size(); k++) {
                    /* Gets the distance between p1 and p2. */
                    Instance i2 = clusteredInstances[minCentroid].get(k);

                    /* Distance. */
                    meanDistOtherC += distanceFunction.distance(i1, i2);
                }

                /* Mean. */
                meanDistOtherC /= (clusteredInstances[minCentroid].size() - 1) == 0 ? 1 : (clusteredInstances[minCentroid].size() - 1);

                /* Now, we calculate the silhouette index, \o/. */
                pointSilhouetteIndex = (meanDistOtherC - meanDistSameC) /
                        Math.max( meanDistSameC, meanDistOtherC );

                /* Sum to the centroid silhouette. */
                centroidSilhouetteIndex += pointSilhouetteIndex;
            }

            centroidSilhouetteIndex /= (clusteredInstances[i].size() - 1) == 0 ? 1 : (clusteredInstances[i].size() - 1);
            m_globalSilhouette += centroidSilhouetteIndex;

            m_clustersSilhouette.add( centroidSilhouetteIndex );
        }

        m_globalSilhouette /= (m_clustersSilhouette.size() == 0) ? 1 : m_clustersSilhouette.size();
    }

    /**
     * Gets the silhouetteIndex for all clusters.
     *
     * @return Returns the clusters Silhouette-Index.
     */
    public ArrayList<Double> getClustersSilhouette() {
        return m_clustersSilhouette;
    }

    /**
     * Gets the global silhouette, i.e: the mean silhouette of
     * all points.
     *
     * @return Returns the mean silhouette of all points.
     */
    public double getGlobalSilhouette() {
        return m_globalSilhouette;
    }

    /**
     * Evaluates a given silhouette index result.
     *
     * @param si Silhouette-Index.
     */
    public String evalSilhouette(double si) {
        String eval = "";

        if (si > 0.70)
            eval = "strong structure!";
        else if (si >  0.50 && si <= 0.70)
            eval = "reasonably structure!";
        else if (si >  0.25 && si <= 0.50)
            eval = "weak structure!";
        else if (si <= 0.25)
            eval = "a non substancial structure was found!";

        return eval;
    }

    /**
     * Returns a string describing the results.
     *
     * @return a string describing the clusterer.
     */
    @Override
    public String toString() {
        StringBuffer description = new StringBuffer("");

        /* Clusters. */
        for (int i = 0; i < m_clustersSilhouette.size(); i++) {
            double si = m_clustersSilhouette.get(i);
            description.append("   Cluster " + i + ": " + String.format(Locale.US, "%.4f", si)
                    + ", veredict: " + evalSilhouette(si) + "\n");
        }

        description.append("   Mean: " + String.format(Locale.US, "%.4f", m_globalSilhouette)
                + ", veredict: " + evalSilhouette(m_globalSilhouette));

        return description.toString();
    }
}