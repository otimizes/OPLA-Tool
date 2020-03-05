package br.ufpr.dinf.gres.domain.view;

import br.ufpr.dinf.gres.architecture.io.OPLAThreadScope;
import br.ufpr.dinf.gres.common.exceptions.MissingConfigurationException;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.database.Database;
import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;
import br.ufpr.dinf.gres.core.jmetal4.results.InfoResults;
import br.ufpr.dinf.gres.core.learning.ClusteringAlgorithm;
import br.ufpr.dinf.gres.core.learning.ExperimentTest;
import br.ufpr.dinf.gres.core.persistence.Persistence;
import br.ufpr.dinf.gres.domain.config.ManagerApplicationConfig;
import br.ufpr.dinf.gres.domain.entity.Objective;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InteractiveSolutionsFormTest {

    private static Persistence mp;

//    @Test
    public void main() throws ClassNotFoundException, IOException {

//        Object[] options = {
//                "Close", "Visualize PLAs alternatives"
//        };
//        JOptionPane.showOptionDialog(null, "Success execution NSGA-II, Finalizing....", "End Optimization", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


        ManagerApplicationConfig managerApplicationConfig = new ManagerApplicationConfig();

        try {
            Connection connection = Database.getConnection();
            mp = new Persistence();
        } catch (ClassNotFoundException | MissingConfigurationException | SQLException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ExperimentResults experimentResults = mp.createExperimentOnDb("AGM", "NSGAII", "", OPLAThreadScope.hash.get());
        ExecutionResults executionResults = new ExecutionResults(experimentResults);
        executionResults.setInfos(new ArrayList<>());

        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, null, 1L);
        solutionSet.getSolutionSet().forEach(solution -> {
            executionResults.getInfos().add(new InfoResults(
                    solution.getExecutionId().toString(),
                    "FUN_" + solution.getSolutionName(),
                    solution.getSolutionName(),
                    executionResults,
                    0,
                    experimentResults,
                    ""
            ));
            executionResults.getInfos().add(new InfoResults(solution.getExecutionId().toString(),
                    1,
                    "1",
                    executionResults,
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
                    experimentResults));
        });
        br.ufpr.dinf.gres.domain.view.InteractiveSolutions interactiveSolutions = new br.ufpr.dinf.gres.domain.view.InteractiveSolutions(managerApplicationConfig, ClusteringAlgorithm.KMEANS, solutionSet);
    }

//    @Test
    public void main2() throws ClassNotFoundException, IOException {

//        Object[] options = {
//                "Close", "Visualize PLAs alternatives"
//        };
//        JOptionPane.showOptionDialog(null, "Success execution NSGA-II, Finalizing....", "End Optimization", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);


        ManagerApplicationConfig managerApplicationConfig = new ManagerApplicationConfig();

        try {
            Connection connection = Database.getConnection();
            mp = new Persistence();
        } catch (ClassNotFoundException | MissingConfigurationException | SQLException e) {
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }

        ExperimentResults experimentResults = mp.createExperimentOnDb("AGM", "NSGAII", "", OPLAThreadScope.hash.get());
        ExecutionResults executionResults = new ExecutionResults(experimentResults);
        executionResults.setInfos(new ArrayList<>());

        List<Objective> objectives = ExperimentTest.getObjectivesFromFile("agm_objectives_03062019.csv");
        SolutionSet solutionSet = ExperimentTest.getSolutionSetFromObjectiveListTest(objectives, null, 0L);
        solutionSet.getSolutionSet().forEach(solution -> {
            executionResults.getInfos().add(new InfoResults(
                    solution.getExecutionId().toString(),
                    "FUN_" + solution.getSolutionName(),
                    solution.getSolutionName(),
                    executionResults,
                    0,
                    experimentResults,
                    ""
            ));
            executionResults.getInfos().add(new InfoResults(solution.getExecutionId().toString(),
                    1,
                    "1",
                    executionResults,
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
                    experimentResults));
        });
        br.ufpr.dinf.gres.domain.view.InteractiveSolutions interactiveSolutions = new br.ufpr.dinf.gres.domain.view.InteractiveSolutions(managerApplicationConfig, ClusteringAlgorithm.KMEANS, solutionSet);
    }
}
