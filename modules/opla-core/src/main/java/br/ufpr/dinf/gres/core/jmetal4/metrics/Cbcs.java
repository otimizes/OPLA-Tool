package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;

public class Cbcs extends Metrics {

    private double cbcs;

    public Cbcs(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getCbcs() {
        return cbcs;
    }

    public void setCbcs(double cbcs) {
        this.cbcs = cbcs;
    }


}

