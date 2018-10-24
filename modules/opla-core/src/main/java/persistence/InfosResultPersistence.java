/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package persistence;

import results.InfoResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author elf
 */
public class InfosResultPersistence {

    private final Connection connection;

    public InfosResultPersistence(Connection connection) {
        this.connection = connection;
    }

    public void persistInfoDatas(InfoResult fakeInfoResult) throws SQLException {
        StringBuilder query = new StringBuilder();

        String executionId = "''";
        if (fakeInfoResult.getExecution() != null)
            executionId = fakeInfoResult.getExecution().getId();

        query.append("insert into infos(id, execution_id, name, list_of_concerns, number_of_packages, number_of_variabilities, number_of_interfaces, number_of_classes, number_of_dependencies,");
        query.append(" number_of_abstractions, number_of_generalizations, number_of_associations, number_of_associations_class, is_all, experiement_id) values (");
        query.append(fakeInfoResult.getId());
        query.append(",");
        query.append(executionId);
        query.append(",");
        query.append("'");
        query.append(fakeInfoResult.getName());
        query.append("'");
        query.append(",");
        query.append("'");
        query.append(fakeInfoResult.getListOfConcerns());
        query.append("'");
        query.append(",");
        query.append(fakeInfoResult.getNumberOfPackages());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfVariabilities());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfInterfaces());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfClasses());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfDependencies());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfAbstraction());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfGeneralizations());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfAssociations());
        query.append(",");
        query.append(fakeInfoResult.getNumberOfassociationsClass());
        query.append(",");
        query.append(fakeInfoResult.getIsAll());
        query.append(",");
        query.append(fakeInfoResult.getExperiement().getId());
        query.append(")");

        Statement statement = connection.createStatement();
        statement.executeUpdate(query.toString());
    }

}
