package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tv_obj")
public class TVObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "av")
    private Double av;

    public TVObjectiveFunction(String idSolution, Execution execution, Experiment experiment) {
        super(idSolution, execution, experiment);
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
