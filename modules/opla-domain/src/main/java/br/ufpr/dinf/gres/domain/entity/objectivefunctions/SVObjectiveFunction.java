package br.ufpr.dinf.gres.domain.entity.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sv_obj")
public class SVObjectiveFunction extends BaseObjectiveFunction {

    private static final long serialVersionUID = 1L;

    @Column(name = "svc")
    private Double svc;

    public SVObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getSvc() {
        return svc;
    }

    public void setSvc(Double svc) {
        this.svc = svc;
    }


}
