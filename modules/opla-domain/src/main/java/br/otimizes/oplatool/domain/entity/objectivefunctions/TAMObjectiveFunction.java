package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tam_obj")
public class TAMObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "meannumops")
    private Double meanNumOps;
    @Column(name = "tam")
    private Double tam;

    public TAMObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getMeanNumOps() {
        return meanNumOps;
    }

    public void setMeanNumOps(Double meanNumOps) {
        this.meanNumOps = meanNumOps;
    }

    public Double getTam() {
        return tam;
    }

    public void setTam(Double tam) {
        this.tam = tam;
    }
}
