package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "acomp_obj")
public class ACOMPObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "sumdepin")
    private Double sumDepIn;
    @Column(name = "sumdepout")
    private Double sumDepOut;

    public ACOMPObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getSumDepIn() {
        return sumDepIn;
    }

    public void setSumDepIn(Double sumDepIn) {
        this.sumDepIn = sumDepIn;
    }

    public Double getSumDepOut() {
        return sumDepOut;
    }

    public void setSumDepOut(Double sumDepOut) {
        this.sumDepOut = sumDepOut;
    }

}
