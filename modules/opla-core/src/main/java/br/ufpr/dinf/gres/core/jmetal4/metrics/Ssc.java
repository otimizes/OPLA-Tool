package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;
import br.ufpr.dinf.gres.domain.entity.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.metric.SscMetric;


public class Ssc extends Metrics implements IPersistentDto<SscMetric> {

    private double ssc;
    private float results;

    public Ssc(Architecture architecture) {

        float tcommoncomp = 0;
        float tvariablecomp = 0;

        for (Package pacote : architecture.getAllPackages()) {

            int variablecomp = 0;

            for (Element elemento : pacote.getElements()) {

                if (elemento.getVariationPoint() != null) {
                    variablecomp = 1;
                }
            }

            if (variablecomp == 1) {
                tvariablecomp++;
            } else {
                tcommoncomp++;
            }
        }

        if (tcommoncomp == 0) {
            this.results = 0;
        } else {

            this.results = 1 / (tcommoncomp / (tvariablecomp + tcommoncomp));
        }

    }

    public float getResults() {
        return results;
    }

    public Ssc(String idSolution, Execution Execution, Experiment experiement) {
        super.setExecution(Execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getSsc() {
        return ssc;
    }

    public void setSsc(double ssc) {
        this.ssc = ssc;
    }

    @Override
    public SscMetric newPersistentInstance() {
        SscMetric metric = new SscMetric();
        metric.setExecution(this.getExecution().newPersistentInstance());
        metric.setExperiment(this.getExperiement().newPersistentInstance());
        metric.setId(Long.valueOf(this.getIdSolution()));
        metric.setIsAll(this.getIsAll());
        metric.setSsc(String.valueOf(this.getSsc()));
        return metric;
    }
}
