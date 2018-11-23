package learning;

import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.encodings.solutionType.ArchitectureSolutionType;
import jmetal4.problems.OPLA;
import org.junit.Test;
import results.Execution;
import results.Experiment;
import results.FunResults;
import results.InfoResult;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ClusteringTest {
    //    [802, 36, 25], [752, 30, 26], [728, 26, 27], [40, 30, 24], [700, 40, 20], [400, 30, 25]
    private double[][] doubles = {{802, 36, 25}, {752, 30, 26}, {728, 26, 27}, {40, 30, 24}, {700, 40, 20}, {400, 30, 25}};

    @Test
    public void kMeans() throws Exception {
        SolutionSet solutionSet = getSolutionSet(doubles);
        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithms.KMEANS);
        SolutionSet run = clustering.run();

        System.out.println(clustering.getClusterEvaluation().clusterResultsToString());
        assertEquals(4, clustering.getFilteredSolutions().size());
        assertEquals(2, run.size());
    }

    @Test
    public void dbscan() throws Exception {
        SolutionSet solutionSet = getSolutionSet(doubles);
        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithms.DBSCAN);
        SolutionSet run = clustering.run();


        System.out.println(clustering.getClusterEvaluation().clusterResultsToString());
        assertEquals(3, clustering.getFilteredSolutions().size());
        assertEquals(3, run.size());
    }

    @Test
    public void optics() throws Exception {
        SolutionSet solutionSet = getSolutionSet(doubles);
        Clustering clustering = new Clustering(solutionSet, ClusteringAlgorithms.OPTICS);
        SolutionSet run = clustering.run();

        System.out.println(clustering.getClusterEvaluation().clusterResultsToString());
//        assertNotNull(run);
    }

    private SolutionSet getSolutionSet(double[][] doubles) throws Exception {
        Experiment experiement = new Experiment("AGM", "NSGAII", "teste");
        Execution execution = new Execution(experiement);

        execution.setFuns(Arrays.asList(new FunResults(
                "1492913161",
                "FUN_agm_1492913161",
                "VAR_0_agm-1492913161",
                execution,
                0,
                experiement,
                ""
        ), new FunResults(
                "1492913122",
                "FUN_agm_1492913122",
                "VAR_0_agm-1492913122",
                execution,
                0,
                experiement,
                ""
        )));

        execution.setInfos(Arrays.asList(
                new InfoResult("1",
                        1,
                        "1",
                        execution,
                        "",
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        experiement),
                new InfoResult("1",
                        2,
                        "2",
                        execution,
                        "",
                        2,
                        2,
                        2,
                        2,
                        2,
                        2,
                        2,
                        2,
                        2,
                        experiement)
        ));

        SolutionSet solutionSet = new SolutionSet();


        for (int i = 0; i < doubles.length; i++) {
            Solution solution = new Solution();
            solution.createObjective(3);
            solution.setObjective(0, doubles[i][0]);
            solution.setObjective(1, doubles[i][1]);
            solution.setObjective(2, doubles[i][2]);
            solution.setNumberOfObjectives(3);
            solutionSet.getSolutionSet().add(solution);
            solutionSet.get(i).setType(new ArchitectureSolutionType(new OPLA()));
            ((OPLA) solutionSet.get(i).getType().problem_).setSelectedMetrics(Arrays.asList("featureDriven", "aclass", "coe"));
        }


        return solutionSet;
    }
}
