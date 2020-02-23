package br.ufpr.dinf.gres.opla.view;

import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.opla.entity.Objective;
import database.Database;
import exceptions.MissingConfigurationException;
import jmetal4.core.SolutionSet;
import learning.ClusteringAlgorithm;
import org.junit.Test;
import persistence.AllMetricsPersistenceDependency;
import persistence.MetricsPersistence;
import results.Execution;
import results.Experiment;
import results.FunResults;
import results.InfoResult;
import utils.ExperimentTest;
import utils.QtdElements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InteractiveSolutionsFormTest {

    private static MetricsPersistence mp;

//    @Test
    public void main() throws ClassNotFoundException, IOException {

//        Object[] options = {
//                "Close", "Visualize PLAs alternatives"
//        };
//        JOptionPane.showOptionDialog(null, "Success execution NSGA-II, Finalizing....", "End Optimization", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


        ManagerApplicationConfig managerApplicationConfig = new ManagerApplicationConfig();

        try {
            Connection connection = Database.getConnection();
            AllMetricsPersistenceDependency allMetricsPersistenceDependency = new AllMetricsPersistenceDependency(connection);
            mp = new MetricsPersistence(allMetricsPersistenceDependency);
        } catch (ClassNotFoundException | MissingConfigurationException | SQLException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        Experiment experiement = mp.createExperimentOnDb("AGM", "NSGAII", "");
        Execution execution = new Execution(experiement);
        execution.setFuns(new ArrayList<>());
        execution.setInfos(new ArrayList<>());

        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, null, 1L);
        solutionSet.getSolutionSet().forEach(solution -> {
            execution.getFuns().add(new FunResults(
                    solution.getExecutionId().toString(),
                    "FUN_" + solution.getSolutionName(),
                    solution.getSolutionName(),
                    execution,
                    0,
                    experiement,
                    ""
            ));
            execution.getInfos().add(new InfoResult(solution.getExecutionId().toString(),
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
                    experiement));
        });
        InteractiveSolutions interactiveSolutions = new InteractiveSolutions(managerApplicationConfig, ClusteringAlgorithm.KMEANS, solutionSet);
    }

    @Test
    public void main2() throws ClassNotFoundException, IOException {

//        Object[] options = {
//                "Close", "Visualize PLAs alternatives"
//        };
//        JOptionPane.showOptionDialog(null, "Success execution NSGA-II, Finalizing....", "End Optimization", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


        ManagerApplicationConfig managerApplicationConfig = new ManagerApplicationConfig();

        try {
            Connection connection = Database.getConnection();
            AllMetricsPersistenceDependency allMetricsPersistenceDependency = new AllMetricsPersistenceDependency(connection);
            mp = new MetricsPersistence(allMetricsPersistenceDependency);
        } catch (ClassNotFoundException | MissingConfigurationException | SQLException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        Experiment experiement = mp.createExperimentOnDb("AGM", "NSGAII", "");
        Execution execution = new Execution(experiement);
        execution.setFuns(new ArrayList<>());
        execution.setInfos(new ArrayList<>());

        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, null, 0L);
        solutionSet.getSolutionSet().forEach(solution -> {
            execution.getFuns().add(new FunResults(
                    solution.getExecutionId().toString(),
                    "FUN_" + solution.getSolutionName(),
                    solution.getSolutionName(),
                    execution,
                    0,
                    experiement,
                    ""
            ));
            execution.getInfos().add(new InfoResult(solution.getExecutionId().toString(),
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
                    experiement));
        });
        InteractiveSolutions interactiveSolutions = new InteractiveSolutions(managerApplicationConfig, ClusteringAlgorithm.KMEANS, solutionSet);
    }
}
