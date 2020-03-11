package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ssc_metrics")
public class SscMetric extends BaseMetric {

    private static final long serialVersionUID = 1L;

    @Column(name = "ssc")
    private Double ssc;

    public SscMetric(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getSsc() {
        return ssc;
    }

    public void setSsc(Double ssc) {
        this.ssc = ssc;
    }


}
