package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;

public class NSGAIIConfigs extends ExperimentCommonConfigs {

    private int populationSize;

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        super.validateGreaterOrEqualOne("populationSize", populationSize);
        this.populationSize = populationSize;
    }
}