package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Interface;


public class MeanNumOpsByInterface {

    private Double results;

    public MeanNumOpsByInterface(Architecture architecture) {
        this.results = 0.0;
        int numberInterfaces = 0;
        for (br.ufpr.dinf.gres.architecture.representation.Package component : architecture.getAllPackages()) {
            for (Interface itf : component.getImplementedInterfaces()) {
                this.results += itf.getOperations().size();
                numberInterfaces++;
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
