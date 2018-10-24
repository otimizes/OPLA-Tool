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
@Table(name = "EleganceMetrics")
public class EleganceMetric implements GenericMetric {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nac")
    private String nac;

    @Column(name = "atmr")
    private String atmr;

    @Column(name = "ec")
    private String ec;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "execution_id")
    private Execution execution;

    @Column(name = "elegance")
    private String elegance;

    @ManyToOne
    @JoinColumn(name = "experiement_id", nullable = false)
    private Experiment experiement;

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

    public String getNac() {
        return nac;
    }

    public void setNac(String nac) {
        this.nac = nac;
    }

    public String getAtmr() {
        return atmr;
    }

    public void setAtmr(String atmr) {
        this.atmr = atmr;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    public String getElegance() {
        return elegance;
    }

    public void setElegance(String elegance) {
        this.elegance = elegance;
    }

    public Experiment getExperiement() {
        return experiement;
    }

    public void setExperiement(Experiment experiement) {
        this.experiement = experiement;
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
                && Objects.equals(experiement, castOther.experiement) && Objects.equals(isAll, castOther.isAll)
                && Objects.equals(idSolution, castOther.idSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nac, atmr, ec, execution, elegance, experiement, isAll, idSolution);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id).append("nac", nac)
                .append("atmr", atmr).append("ec", ec).append("executionId", execution).append("elegance", elegance)
                .append("experiementId", experiement).append("isAll", isAll).append("idSolution", idSolution)
                .toString();
    }

}
