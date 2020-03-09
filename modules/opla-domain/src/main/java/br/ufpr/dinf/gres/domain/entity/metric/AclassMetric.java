package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "aclass_metrics")
public class AclassMetric implements GenericMetric {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "sumclassdepin")
    private Double sumClassesDepIn;
    @Column(name = "sumclassdepout")
    private Double sumClassesDepOut;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "execution_id")
    private Execution execution;

    @ManyToOne
    @JoinColumn(name = "experiement_id", nullable = false)
    private Experiment experiment;

    @Column(name = "is_all")
    private Integer isAll;

    @Column(name = "id_solution")
    private String idSolution;

    public AclassMetric(String idSolution, Execution execution, Experiment experiement) {
        this.idSolution = idSolution;
        this.execution = execution;
        this.experiment = experiement;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Execution getExecution() {
        return execution;
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getSumClassesDepIn() {
        return sumClassesDepIn;
    }

    public void setSumClassesDepIn(Double sumClassesDepIn) {
        this.sumClassesDepIn = sumClassesDepIn;
    }

    public Double getSumClassesDepOut() {
        return sumClassesDepOut;
    }

    public void setSumClassesDepOut(Double sumClassesDepOut) {
        this.sumClassesDepOut = sumClassesDepOut;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
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
        AclassMetric castOther = (AclassMetric) other;
        return Objects.equals(id, castOther.id) && Objects.equals(execution, castOther.execution)
                && Objects.equals(sumClassesDepIn, castOther.sumClassesDepIn)
                && Objects.equals(sumClassesDepOut, castOther.sumClassesDepOut)
                && Objects.equals(experiment, castOther.experiment) && Objects.equals(isAll, castOther.isAll)
                && Objects.equals(idSolution, castOther.idSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sumClassesDepIn, sumClassesDepOut, execution, experiment, isAll, idSolution);
    }


}
