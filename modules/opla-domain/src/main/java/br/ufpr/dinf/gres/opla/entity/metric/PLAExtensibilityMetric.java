package br.ufpr.dinf.gres.opla.entity.metric;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import br.ufpr.dinf.gres.opla.entity.Execution;
import br.ufpr.dinf.gres.opla.entity.Experiment;

@Entity
@Table(name = "PLAExtensibilityMetrics")
public class PLAExtensibilityMetric implements GenericMetric {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plaExtensibility")
    private String plaExtensibility;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "execution_id")
    private Execution execution;

    @Column(name = "is_all")
    private Integer isAll;

    @ManyToOne
    @JoinColumn(name = "experiement_id", nullable = false)
    private Experiment experiment;

    @Column(name = "id_solution")
    private String idSolution;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaExtensibility() {
        return plaExtensibility;
    }

    public void setPlaExtensibility(String plaExtensibility) {
        this.plaExtensibility = plaExtensibility;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public Integer getIsAll() {
        return isAll;
    }

    public void setIsAll(Integer isAll) {
        this.isAll = isAll;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public String getIdSolution() {
        return idSolution;
    }

    public void setIdSolution(String idSolution) {
        this.idSolution = idSolution;
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
        PLAExtensibilityMetric castOther = (PLAExtensibilityMetric) other;
        return Objects.equals(id, castOther.id) && Objects.equals(plaExtensibility, castOther.plaExtensibility)
                && Objects.equals(execution, castOther.execution) && Objects.equals(isAll, castOther.isAll)
                && Objects.equals(experiment, castOther.experiment) && Objects.equals(idSolution, castOther.idSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, plaExtensibility, execution, isAll, experiment, idSolution);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id)
                .append("plaExtensibility", plaExtensibility).append("executionId", execution).append("isAll", isAll)
                .append("experimentId", experiment).append("idSolution", idSolution).toString();
    }

}
