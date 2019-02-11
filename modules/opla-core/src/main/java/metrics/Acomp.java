/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import results.Execution;
import results.Experiment;

/**
 * @author elf
 */
public class Acomp extends Metrics {

    private Double sumDepIn;
    private Double sumDepOut;

    public Acomp(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
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
