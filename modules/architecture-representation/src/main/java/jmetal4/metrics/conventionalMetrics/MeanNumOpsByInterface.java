package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;


public class MeanNumOpsByInterface {

    private Architecture architecture;
    private double results;
    private int numberInterfaces;

    public MeanNumOpsByInterface(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0;
        this.numberInterfaces = 0;
        for (arquitetura.representation.Package component : this.architecture.getAllPackages()) {
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
