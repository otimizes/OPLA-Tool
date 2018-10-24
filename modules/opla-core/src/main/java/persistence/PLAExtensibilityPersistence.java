package persistence;

import metrics.Elegance;
import metrics.PLAExtensibility;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PLAExtensibilityPersistence {

    private Connection connection;

    public PLAExtensibilityPersistence(Connection connection) {
        this.connection = connection;
    }

    public void save(PLAExtensibility plaExtensibility) {

        String executionID = "''";
        if (plaExtensibility.getExecution() != null)
            executionID = plaExtensibility.getExecution().getId();

        StringBuilder query = new StringBuilder();

        query.append("insert into PLAExtensibilityMetrics (plaExtensibility, execution_id, is_all, experiement_id, id_solution) values (");
        query.append(plaExtensibility.getPlaExtensibility());
        query.append(",");
        query.append(executionID);
        query.append(",");
        if (plaExtensibility.getExecution() == null)
            query.append("1");
        else
            query.append("0");
        query.append(",");
        query.append(plaExtensibility.getExperiement().getId());
        query.append(",");
        query.append(plaExtensibility.getIdSolution());
        query.append(")");

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
