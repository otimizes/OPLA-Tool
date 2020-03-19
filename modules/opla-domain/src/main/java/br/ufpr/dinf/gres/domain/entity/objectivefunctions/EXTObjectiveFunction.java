package br.ufpr.dinf.gres.domain.entity.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ext_obj")
public class EXTObjectiveFunction extends BaseObjectiveFunction {

    private static final long serialVersionUID = 1L;

    @Column(name = "pla_extensibility")
    private Double plaExtensibility;

    public EXTObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getPlaExtensibility() {
        return plaExtensibility;
    }

    public void setPlaExtensibility(Double plaExtensibility) {
        this.plaExtensibility = plaExtensibility;
    }
}
