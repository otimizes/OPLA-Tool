package br.ufpr.dinf.gres.domain.entity.metric;

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

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

@Entity
@Table(name = "elegance_metrics")
public class EleganceMetric implements GenericMetric {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "nac")
    private Double nac;

    @Column(name = "atmr")
    private Double atmr;

    @Column(name = "ec")
    private Double ec;

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

    @Column(name = "elegance")
    private Double elegance;

    public EleganceMetric(String idSolution, Execution execution, Experiment experiement) {
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getNac() {
        return nac;
    }

    public void setNac(Double nac) {
        this.nac = nac;
    }

    public Double getAtmr() {
        return atmr;
    }

    public void setAtmr(Double atmr) {
        this.atmr = atmr;
    }

    public Double getEc() {
        return ec;
    }

    public void setEc(Double ec) {
        this.ec = ec;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public Double getElegance() {
        return elegance;
    }

    public void setElegance(Double elegance) {
        this.elegance = elegance;
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
        EleganceMetric castOther = (EleganceMetric) other;
        return Objects.equals(id, castOther.id) && Objects.equals(nac, castOther.nac)
                && Objects.equals(atmr, castOther.atmr) && Objects.equals(ec, castOther.ec)
                && Objects.equals(execution, castOther.execution) && Objects.equals(elegance, castOther.elegance)
                && Objects.equals(experiment, castOther.experiment) && Objects.equals(isAll, castOther.isAll)
                && Objects.equals(idSolution, castOther.idSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nac, atmr, ec, execution, elegance, experiment, isAll, idSolution);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id).append("nac", nac)
                .append("atmr", atmr).append("ec", ec).append("executionId", execution).append("elegance", elegance)
                .append("experiementId", experiment).append("isAll", isAll).append("idSolution", idSolution)
                .toString();
    }

}
