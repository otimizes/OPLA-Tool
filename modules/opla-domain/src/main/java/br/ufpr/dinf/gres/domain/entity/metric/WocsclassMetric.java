package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wocsclass_metrics")
public class WocsclassMetric extends BaseMetric {

    private static final long serialVersionUID = 1L;

    @Column(name = "wocsclass")
    private Double wocsclass;

    public WocsclassMetric(String idSolution, Execution execution, Experiment experiement) {
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
