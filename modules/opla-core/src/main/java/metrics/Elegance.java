package metrics;

import results.Execution;
import results.Experiment;

public class Elegance extends Metrics {

    private double nac;
    private double atmr;
    private double ec;

    public Elegance(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
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

}
