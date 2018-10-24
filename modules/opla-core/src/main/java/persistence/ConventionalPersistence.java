package persistence;

import metrics.Conventional;
import metrics.Elegance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConventionalPersistence {

    private Connection connection;

    public ConventionalPersistence(Connection connection) {
        this.connection = connection;
    }

    public void save(Conventional conventional) {

        String executionID = "''";
        if (conventional.getExecution() != null)
            executionID = conventional.getExecution().getId();

        StringBuilder query = new StringBuilder();

        query.append("insert into ConventionalMetrics (sum_cohesion, cohesion,"
                + " macAggregation, meanDepComps, meanNumOps, sumClassesDepIn,"
                + " sumClassesDepOut, sumDepIn, sumDepOut, execution_id, is_all, experiement_id, id_solution)"
                + " values (");

        query.append(conventional.getSumChoesion());
        query.append(",");
        query.append(conventional.getCohesion());
        query.append(",");
        query.append(conventional.getMacAggregation());
        query.append(",");
        query.append(conventional.getMeanDepComps());
        query.append(",");
        query.append(conventional.getMeanNumOps());
        query.append(",");
        query.append(conventional.getSumClassesDepIn());
        query.append(",");
        query.append(conventional.getSumClassesDepOut());
        query.append(",");
        query.append(conventional.getSumDepIn());
        query.append(",");
        query.append(conventional.getSumDepOut());
        query.append(",");
        query.append(executionID);
        query.append(",");
        if (conventional.getExecution() == null)
            query.append("1");
        else
            query.append("0");
        query.append(",");
        query.append(conventional.getExperiement().getId());
        query.append(",");
        query.append(conventional.getIdSolution());
        query.append(")");

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
