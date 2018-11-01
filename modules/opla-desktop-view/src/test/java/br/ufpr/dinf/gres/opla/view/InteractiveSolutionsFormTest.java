package br.ufpr.dinf.gres.opla.view;

import br.ufpr.dinf.gres.opla.config.ManagerApplicationConfig;
import database.Database;
import exceptions.MissingConfigurationException;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.encodings.solutionType.ArchitectureSolutionType;
import jmetal4.problems.OPLA;
import org.junit.Test;
import persistence.AllMetricsPersistenceDependency;
import persistence.MetricsPersistence;
import results.Execution;
import results.Experiment;
import results.FunResults;
import results.InfoResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class InteractiveSolutionsFormTest {

    private static MetricsPersistence mp;

//    @Test
    public void main() throws ClassNotFoundException {

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

        Experiment experiement = mp.createExperimentOnDb("AGM", "NSGAII", "teste");
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
        Solution solution = new Solution();
        solutionSet.getSolutionSet().add(solution);
        solutionSet.get(0).setType(new ArchitectureSolutionType(new OPLA()));
        ((OPLA) solutionSet.get(0).getType().problem_).setSelectedMetrics(Arrays.asList("featureDriven", "aclass"));

        Solution solution2 = new Solution();
        solutionSet.getSolutionSet().add(solution2);
        solutionSet.get(1).setType(new ArchitectureSolutionType(new OPLA()));
        ((OPLA) solutionSet.get(1).getType().problem_).setSelectedMetrics(Arrays.asList("featureDriven", "aclass", "coe"));

        InteractiveSolutions interactiveSolutions = new InteractiveSolutions(managerApplicationConfig, solutionSet, execution);
    }
}
