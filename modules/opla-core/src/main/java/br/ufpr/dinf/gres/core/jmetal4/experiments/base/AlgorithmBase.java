package br.ufpr.dinf.gres.core.jmetal4.experiments.base;

import br.ufpr.dinf.gres.core.jmetal4.experiments.ExperimentCommonConfigs;

public interface AlgorithmBase<T extends ExperimentCommonConfigs> {

    /**
     * This method must call the experiment class, or implement the experiment code
     */
    void execute(T experimentCommomConfigs) throws Exception;
}
