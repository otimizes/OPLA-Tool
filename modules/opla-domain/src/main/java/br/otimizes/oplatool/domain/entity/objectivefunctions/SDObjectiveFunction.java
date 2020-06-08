package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.*;

@Entity
@Table(name = "sd_obj")
public class SDObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "ssc")
    private Double ssc;

    public SDObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
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
