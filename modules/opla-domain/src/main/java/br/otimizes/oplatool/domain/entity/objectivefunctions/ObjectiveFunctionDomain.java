package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public class ObjectiveFunctionDomain implements GenericMetric {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "execution_id")
    private Execution execution;
    @ManyToOne
    @JoinColumn(name = "experiment_id", nullable = false)
    private Experiment experiment;
    @Column(name = "is_all")
    private Integer isAll;
    @Column(name = "id_solution")
    private String idSolution;

    @Override
    public String getId() {
        return this.id;
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

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }

    public String getIdSolution() {
        return idSolution;
    }

    public void setIdSolution(String idSolution) {
        this.idSolution = idSolution;
    }

    public ObjectiveFunctionDomain(String idSolution, Execution execution, Experiment experiment) {
        this.idSolution = idSolution;
        this.execution = execution;
        this.experiment = experiment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectiveFunctionDomain that = (ObjectiveFunctionDomain) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(execution, that.execution) &&
                Objects.equals(experiment, that.experiment) &&
                Objects.equals(isAll, that.isAll) &&
                Objects.equals(idSolution, that.idSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, execution, experiment, isAll, idSolution);
    }

    @Override
    public String toString() {
        return "BaseMetric{" +
                "id='" + id + '\'' +
                ", execution=" + execution +
                ", experiment=" + experiment +
                ", isAll=" + isAll +
                ", idSolution='" + idSolution + '\'' +
                '}';
    }
}