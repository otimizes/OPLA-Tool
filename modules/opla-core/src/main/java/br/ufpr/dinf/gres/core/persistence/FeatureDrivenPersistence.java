package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.metrics.Elegance;
import br.ufpr.dinf.gres.core.jmetal4.metrics.FeatureDriven;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeatureDrivenPersistence {

    private Connection connection;

    public FeatureDrivenPersistence(Connection connection) {
        this.connection = connection;
    }

    public void save(FeatureDriven fd) {
        String executionID = "null";
        if (fd.getExecutionResults() != null)
            executionID = fd.getExecutionResults().getId();

        StringBuilder query = new StringBuilder();
        query.append("insert into FeatureDrivenMetrics (msiAggregation, cdac, cdai, cdao, cibc, iibc, oobc, lcc, lccClass, cdaClass, cibClass, execution_id, is_all, experiement_id, id_solution) values (");
        query.append(fd.getMsiAggregation());
        query.append(",");
        query.append(fd.getCdac());
        query.append(",");
        query.append(fd.getCdai());
        query.append(",");
        query.append(fd.getCdao());
        query.append(",");
        query.append(fd.getCibc());
        query.append(",");
        query.append(fd.getIibc());
        query.append(",");
        query.append(fd.getOobc());
        query.append(",");
        query.append(fd.getLcc());
        query.append(",");
        query.append(fd.getLccClass());
        query.append(",");
        query.append(fd.getCdaClass());
        query.append(",");
        query.append(fd.getCibClass());
        query.append(",");
        query.append(executionID);
        query.append(",");
        if (fd.getExecutionResults() == null)
            query.append("1");
        else
            query.append("0");
        query.append(",");
        query.append(fd.getExperiement().getId());
        query.append(",");
        query.append(fd.getIdSolution());
        query.append(")");

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Elegance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
