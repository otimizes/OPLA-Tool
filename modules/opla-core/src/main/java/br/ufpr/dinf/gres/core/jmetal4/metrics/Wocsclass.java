package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;


public class Wocsclass extends Metrics {

    private float wocsclass;

    public Wocsclass(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public float getWocsClass() {
        return wocsclass;
    }

    public void setWocsClass(float wocsclass) {
        this.wocsclass = wocsclass;
    }


}
