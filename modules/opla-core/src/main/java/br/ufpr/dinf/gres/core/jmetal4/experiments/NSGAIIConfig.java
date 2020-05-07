package br.ufpr.dinf.gres.core.jmetal4.experiments;

public class NSGAIIConfig extends ExperimentCommonConfigs {

    private int populationSize;

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        super.validateGreaterOrEqualOne("populationSize", populationSize);
        this.populationSize = populationSize;
    }
}