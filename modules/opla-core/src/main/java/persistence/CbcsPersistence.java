package persistence;

import metrics.Cbcs;
import metrics.Elegance;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CbcsPersistence {

    private Connection connection;

    public CbcsPersistence(Connection connection) {
	this.connection = connection;
    }

    public void save(Cbcs cBcs) {

	String executionID = "''";
	if (cBcs.getExecution() != null)
	    executionID = cBcs.getExecution().getId();

	StringBuilder query = new StringBuilder();

	query.append("insert into CbcsMetrics (cBcs, execution_id, is_all, experiement_id, id_solution) values (");
	query.append(cBcs.getCbcs());
	query.append(",");
	query.append(executionID);
	query.append(",");
	if (cBcs.getExecution() == null)
	    query.append("1");
	else
	    query.append("0");
	query.append(",");
	query.append(cBcs.getExperiement().getId());
	query.append(",");
	query.append(cBcs.getIdSolution());
	query.append(")");

	try {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(query.toString());
	} catch (SQLException ex) {
	    Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
