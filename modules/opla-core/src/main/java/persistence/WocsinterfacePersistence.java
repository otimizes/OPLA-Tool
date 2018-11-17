package persistence;

import metrics.Elegance;
import metrics.Wocsinterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WocsinterfacePersistence {

    private Connection connection;

    public WocsinterfacePersistence(Connection connection) {
	this.connection = connection;
    }

    public void save(Wocsinterface wocsInterface) {

	String executionID = "''";
	if (wocsInterface.getExecution() != null)
	    executionID = wocsInterface.getExecution().getId();

	StringBuilder query = new StringBuilder();

	query.append("insert into WocsinterfaceMetrics (wocsInterface, execution_id, is_all, experiement_id, id_solution) values (");
	query.append(wocsInterface.getWocsInterface());
	query.append(",");
	query.append(executionID);
	query.append(",");
	if (wocsInterface.getExecution() == null)
	    query.append("1");
	else
	    query.append("0");
	query.append(",");
	query.append(wocsInterface.getExperiement().getId());
	query.append(",");
	query.append(wocsInterface.getIdSolution());
	query.append(")");

	try {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(query.toString());
	} catch (SQLException ex) {
	    Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
