package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.core.jmetal4.results.ExecutionResults;
import br.ufpr.dinf.gres.core.jmetal4.results.ExperimentResults;
import br.ufpr.dinf.gres.core.persistence.IPersistentDto;
import br.ufpr.dinf.gres.domain.entity.metric.PLAExtensibilityMetric;

/**
 * @author elf
 */
public class PLAExtensibility extends Metrics implements IPersistentDto<PLAExtensibilityMetric> {

    private double plaExtensibility;

    public PLAExtensibility(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getPlaExtensibility() {
        return plaExtensibility;
    }

    public void setPlaExtensibility(double plaExtensibility) {
        this.plaExtensibility = plaExtensibility;
    }

    @Override
    public String toString() {
        return "PLAExtensibility [" +
                "plaExtensibility=" + plaExtensibility +
                "]";
    }

    @Override
    public PLAExtensibilityMetric newPersistentInstance() {
        PLAExtensibilityMetric metric = new PLAExtensibilityMetric();
        metric.setExecution(this.getExecutionResults().newPersistentInstance());
        metric.setExperiment(this.getExperiement().newPersistentInstance());
        metric.setId(Long.valueOf(this.getIdSolution()));
        metric.setIsAll(this.getIsAll());
        metric.setPlaExtensibility(String.valueOf(this.getPlaExtensibility()));
        return metric;
    }
}
