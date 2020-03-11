package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "coe_metrics")
public class CoeMetric extends BaseMetric {

    private static final long serialVersionUID = 1L;

    @Column(name = "cohesion")
    private Double cohesion;

    public CoeMetric(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public Double getCohesion() {
        return cohesion;
    }

    public void setCohesion(Double cohesion) {
        this.cohesion = cohesion;
    }


}
