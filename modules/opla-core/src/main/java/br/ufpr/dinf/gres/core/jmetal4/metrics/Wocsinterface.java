package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;

public class Wocsinterface extends Metrics {

    private double wocsinterface;

    public Wocsinterface(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getWocsInterface() {
        return wocsinterface;
    }

    public void setWocsInterface(double wocsinterface) {
        this.wocsinterface = wocsinterface;
    }


}
