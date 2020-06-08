package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "coe_obj")
public class COEObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "h")
    private Double h;

    public COEObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }
}
