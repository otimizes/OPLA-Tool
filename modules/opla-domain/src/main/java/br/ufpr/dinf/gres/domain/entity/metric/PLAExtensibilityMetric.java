package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "plaextensibility_metrics")
public class PLAExtensibilityMetric extends BaseMetric {

    private static final long serialVersionUID = 1L;

    @Column(name = "plaExtensibility")
    private Double plaExtensibility;

    public PLAExtensibilityMetric(String idSolution, Execution execution, Experiment experiement) {
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
