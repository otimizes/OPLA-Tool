package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.metrics.Elegance;
import br.ufpr.dinf.gres.core.jmetal4.metrics.Svc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SvcPersistence {

    private Connection connection;

    public SvcPersistence(Connection connection) {
	this.connection = connection;
    }

    public void save(Svc sVc) {

	String executionID = "null";
	if (sVc.getExecutionResults() != null)
	    executionID = sVc.getExecutionResults().getId();

	StringBuilder query = new StringBuilder();

	query.append("insert into SvcMetrics (sVc, execution_id, is_all, experiment_id, id_solution) values (");
	query.append(sVc.getSvc());
	query.append(",");
	query.append(executionID);
	query.append(",");
	if (sVc.getExecutionResults() == null)
	    query.append("1");
	else
	    query.append("0");
	query.append(",");
	query.append(sVc.getExperiement().getId());
	query.append(",");
	query.append(sVc.getIdSolution());
	query.append(")");

	try {
	    Statement statement = connection.createStatement();
	    statement.executeUpdate(query.toString());
	} catch (SQLException ex) {
	    Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
