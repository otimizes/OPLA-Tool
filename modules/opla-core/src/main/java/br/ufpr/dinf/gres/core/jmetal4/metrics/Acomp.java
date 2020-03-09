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
public class Acomp extends Metrics {

    private Double sumDepIn;
    private Double sumDepOut;

    public Acomp(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
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


    @Override
    public String toString() {
        return "Acomp [sumDepIn=" + sumDepIn + ", sumDepOut=" + sumDepOut + "]";
    }

    public Double evaluateAcompFitness() {
        return this.sumDepIn + this.sumDepOut;
    }

}
