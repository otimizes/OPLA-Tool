package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "eleg_obj")
public class ELEGObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "nac")
    private Double nac;

    @Column(name = "atmr")
    private Double atmr;

    @Column(name = "ec")
    private Double ec;

    @Column(name = "elegance")
    private Double elegance;

    public ELEGObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
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
}
