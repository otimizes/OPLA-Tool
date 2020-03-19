package br.ufpr.dinf.gres.domain.entity.objectivefunctions;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import javax.persistence.*;

@Entity
@Table(name = "tv_obj")
public class TVObjectiveFunction extends BaseObjectiveFunction {

    private static final long serialVersionUID = 1L;

    @Column(name = "av")
    private Double av;

    public TVObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getAv() {
        return av;
    }

    public void setAv(Double av) {
        this.av = av;
    }

}
