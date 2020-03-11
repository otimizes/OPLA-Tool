package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dc_metrics")
public class DcMetric extends BaseMetric {

    private static final long serialVersionUID = 1L;

    @Column(name = "cdac")
    private Double cdac;
    @Column(name = "cdai")
    private Double cdai;
    @Column(name = "cdao")
    private Double cdao;

    public DcMetric(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getCdac() {
        return cdac;
    }

    public void setCdac(Double cdac) {
        this.cdac = cdac;
    }

    public Double getCdai() {
        return cdai;
    }

    public void setCdai(Double cdai) {
        this.cdai = cdai;
    }

    public Double getCdao() {
        return cdao;
    }

    public void setCdao(Double cdao) {
        this.cdao = cdao;
    }

}
