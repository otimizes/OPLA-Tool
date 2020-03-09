package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "feature_driven_metrics")
public class FeatureDrivenMetric implements GenericMetric {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "msiAggregation")
    private Double msiAggregation;

    @Column(name = "cdac")
    private Double cdac;

    @Column(name = "cdai")
    private Double cdai;

    @Column(name = "cdao")
    private Double cdao;

    @Column(name = "cibc")
    private Double cibc;

    @Column(name = "iibc")
    private Double iibc;

    @Column(name = "oobc")
    private Double oobc;

    @Column(name = "lcc")
    private Double lcc;

    @Column(name = "lccClass")
    private Double lccClass;

    @Column(name = "cdaClass")
    private Double cdaClass;

    @Column(name = "cibClass")
    private Double cibClass;

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

    public FeatureDrivenMetric(String idSolution, Execution executionResults, Experiment experiement) {
        this.idSolution = idSolution;
        this.execution = executionResults;
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

    public Double getMsiAggregation() {
        return msiAggregation;
    }

    public void setMsiAggregation(Double msiAggregation) {
        this.msiAggregation = msiAggregation;
    }

    public Double getCdac() {
        return cdac;
    }

    public void setCdac(Double cdac) {
        this.cdac = cdac;
    }

    public Double getCdai() {
        return cdai;
    }

    public void setCdai(Double cdai) {
        this.cdai = cdai;
    }

    public Double getCdao() {
        return cdao;
    }

    public void setCdao(Double cdao) {
        this.cdao = cdao;
    }

    public Double getCibc() {
        return cibc;
    }

    public void setCibc(Double cibc) {
        this.cibc = cibc;
    }

    public Double getIibc() {
        return iibc;
    }

    public void setIibc(Double iibc) {
        this.iibc = iibc;
    }

    public Double getOobc() {
        return oobc;
    }

    public void setOobc(Double oobc) {
        this.oobc = oobc;
    }

    public Double getLcc() {
        return lcc;
    }

    public void setLcc(Double lcc) {
        this.lcc = lcc;
    }

    public Double getLccClass() {
        return lccClass;
    }

    public void setLccClass(Double lccClass) {
        this.lccClass = lccClass;
    }

    public Double getCdaClass() {
        return cdaClass;
    }

    public void setCdaClass(Double cdaClass) {
        this.cdaClass = cdaClass;
    }

    public Double getCibClass() {
        return cibClass;
    }

    public void setCibClass(Double cibClass) {
        this.cibClass = cibClass;
    }

    public Execution getExecutionId() {
        return execution;
    }

    public void setExecutionId(Execution execution) {
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

    public Execution getExecution() {
        return execution;
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
        FeatureDrivenMetric castOther = (FeatureDrivenMetric) other;
        return Objects.equals(id, castOther.id) && Objects.equals(msiAggregation, castOther.msiAggregation)
                && Objects.equals(cdac, castOther.cdac) && Objects.equals(cdai, castOther.cdai)
                && Objects.equals(cdao, castOther.cdao) && Objects.equals(cibc, castOther.cibc)
                && Objects.equals(iibc, castOther.iibc) && Objects.equals(oobc, castOther.oobc)
                && Objects.equals(lcc, castOther.lcc) && Objects.equals(lccClass, castOther.lccClass)
                && Objects.equals(cdaClass, castOther.cdaClass) && Objects.equals(cibClass, castOther.cibClass)
                && Objects.equals(execution, castOther.execution) && Objects.equals(isAll, castOther.isAll)
                && Objects.equals(experiment, castOther.experiment)
                && Objects.equals(idSolution, castOther.idSolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, msiAggregation, cdac, cdai, cdao, cibc, iibc, oobc, lcc, lccClass, cdaClass, cibClass,
                execution, isAll, experiment, idSolution);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append("id", id)
                .append("msiAggregation", msiAggregation).append("cdac", cdac).append("cdai", cdai).append("cdao", cdao)
                .append("cibc", cibc).append("iibc", iibc).append("oobc", oobc).append("lcc", lcc)
                .append("lccClass", lccClass).append("cdaClass", cdaClass).append("cibClass", cibClass)
                .append("executionId", execution).append("isAll", isAll).append("experiementId", experiment)
                .append("idSolution", idSolution).toString();
    }

}
