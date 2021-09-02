package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "lcc_obj")
public class LCCObjectiveFunction extends ObjectiveFunctionDomain {

    @Column(name = "lcc")
    private Double lcc;

    public LCCObjectiveFunction(String idSolution, Execution execution, Experiment experiment) {
        super(idSolution, execution, experiment);
    }

    public Double getLcc() {
        return lcc;
    }

    public void setLcc(Double lcc) {
        this.lcc = lcc;
    }
}
