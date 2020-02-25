package jmetal4.metrics;

import jmetal4.results.Execution;
import jmetal4.results.Experiment;

public class Wocsinterface extends Metrics {

    private double wocsinterface;

    public Wocsinterface(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
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
