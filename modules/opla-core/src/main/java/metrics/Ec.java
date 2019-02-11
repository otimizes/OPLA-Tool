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
public class Ec extends Metrics {

    private double cibc;
    private double iibc;
    private double oobc;

    public Ec(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }


    public double getCibc() {
        return cibc;
    }


    public void setCibc(double cibc) {
        this.cibc = cibc;
    }


    public double getIibc() {
        return iibc;
    }


    public void setIibc(double iibc) {
        this.iibc = iibc;
    }


    public double getOobc() {
        return oobc;
    }


    public void setOobc(double oobc) {
        this.oobc = oobc;
    }


    @Override
    public String toString() {
        return "Ec [cibc=" + cibc + ", iibc=" + iibc + ", oobc=" + oobc + "]";
    }

    public Double evaluateEcFitness() {
        return this.cibc + this.iibc + this.oobc;
    }

}
