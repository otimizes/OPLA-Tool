/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.domain.entity.ExecutionResults;
import br.ufpr.dinf.gres.domain.entity.ExperimentResults;

/**
 * @author elf
 */
public class Tam extends Metrics {

    private Double meanNumOps;

    public Tam(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public Double getMeanNumOps() {
        return meanNumOps;
    }

    public void setMeanNumOps(Double meanNumOps) {
        this.meanNumOps = meanNumOps;
    }

    @Override
    public String toString() {
        return "Tam [meanNumOps=" + meanNumOps + "]";
    }

    public Double evaluateTamFitness() {
        return this.meanNumOps;
    }

}
