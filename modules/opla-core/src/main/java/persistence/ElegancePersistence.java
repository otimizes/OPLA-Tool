package persistence;

import metrics.Elegance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElegancePersistence {

    private Connection connection;

    public ElegancePersistence(Connection connection) {
        this.connection = connection;
    }

    /**
     * Save to database
     */
    public void save(Elegance eleganceMetric) {

        String executionID = "''";
        if (eleganceMetric.getExecution() != null)
            executionID = eleganceMetric.getExecution().getId();

        StringBuilder query = new StringBuilder();
        query.append("insert into EleganceMetrics (nac,atmr,ec,elegance,execution_id, experiement_id, is_all, id_solution) values (");
        query.append(eleganceMetric.getNac());
        query.append(",");
        query.append(eleganceMetric.getAtmr());
        query.append(",");
        query.append(eleganceMetric.getEc());
        query.append(",");
        query.append(eleganceMetric.evaluateEleganceFitness());
        query.append(",");
        query.append(executionID);
        query.append(",");
        query.append(eleganceMetric.getExperiement().getId());
        query.append(",");
        if (eleganceMetric.getExecution() == null)
            query.append("1");
        else
            query.append("0");
        query.append(",");
        query.append(eleganceMetric.getIdSolution());
        query.append(")");

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
