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
public class Tam extends Metrics {

    private Double meanNumOps;

    public Tam(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
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
