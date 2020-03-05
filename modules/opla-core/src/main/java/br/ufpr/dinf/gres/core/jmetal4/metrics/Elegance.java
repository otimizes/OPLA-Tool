package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;

public class Elegance extends Metrics {

    private double nac;
    private double atmr;
    private double ec;

    public Elegance(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getNac() {
        return nac;
    }

    public void setNac(double nac) {
        this.nac = nac;
    }

    public double getAtmr() {
        return atmr;
    }

    public void setAtmr(double d) {
        this.atmr = d;
    }

    public double getEc() {
        return ec;
    }

    public void setEc(double ec) {
        this.ec = ec;
    }

    public double evaluateEleganceFitness() {
        return this.nac + this.atmr + this.ec;
    }

    @Override
    public String toString() {
        return "Elegance [" +
                "nac=" + nac +
                ", atmr=" + atmr +
                ", ec=" + ec +
                "]";
    }
}
