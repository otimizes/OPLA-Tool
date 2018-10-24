/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package results;

import utils.Id;

/**
 * @author elf
 */
public class InfoResult {

    private String id;
    private int isAll; // informa se é referente os dados de todas as rodadas
    private String name; // INFO_PLANAME_N (where n is number of run)
    private Execution execution;
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
    private Experiment experiement;

    public InfoResult() {
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

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public int getIsAll() {
        return this.isAll;
    }

    public void setIsAll(int i) {
        this.isAll = i;
    }

    public Experiment getExperiement() {
        return this.experiement;
    }

    public void setExperiement(Experiment experiement) {
        this.experiement = experiement;
    }

}