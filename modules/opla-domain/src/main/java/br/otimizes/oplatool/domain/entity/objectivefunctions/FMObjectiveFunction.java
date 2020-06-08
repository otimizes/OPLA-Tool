package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fm_obj")
public class FMObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "msi_aggregation")
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

    @Column(name = "lcc_class")
    private Double lccClass;

    @Column(name = "cda_class")
    private Double cdaClass;

    @Column(name = "cib_class")
    private Double cibClass;

    public FMObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
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

}
