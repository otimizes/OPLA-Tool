/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpr.dinf.gres.core.jmetal4.results;

import br.ufpr.dinf.gres.core.jmetal4.database.Database;
import br.ufpr.dinf.gres.core.jmetal4.util.Id;
import br.ufpr.dinf.gres.core.persistence.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Classe que representa um experiemento.
 *
 * @author elf
 */
public class ExperimentResults implements IPersistentDto<Experiment> {

    private String id;
    private String name;
    private String description;
    private String algorithm;
    private String createdAt;
    private String hash;
    private Collection<ExecutionResults> executionResults;

    /**
     * Use this if you don't cares about id generated. Otherwise use setters
     *
     * @param name
     * @param description
     * @throws Exception
     */
    public ExperimentResults(String name, String algorithm, String description) throws Exception {
        this.name = name;
        this.algorithm = algorithm;
        this.description = description;
        this.id = givenId();
        this.createdAt = setCreatedAt();
    }

    public ExperimentResults(String name, String algorithm, String description, String hash) throws Exception {
        this.name = name;
        this.algorithm = algorithm;
        this.description = description;
        this.id = givenId();
        this.hash = hash;
        this.createdAt = setCreatedAt();
    }

    private static Integer getResultParseInteger(ResultSet resultSetInfos, String nameColumn) {
        try {
            return Integer.parseInt(resultSetInfos.getString(nameColumn));
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Double getResultParseDouble(ResultSet resultSetConventional, String nameColumn) {
        try {
            return Double.parseDouble(resultSetConventional.getString(nameColumn));
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlgorithmAndDescription() {
        if ("null".equals(this.description))
            return this.getAlgorithm();

        StringBuilder sb = new StringBuilder();
        sb.append(this.getAlgorithm());
        sb.append(" (");
        sb.append(this.description);
        sb.append(")");

        return sb.toString();
    }

    private String makeQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into experiments (id, name, algorithm, description, created_at, hash) ");
        sb.append("values (");
        sb.append(this.id);
        sb.append(",'");
        sb.append(this.name);
        sb.append("','");
        sb.append(this.algorithm);
        sb.append("','");
        sb.append(this.description);
        sb.append("','");
        sb.append(this.getCreatedAt());
        sb.append("','");
        sb.append(this.hash);
        sb.append("')");
        return sb.toString();
    }

    private String givenId() {
        return Id.generateUniqueId();
    }

    public void save() throws Exception {
        Statement statement = Database.getConnection().createStatement();
        statement.executeUpdate(makeQuery());
        statement.close();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String setCreatedAt() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dt.format(new Date()).toString();
    }

    public Collection<ExecutionResults> getExecutionResults() {
        return executionResults;
    }

    public void setExecutionResults(Collection<ExecutionResults> execs) {
        this.executionResults = execs;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public Experiment newPersistentInstance(IPersistentDto persistentDto) {
        return null;
    }
}
