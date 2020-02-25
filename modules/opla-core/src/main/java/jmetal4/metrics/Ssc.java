package jmetal4.metrics;

import jmetal4.results.Execution;
import jmetal4.results.Experiment;


public class Ssc extends Metrics {

    private double ssc;

    public Ssc(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getSsc() {
        return ssc;
    }

    public void setSsc(double ssc) {
        this.ssc = ssc;
    }

}
