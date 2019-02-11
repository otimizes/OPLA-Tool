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
public class Conventional extends Metrics {

    private Double sumChoesion;
    private Double cohesion;
    private Double meanDepComps;
    private Double meanNumOps;
    private int sumClassesDepIn;
    private int sumClassesDepOut;
    private Double sumDepIn;
    private Double sumDepOut;

    public Conventional(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public Double getMacAggregation() {
        return this.meanNumOps + this.meanDepComps + Double.valueOf(this.sumClassesDepIn) + Double.valueOf(this.sumClassesDepOut) + this.sumDepIn + this.sumDepOut + (1 / this.sumChoesion);
    }

    public Double getSumChoesion() {
        return sumChoesion;
    }

    public void setSumChoesion(Double sumChoesion) {
        this.sumChoesion = sumChoesion;
    }

    public void setSumCohesion(Double sumChoesion) {
        this.sumChoesion = sumChoesion;
    }

    public Double getMeanDepComps() {
        return meanDepComps;
    }

    public void setMeanDepComps(Double meanDepComps) {
        this.meanDepComps = meanDepComps;
    }

    public Double getMeanNumOps() {
        return meanNumOps;
    }

    public void setMeanNumOps(Double meanNumOps) {
        this.meanNumOps = meanNumOps;
    }

    public int getSumClassesDepIn() {
        return sumClassesDepIn;
    }

    public void setSumClassesDepIn(int sumClassesDepIn) {
        this.sumClassesDepIn = sumClassesDepIn;
    }

    public int getSumClassesDepOut() {
        return sumClassesDepOut;
    }

    public void setSumClassesDepOut(int sumClassesDepOut) {
        this.sumClassesDepOut = sumClassesDepOut;
    }

    public Double getSumDepIn() {
        return sumDepIn;
    }

    public void setSumDepIn(Double sumDepIn) {
        this.sumDepIn = sumDepIn;
    }

    public Double getSumDepOut() {
        return sumDepOut;
    }

    public void setSumDepOut(Double sumDepOut) {
        this.sumDepOut = sumDepOut;
    }

    public Double getCohesion() {
        return cohesion;
    }

    public void setCohesion(Double choesion) {
        this.cohesion = choesion;
    }

    @Override
    public String toString() {
        return "Conventional [sumChoesion=" + sumChoesion + ", cohesion=" + cohesion + ", meanDepComps=" + meanDepComps
                + ", meanNumOps=" + meanNumOps + ", sumClassesDepIn=" + sumClassesDepIn + ", sumClassesDepOut="
                + sumClassesDepOut + ", sumDepIn=" + sumDepIn + ", sumDepOut=" + sumDepOut + "]";
    }

    public Double evaluateMACFitness() {
        return this.sumClassesDepIn + this.sumClassesDepOut + this.sumDepIn + this.sumDepOut + this.cohesion;
    }

}
