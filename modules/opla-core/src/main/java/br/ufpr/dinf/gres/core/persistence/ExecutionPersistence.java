package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.FunResults;
import br.ufpr.dinf.gres.core.jmetal4.results.InfoResults;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecutionPersistence {

    private AllMetricsPersistenceDependency allMetricsPersistenceDependencies;

    public ExecutionPersistence(AllMetricsPersistenceDependency allMetricsPersistenceDependencies) {
        this.allMetricsPersistenceDependencies = allMetricsPersistenceDependencies;
    }

    public void persist(ExecutionResults executionResults) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("insert into executions (id, experiement_id, time) values ");
        query.append("(");
        query.append(executionResults.getId());
        query.append(",");
        query.append(executionResults.getExperiement().getId());
        query.append(",");
        query.append(executionResults.getTime());
        query.append(")");

        Connection connection = allMetricsPersistenceDependencies.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(query.toString());
        statement.close();

        if (executionResults.getInfos() != null) {
            for (InfoResults ir : executionResults.getInfos())
                allMetricsPersistenceDependencies.getInfosPersistence().persistInfoDatas(ir);
        }

        if (executionResults.getFuns() != null) {
            for (FunResults fr : executionResults.getFuns())
                allMetricsPersistenceDependencies.getFunsPersistence().persistFunsDatas(fr);
        }

        MetricsPersistence metricsPersistence = new MetricsPersistence(allMetricsPersistenceDependencies);
        metricsPersistence.persisteMetrics(executionResults);
        metricsPersistence = null;

    }

}
