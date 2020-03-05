/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.results.InfoResults;

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

    public void persistInfoDatas(InfoResults fakeInfoResults) throws SQLException {
        StringBuilder query = new StringBuilder();

        String executionId = "null";
        if (fakeInfoResults.getExecutionResults() != null)
            executionId = fakeInfoResults.getExecutionResults().getId();

        query.append("insert into infos(id, execution_id, name, list_of_concerns, number_of_packages, number_of_variabilities, number_of_interfaces, number_of_classes, number_of_dependencies,");
        query.append(" number_of_abstractions, number_of_generalizations, number_of_associations, number_of_associations_class, is_all, experiement_id, user_evaluation, freezed_elements, objectives) values (");
        query.append(fakeInfoResults.getId());
        query.append(",");
        query.append(executionId);
        query.append(",");
        query.append("'");
        query.append(fakeInfoResults.getName());
        query.append("'");
        query.append(",");
        query.append("'");
        query.append(fakeInfoResults.getListOfConcerns());
        query.append("'");
        query.append(",");
        query.append(fakeInfoResults.getNumberOfPackages());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfVariabilities());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfInterfaces());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfClasses());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfDependencies());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfAbstraction());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfGeneralizations());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfAssociations());
        query.append(",");
        query.append(fakeInfoResults.getNumberOfassociationsClass());
        query.append(",");
        query.append(fakeInfoResults.getIsAll());
        query.append(",");
        query.append(fakeInfoResults.getExperiement().getId());
        query.append(",");
        query.append(fakeInfoResults.getUserEvaluation());
        query.append(",'");
        query.append(fakeInfoResults.getFreezedElements());
        query.append("','");
        query.append(fakeInfoResults.getObjectives());
        query.append("')");

        Statement statement = connection.createStatement();
        statement.executeUpdate(query.toString());
    }

}
