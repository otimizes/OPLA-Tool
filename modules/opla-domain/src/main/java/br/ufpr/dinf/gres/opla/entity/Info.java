package br.ufpr.dinf.gres.opla.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "infos")
public class Info implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "execution_id", nullable = false)
    private Execution execution;

    @Column(name = "list_of_concerns", columnDefinition = "TEXT")
    private String listOfConcerns;

    @Column(name = "number_of_packages")
    private Integer numberOfPackages;

    @Column(name = "number_of_variabilities")
    private Integer numberOfVariabilities;

    @Column(name = "number_of_interfaces")
    private Integer numberOfInterfaces;

    @Column(name = "number_of_dependencies")
    private Integer numberOfDependencies;

    @Column(name = "number_of_abstractions")
    private Integer numberOfAbstractions;

    @Column(name = "number_of_generalizations")
    private Integer numberOfGeneralizations;

    @Column(name = "number_of_associations")
    private Integer numberOfAssociations;

    @Column(name = "number_of_associations_class")
    private Integer numberOfAssociationsClass;

    @Column(name = "name")
    private String name;

    @Column(name = "is_all")
    private Integer isAll;

    @ManyToOne
    @JoinColumn(name = "experiement_id", nullable = false)
    private Experiment experiement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public String getListOfConcerns() {
        return listOfConcerns;
    }

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

    public Integer getNumberOfAbstractions() {
        return numberOfAbstractions;
    }

    public void setNumberOfAbstractions(Integer numberOfAbstractions) {
        this.numberOfAbstractions = numberOfAbstractions;
    }

    public Integer getNumberOfGeneralizations() {
        return numberOfGeneralizations;
    }

    public void setNumberOfGeneralizations(Integer numberOfGeneralizations) {
        this.numberOfGeneralizations = numberOfGeneralizations;
    }

    public Integer getNumberOfAssociations() {
        return numberOfAssociations;
    }

    public void setNumberOfAssociations(Integer numberOfAssociations) {
        this.numberOfAssociations = numberOfAssociations;
    }

    public Integer getNumberOfAssociationsClass() {
        return numberOfAssociationsClass;
    }

    public void setNumberOfAssociationsClass(Integer numberOfAssociationsClass) {
        this.numberOfAssociationsClass = numberOfAssociationsClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }

    public Experiment getExperiement() {
        return experiement;
    }

    public void setExperiement(Experiment experiement) {
        this.experiement = experiement;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!getClass().equals(other.getClass())) {
            return false;
        }
        Info castOther = (Info) other;
        return Objects.equals(id, castOther.id) && Objects.equals(execution, castOther.execution)
                && Objects.equals(listOfConcerns, castOther.listOfConcerns)
                && Objects.equals(numberOfPackages, castOther.numberOfPackages)
                && Objects.equals(numberOfVariabilities, castOther.numberOfVariabilities)
                && Objects.equals(numberOfInterfaces, castOther.numberOfInterfaces)
                && Objects.equals(numberOfDependencies, castOther.numberOfDependencies)
                && Objects.equals(numberOfAbstractions, castOther.numberOfAbstractions)
                && Objects.equals(numberOfGeneralizations, castOther.numberOfGeneralizations)
                && Objects.equals(numberOfAssociations, castOther.numberOfAssociations)
                && Objects.equals(numberOfAssociationsClass, castOther.numberOfAssociationsClass)
                && Objects.equals(name, castOther.name) && Objects.equals(isAll, castOther.isAll)
                && Objects.equals(experiement, castOther.experiement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, execution, listOfConcerns, numberOfPackages, numberOfVariabilities, numberOfInterfaces,
                numberOfDependencies, numberOfAbstractions, numberOfGeneralizations, numberOfAssociations,
                numberOfAssociationsClass, name, isAll, experiement);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id).append("execution", execution)
                .append("listOfConcerns", listOfConcerns).append("numberOfPackages", numberOfPackages)
                .append("numberOfVariabilities", numberOfVariabilities).append("numberOfInterfaces", numberOfInterfaces)
                .append("numberOfDependencies", numberOfDependencies)
                .append("numberOfAbstractions", numberOfAbstractions)
                .append("numberOfGeneralizations", numberOfGeneralizations)
                .append("numberOfAssociations", numberOfAssociations)
                .append("numberOfAssociationsClass", numberOfAssociationsClass).append("name", name)
                .append("isAll", isAll).append("experiement", experiement).toString();
    }

}
