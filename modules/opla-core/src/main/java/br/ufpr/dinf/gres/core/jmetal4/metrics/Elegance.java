package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.domain.entity.ExecutionResults;
import br.ufpr.dinf.gres.domain.entity.ExperimentResults;
import br.ufpr.dinf.gres.domain.entity.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.metric.EleganceMetric;

public class Elegance extends Metrics implements IPersistentDto<EleganceMetric> {

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

    @Override
    public EleganceMetric newPersistentInstance() {
        EleganceMetric metric = new EleganceMetric();
        metric.setExecution(this.getExecutionResults().newPersistentInstance());
        metric.setExperiment(this.getExperiement().newPersistentInstance());
        metric.setId(Long.valueOf(this.getIdSolution()));
        metric.setIsAll(this.getIsAll());
        metric.setAtmr(String.valueOf(this.getAtmr()));
        metric.setEc(String.valueOf(this.getEc()));
        metric.setNac(String.valueOf(this.getNac()));
        return metric;
    }
}
