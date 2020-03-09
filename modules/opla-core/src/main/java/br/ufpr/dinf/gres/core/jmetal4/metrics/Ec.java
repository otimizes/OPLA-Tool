/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

/**
 * @author elf
 */
public class Ec extends Metrics {

    private double cibc;
    private double iibc;
    private double oobc;

    public Ec(String idSolution, Execution Execution, Experiment experiement) {
        super.setExecution(Execution);
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
