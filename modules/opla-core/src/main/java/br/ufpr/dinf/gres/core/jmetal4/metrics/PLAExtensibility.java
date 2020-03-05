package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;

/**
 * @author elf
 */
public class PLAExtensibility extends Metrics {

    private double plaExtensibility;

    public PLAExtensibility(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getPlaExtensibility() {
        return plaExtensibility;
    }

    public void setPlaExtensibility(double plaExtensibility) {
        this.plaExtensibility = plaExtensibility;
    }

    @Override
    public String toString() {
        return "PLAExtensibility [" +
                "plaExtensibility=" + plaExtensibility +
                "]";
    }
}
