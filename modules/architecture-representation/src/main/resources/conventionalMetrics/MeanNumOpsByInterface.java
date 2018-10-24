package br.uem.din.metrics.conventionalMetrics;

import br.uem.din.architectureEvolution.representation.Architecture;
import br.uem.din.architectureEvolution.representation.Component;
import br.uem.din.architectureEvolution.representation.Interface;

public class MeanNumOpsByInterface {

    private Architecture architecture;
    private double results;
    private int numberInterfaces;

    public MeanNumOpsByInterface(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0;
        this.numberInterfaces = 0;
        for (Component component : architecture.getComponents()) {
            for (Interface itf : component.getImplementedInterfaces()) {
                this.results += itf.getOperations().size();
                this.numberInterfaces++;
            }
        }
        this.results = results / numberInterfaces;
    }

    public double getResults() {
        return results;
    }

}
