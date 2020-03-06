/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpr.dinf.gres.core.jmetal4.results;

import br.ufpr.dinf.gres.core.jmetal4.util.Id;
import br.ufpr.dinf.gres.core.persistence.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.Info;

/**
 * @author elf
 */
public class InfoResults implements IPersistentDto<Info> {

    private String id;
    private int isAll; // informa se é referente os dados de todas as rodadas
    private String name; // INFO_PLANAME_N (where n is number of run)
    private ExecutionResults executionResults;
    private String listOfConcerns; //separated by pipe |
    private Integer numberOfPackages;
    private Integer numberOfVariabilities;
    private Integer numberOfClasses;
    private Integer numberOfInterfaces;
    private Integer numberOfDependencies;
    private Integer numberOfAbstraction;
    private Integer numberOfAssociations;
    private Integer numberOfGeneralizations;
    private Integer numberOfassociationsClass;
    private Integer userEvaluation;
    private String freezedElements;
    private ExperimentResults experiement;
    private String objectives;

    public InfoResults() {
        this.id = Id.generateUniqueId();
    }

    public String getListOfConcerns() {
        return listOfConcerns;
    }

    /**
     * String listOfConcerns should be a string of values separated with pipes |.
     * <p>
     * Ex: concern1 | concern2 | concerns3
     *
     * @param listOfConcerns
     */
    public void setListOfConcerns(String listOfConcerns) {
        this.listOfConcerns = listOfConcerns;
    }

    public Integer getNumberOfPackages() {
        return numberOfPackages;
    }

    public void setNumberOfPackages(Integer numberOfPackages) {
        this.numberOfPackages = numberOfPackages;
    }

    public Integer getNumberOfVariabilities() {
        return numberOfVariabilities;
    }

    public void setNumberOfVariabilities(Integer numberOfVariabilities) {
        this.numberOfVariabilities = numberOfVariabilities;
    }

    public Integer getNumberOfClasses() {
        return numberOfClasses;
    }

    public void setNumberOfClasses(Integer numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public Integer getNumberOfInterfaces() {
        return numberOfInterfaces;
    }

    public void setNumberOfInterfaces(Integer numberOfInterfaces) {
        this.numberOfInterfaces = numberOfInterfaces;
    }

    public Integer getNumberOfDependencies() {
        return numberOfDependencies;
    }

    public void setNumberOfDependencies(Integer numberOfDependencies) {
        this.numberOfDependencies = numberOfDependencies;
    }

    public Integer getNumberOfAbstraction() {
        return numberOfAbstraction;
    }

    public void setNumberOfAbstraction(Integer numberOfAbstraction) {
        this.numberOfAbstraction = numberOfAbstraction;
    }

    public Integer getNumberOfAssociations() {
        return numberOfAssociations;
    }

    public void setNumberOfAssociations(Integer numberOfAssociations) {
        this.numberOfAssociations = numberOfAssociations;
    }

    public Integer getNumberOfGeneralizations() {
        return numberOfGeneralizations;
    }

    public void setNumberOfGeneralizations(Integer numberOfGeneralizations) {
        this.numberOfGeneralizations = numberOfGeneralizations;
    }

    public Integer getNumberOfassociationsClass() {
        return numberOfassociationsClass;
    }

    public void setNumberOfassociationsClass(Integer numberOfassociationsClass) {
        this.numberOfassociationsClass = numberOfassociationsClass;
    }

    public String getName() {
        return name;
    }

    /**
     * name must be "plaName_runNumber"
     * <p>
     * Then his method will set name
     * like: INFO_plaName_runNumber
     *
     * @param name
     */
    public void setName(String name) {
        this.name = "INFO_" + name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExecutionResults getExecutionResults() {
        return executionResults;
    }

    public void setExecutionResults(ExecutionResults executionResults) {
        this.executionResults = executionResults;
    }

    public int getIsAll() {
        return this.isAll;
    }

    public void setIsAll(int i) {
        this.isAll = i;
    }

    public ExperimentResults getExperiement() {
        return this.experiement;
    }

    public void setExperiement(ExperimentResults experiement) {
        this.experiement = experiement;
    }

    public Integer getUserEvaluation() {
        return userEvaluation;
    }

    public void setUserEvaluation(Integer userEvaluation) {
        this.userEvaluation = userEvaluation;
    }

    public String getFreezedElements() {
        return freezedElements;
    }

    public void setFreezedElements(String freezedElements) {
        this.freezedElements = freezedElements;
    }

    @Override
    public String toString() {
        return "InfoResult{" +
                "\nid='" + id + '\'' +
                ", \nisAll=" + isAll +
                ", \nname='" + name + '\'' +
                ", \nlistOfConcerns='" + listOfConcerns + '\'' +
                ", \nnumberOfPackages=" + numberOfPackages +
                ", \nnumberOfVariabilities=" + numberOfVariabilities +
                ", \nnumberOfClasses=" + numberOfClasses +
                ", \nnumberOfInterfaces=" + numberOfInterfaces +
                ", \nnumberOfDependencies=" + numberOfDependencies +
                ", \nnumberOfAbstraction=" + numberOfAbstraction +
                ", \nnumberOfAssociations=" + numberOfAssociations +
                ", \nnumberOfGeneralizations=" + numberOfGeneralizations +
                ", \nnumberOfassociationsClass=" + numberOfassociationsClass +
                "\n}";
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    @Override
    public Info newPersistentInstance() {
        Info info = new Info();
        info.setNumberOfAssociationsClass(this.getNumberOfassociationsClass());
        info.setNumberOfAssociations(this.getNumberOfAssociations());
        info.setNumberOfAbstractions(this.getNumberOfAbstraction());
        info.setExperiment(this.getExperiement().newPersistentInstance());
        if (this.getExecutionResults() != null) info.setExecution(this.getExecutionResults().newPersistentInstance());
        info.setFreezedElements(this.getFreezedElements());
        info.setId(Long.valueOf(this.getId()));
        info.setIsAll(this.getIsAll());
        info.setName(this.getName());
        info.setUserEvaluation(this.getUserEvaluation());
        info.setListOfConcerns(this.getListOfConcerns());
        info.setNumberOfClasses(this.getNumberOfClasses());
        info.setNumberOfDependencies(this.getNumberOfDependencies());
        info.setNumberOfVariabilities(this.getNumberOfVariabilities());
        info.setNumberOfPackages(this.getNumberOfPackages());
        info.setNumberOfInterfaces(this.getNumberOfInterfaces());
        info.setObjectives(this.getObjectives());
        info.setNumberOfGeneralizations(this.getNumberOfGeneralizations());
        info.setNumberOfVariabilities(this.getNumberOfVariabilities());
        return info;
    }

}