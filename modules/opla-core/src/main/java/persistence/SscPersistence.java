package persistence;

import metrics.Elegance;
import metrics.Ssc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SscPersistence {

    private Connection connection;

    public SscPersistence(Connection connection) {
	this.connection = connection;
    }

    public void save(Ssc sSc) {

	String executionID = "''";
	if (sSc.getExecution() != null)
	    executionID = sSc.getExecution().getId();

	StringBuilder query = new StringBuilder();

	query.append("insert into SscMetrics (sSc, execution_id, is_all, experiement_id, id_solution) values (");
	query.append(sSc.getSsc());
	query.append(",");
	query.append(executionID);
	query.append(",");
	if (sSc.getExecution() == null)
	    query.append("1");
	else
	    query.append("0");
	query.append(",");
	query.append(sSc.getExperiement().getId());
	query.append(",");
	query.append(sSc.getIdSolution());
	query.append(")");

	try {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(query.toString());
	} catch (SQLException ex) {
	    Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
