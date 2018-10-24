package persistence;

import results.Execution;
import results.FunResults;
import results.InfoResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecutionPersistence {

    private AllMetricsPersistenceDependency allMetricsPersistenceDependencies;

    public ExecutionPersistence(AllMetricsPersistenceDependency allMetricsPersistenceDependencies) {
        this.allMetricsPersistenceDependencies = allMetricsPersistenceDependencies;
    }

    public void persist(Execution execution) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into executions (id, experiement_id, time) values ");
        query.append("(");
        query.append(execution.getId());
        query.append(",");
        query.append(execution.getExperiement().getId());
        query.append(",");
        query.append(execution.getTime());
        query.append(")");

        Connection connection = allMetricsPersistenceDependencies.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query.toString());
        statement.close();

        if (execution.getInfos() != null) {
            for (InfoResult ir : execution.getInfos())
                allMetricsPersistenceDependencies.getInfosPersistence().persistInfoDatas(ir);
        }

        if (execution.getFuns() != null) {
            for (FunResults fr : execution.getFuns())
                allMetricsPersistenceDependencies.getFunsPersistence().persistFunsDatas(fr);
        }

        MetricsPersistence metricsPersistence = new MetricsPersistence(allMetricsPersistenceDependencies);
        metricsPersistence.persisteMetrics(execution);
        metricsPersistence = null;

    }

}
