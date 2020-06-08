package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cm_obj")
public class CMObjectiveFunction extends ObjectiveFunctionDomain {

    private static final long serialVersionUID = 1L;

    @Column(name = "sum_cohesion")
    private Double sumCohesion;

    @Column(name = "macAggregation")
    private Double macAggregation;

    @Column(name = "cohesion")
    private Double cohesion;

    @Column(name = "meanDepComps")
    private Double meanDepComps;

    @Column(name = "meanNumOps")
    private Double meanNumOps;

    @Column(name = "sumClassesDepIn")
    private Double sumClassesDepIn;

    @Column(name = "sumClassesDepOut")
    private Double sumClassesDepOut;

    @Column(name = "sumDepIn")
    private Double sumDepIn;

    @Column(name = "sumDepOut")
    private Double sumDepOut;

    public CMObjectiveFunction(String idSolution, Execution execution, Experiment experiement) {
        super(idSolution, execution, experiement);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Double getSumCohesion() {
        return sumCohesion;
    }

    public void setSumCohesion(Double sumCohesion) {
        this.sumCohesion = sumCohesion;
    }

    public Double getMacAggregation() {
        return macAggregation;
    }

    public void setMacAggregation(Double macAggregation) {
        this.macAggregation = macAggregation;
    }

    public Double getCohesion() {
        return cohesion;
    }

    public void setCohesion(Double cohesion) {
        this.cohesion = cohesion;
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

    public Double getSumClassesDepIn() {
        return sumClassesDepIn;
    }

    public void setSumClassesDepIn(Double sumClassesDepIn) {
        this.sumClassesDepIn = sumClassesDepIn;
    }

    public Double getSumClassesDepOut() {
        return sumClassesDepOut;
    }

    public void setSumClassesDepOut(Double sumClassesDepOut) {
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

}
