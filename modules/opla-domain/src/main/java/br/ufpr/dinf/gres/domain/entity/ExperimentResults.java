/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpr.dinf.gres.domain.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
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
     */
    public ExperimentResults(String name, String algorithm, String description) {
        this.name = name;
        this.algorithm = algorithm;
        this.description = description;
        this.id = givenId();
        this.createdAt = setCreatedAt();
    }

    public ExperimentResults(String name, String algorithm, String description, String hash) {
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

    private String givenId() {
        return Id.generateUniqueId();
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
    public Experiment newPersistentInstance() {
        Experiment experiment = new Experiment();
        experiment.setAlgorithm(this.getAlgorithm());
        experiment.setCreatedAt(this.getCreatedAt());
        experiment.setDescription(this.getDescription());
        experiment.setHash(this.getHash());
        experiment.setId(Long.valueOf(this.getId()));
        experiment.setName(this.getName());
        return experiment;
    }
}