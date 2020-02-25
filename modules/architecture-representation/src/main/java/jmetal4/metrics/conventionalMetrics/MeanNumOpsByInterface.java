package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;


public class MeanNumOpsByInterface {

    private Architecture architecture;
    private Double results;
    private int numberInterfaces;

    public MeanNumOpsByInterface(Architecture architecture) {
        this.architecture = architecture;
        this.results = 0.0;
        this.numberInterfaces = 0;
        for (arquitetura.representation.Package component : this.architecture.getAllPackages()) {
            for (Interface itf : component.getImplementedInterfaces()) {
                this.results += itf.getOperations().size();
                this.numberInterfaces++;
            }
        }
        if (numberInterfaces == 0) {
            this.results = 0.0;
        } else {
            this.results = results / numberInterfaces;
        }
    }

    public Double getResults() {
        return results;
    }

}
