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
@Table(name = "FeatureDrivenMetrics")
public class FeatureDrivenMetric implements GenericMetric {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "msiAggregation")
    private String msiAggregation;

    @Column(name = "cdac")
    private String cdac;

    @Column(name = "cdai")
    private String cdai;

    @Column(name = "cdao")
    private String cdao;

    @Column(name = "cibc")
    private String cibc;

    @Column(name = "iibc")
    private String iibc;

    @Column(name = "oobc")
    private String oobc;

    @Column(name = "lcc")
    private String lcc;

    @Column(name = "lccClass")
    private String lccClass;

    @Column(name = "cdaClass")
    private String cdaClass;

    @Column(name = "cibClass")
    private String cibClass;

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

    public String getMsiAggregation() {
        return msiAggregation;
    }

    public void setMsiAggregation(String msiAggregation) {
        this.msiAggregation = msiAggregation;
    }

    public String getCdac() {
        return cdac;
    }

    public void setCdac(String cdac) {
        this.cdac = cdac;
    }

    public String getCdai() {
        return cdai;
    }

    public void setCdai(String cdai) {
        this.cdai = cdai;
    }

    public String getCdao() {
        return cdao;
    }

    public void setCdao(String cdao) {
        this.cdao = cdao;
    }

    public String getCibc() {
        return cibc;
    }

    public void setCibc(String cibc) {
        this.cibc = cibc;
    }

    public String getIibc() {
        return iibc;
    }

    public void setIibc(String iibc) {
        this.iibc = iibc;
    }

    public String getOobc() {
        return oobc;
    }

    public void setOobc(String oobc) {
        this.oobc = oobc;
    }

    public String getLcc() {
        return lcc;
    }

    public void setLcc(String lcc) {
        this.lcc = lcc;
    }

    public String getLccClass() {
        return lccClass;
    }

    public void setLccClass(String lccClass) {
        this.lccClass = lccClass;
    }

    public String getCdaClass() {
        return cdaClass;
    }

    public void setCdaClass(String cdaClass) {
        this.cdaClass = cdaClass;
    }

    public String getCibClass() {
        return cibClass;
    }

    public void setCibClass(String cibClass) {
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
