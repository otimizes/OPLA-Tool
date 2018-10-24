/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistence;

import results.FunResults;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author elf
 */
public class FunsResultPersistence {

    private final Connection connection;


    public FunsResultPersistence(Connection connection) {
        this.connection = connection;
    }

    public void persistFunsDatas(FunResults funs) throws SQLException {
        StringBuilder query = new StringBuilder();

        String executionId = "''";
        if (funs.getExecution() != null)
            executionId = funs.getExecution().getId();

        query.append("insert into objectives (id, execution_id, objectives, is_all, experiement_id, solution_name) values (");
        query.append(funs.getId());
        query.append(",");
        query.append(executionId);
        query.append(",'");
        query.append(funs.getObjectives());
        query.append("',");
        query.append(funs.getIsAll());
        query.append(",");
        query.append(funs.getExperiement().getId());
        query.append(",'");
        query.append(funs.getSolution_name());
        query.append("')");
        Statement statement = connection.createStatement();

        statement.executeUpdate(query.toString());
    }

}
