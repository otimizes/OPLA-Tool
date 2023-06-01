package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;

public class NSGAIII_jm6Configs extends ExperimentCommonConfigs {

    private int populationSize;

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        super.validateGreaterOrEqualOne("populationSize", populationSize);
        this.populationSize = populationSize;
    }
}