package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wocsclass_obj")
public class WOCSCLASSObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "wocsclass")
    private Double wocsclass;

    public WOCSCLASSObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getWocsclass() {
        return wocsclass;
    }

    public void setWocsclass(Double wocsclass) {
        this.wocsclass = wocsclass;
    }


}
