package persistence;

import metrics.Elegance;
import metrics.Wocsclass;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WocsclassPersistence {

    private Connection connection;

    public WocsclassPersistence(Connection connection) {
	this.connection = connection;
    }

    public void save(Wocsclass wocsClass) {

	String executionID = "''";
	if (wocsClass.getExecution() != null)
	    executionID = wocsClass.getExecution().getId();

	StringBuilder query = new StringBuilder();

	query.append("insert into WocsclassMetrics (wocsClass, execution_id, is_all, experiement_id, id_solution) values (");
	query.append(wocsClass.getWocsClass());
	query.append(",");
	query.append(executionID);
	query.append(",");
	if (wocsClass.getExecution() == null)
	    query.append("1");
	else
	    query.append("0");
	query.append(",");
	query.append(wocsClass.getExperiement().getId());
	query.append(",");
	query.append(wocsClass.getIdSolution());
	query.append(")");

	try {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(query.toString());
	} catch (SQLException ex) {
	    Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
