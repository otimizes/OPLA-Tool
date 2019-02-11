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
public class Coe extends Metrics {

    private Double cohesion;
    private Double lcc;

    public Coe(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }


    public Double getCohesion() {
        return cohesion;
    }


    public void setCohesion(Double cohesion) {
        this.cohesion = cohesion;
    }


    public Double getLcc() {
        return lcc;
    }


    public void setLcc(Double lcc) {
        this.lcc = lcc;
    }


    @Override
    public String toString() {
        return "Coe [cohesion=" + cohesion + ", lcc=" + lcc + "]";
    }

    public Double evaluateCoeFitness() {
        //return this.lcc + this.cohesion;
        return this.lcc;
    }


}
