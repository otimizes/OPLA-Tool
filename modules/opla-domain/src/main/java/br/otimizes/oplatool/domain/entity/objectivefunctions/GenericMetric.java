package br.otimizes.oplatool.domain.entity.objectivefunctions;

import br.otimizes.oplatool.domain.entity.Execution;
import br.otimizes.oplatool.domain.entity.Experiment;

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
