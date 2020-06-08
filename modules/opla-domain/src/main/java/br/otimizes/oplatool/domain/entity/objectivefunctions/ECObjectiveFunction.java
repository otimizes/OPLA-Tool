package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ec_obj")
public class ECObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "cibc")
    private Double cibc;
    @Column(name = "iibc")
    private Double iibc;
    @Column(name = "oobc")
    private Double oobc;

    public ECObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

}
