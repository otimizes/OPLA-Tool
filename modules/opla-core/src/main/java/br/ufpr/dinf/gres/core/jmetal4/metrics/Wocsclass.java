package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.Execution;
import br.ufpr.dinf.gres.core.jmetal4.results.Experiment;


public class Wocsclass extends Metrics {

    private float wocsclass;

    public Wocsclass(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
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
