/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.domain.entity.ExecutionResults;
import br.ufpr.dinf.gres.domain.entity.ExperimentResults;
import br.ufpr.dinf.gres.domain.entity.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.metric.ConventionalMetric;

/**
 * @author elf
 */
public class Conventional extends Metrics implements IPersistentDto<ConventionalMetric> {

    private Double sumChoesion;
    private Double cohesion;
    private Double meanDepComps;
    private Double meanNumOps;
    private int sumClassesDepIn;
    private int sumClassesDepOut;
    private Double sumDepIn;
    private Double sumDepOut;

    public Conventional(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public Double getMacAggregation() {
        return this.meanNumOps + this.meanDepComps + (double) this.sumClassesDepIn + (double) this.sumClassesDepOut + this.sumDepIn + this.sumDepOut + (1 / this.sumChoesion);
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

    @Override
    public ConventionalMetric newPersistentInstance() {
        ConventionalMetric metric = new ConventionalMetric();
        metric.setExecution(this.getExecutionResults().newPersistentInstance());
        metric.setExperiment(this.getExperiement().newPersistentInstance());
        metric.setId(Long.valueOf(this.getIdSolution()));
        metric.setIsAll(this.getIsAll());
        metric.setCohesion(String.valueOf(this.getCohesion()));
        metric.setMacAggregation(String.valueOf(this.getMacAggregation()));
        metric.setMeanDepComps(String.valueOf(this.getMeanDepComps()));
        metric.setMeanNumOps(String.valueOf(this.getMeanNumOps()));
        metric.setSumClassesDepIn(String.valueOf(this.getSumClassesDepIn()));
        metric.setSumClassesDepOut(String.valueOf(this.getSumClassesDepOut()));
        metric.setSumDepIn(String.valueOf(this.getSumDepIn()));
        metric.setSumDepOut(String.valueOf(this.getSumDepOut()));
        return metric;
    }
}
