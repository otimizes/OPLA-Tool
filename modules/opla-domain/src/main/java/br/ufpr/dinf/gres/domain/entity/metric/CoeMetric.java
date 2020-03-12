package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coe_metrics")
public class CoeMetric extends BaseMetric {

    private static final long serialVersionUID = 1L;

    @Column(name = "cohesion")
    private Double cohesion;
    @Column(name = "lcc")
    private Double lcc;
    @Column(name = "h")
    private Double h;

    public CoeMetric(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public Double getCohesion() {
        return cohesion;
    }

    public void setCohesion(Double cohesion) {
        this.cohesion = cohesion;
    }

    public Double getLcc() {
        return lcc;
    }

    public void setLcc(Double lcc) {
        this.lcc = lcc;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }
}
