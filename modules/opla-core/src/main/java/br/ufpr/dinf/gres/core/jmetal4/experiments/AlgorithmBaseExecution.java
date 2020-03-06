package br.ufpr.dinf.gres.core.jmetal4.experiments;

import br.ufpr.dinf.gres.core.jmetal4.experiments.ExperimentCommomConfigs;

public interface AlgorithmBaseExecution<T extends ExperimentCommomConfigs> {

    /**
     * This method must call the experiment class, or implement the experiment code
     */

    void execute(T experimentCommomConfigs) throws Exception;
}
