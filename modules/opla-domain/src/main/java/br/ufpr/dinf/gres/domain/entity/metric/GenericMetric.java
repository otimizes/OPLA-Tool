package br.ufpr.dinf.gres.domain.entity.metric;

import br.ufpr.dinf.gres.domain.entity.Execution;
import br.ufpr.dinf.gres.domain.entity.Experiment;

import java.io.Serializable;

/**
 * Generic Representation for Metric
 */
public interface GenericMetric extends Serializable {
    String getId();

    Execution getExecution();

    Experiment getExperiment();

    Integer getIsAll();

    String getIdSolution();
}
