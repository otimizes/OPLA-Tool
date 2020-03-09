package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "acomp_metrics")
public class AcompMetric implements GenericMetric {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sumdepin")
    private Double sumDepIn;
    @Column(name = "sumdepout")
    private Double sumDepOut;

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

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Double getSumDepIn() {
        return sumDepIn;
    }

    public void setSumDepIn(Double sumDepIn) {
        this.sumDepIn = sumDepIn;
    }

    public Double getSumDepOut() {
        return sumDepOut;
    }

    public void setSumDepOut(Double sumDepOut) {
        this.sumDepOut = sumDepOut;
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
        AcompMetric castOther = (AcompMetric) other;
        return Objects.equals(id, castOther.id) && Objects.equals(execution, castOther.execution)
                && Objects.equals(sumDepIn, castOther.sumDepIn)
                && Objects.equals(sumDepOut, castOther.sumDepOut)
                && Objects.equals(experiment, castOther.experiment) && Objects.equals(isAll, castOther.isAll)
                && Objects.equals(idSolution, castOther.idSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sumDepIn, sumDepOut, execution, experiment, isAll, idSolution);
    }


}
