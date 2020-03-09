package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.metric.CbcsMetric;

public class Cbcs extends Metrics implements IPersistentDto<CbcsMetric> {

    private double cbcs;
    private float results;

    public Cbcs(Architecture architecture) {

        this.results = 0;
        float valorcbcs = 0;
        float numinterface = architecture.getAllInterfaces().size();

        for (Interface interf : architecture.getAllInterfaces()) {
            valorcbcs += interf.getRelationships().size();
        }

        this.results = valorcbcs / numinterface;

    }

    public float getResults() {
        return results;
    }


    public Cbcs(String idSolution, Execution Execution, Experiment experiement) {
        super.setExecution(Execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getCbcs() {
        return cbcs;
    }

    public void setCbcs(double cbcs) {
        this.cbcs = cbcs;
    }


    @Override
    public CbcsMetric newPersistentInstance() {
        CbcsMetric metric = new CbcsMetric();
        metric.setExecution(this.getExecution().newPersistentInstance());
        metric.setExperiment(this.getExperiement().newPersistentInstance());
        metric.setId(Long.valueOf(this.getIdSolution()));
        metric.setIsAll(this.getIsAll());
        metric.setCbcs(String.valueOf(this.getCbcs()));
        return metric;
    }
}

