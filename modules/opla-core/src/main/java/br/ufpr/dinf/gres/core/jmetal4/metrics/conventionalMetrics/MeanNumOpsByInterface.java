package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Mean of operations number of interface
 */
public class MeanNumOpsByInterface extends ObjectiveFunctionImplementation {

    public MeanNumOpsByInterface(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        int numberInterfaces = 0;
        for (br.ufpr.dinf.gres.architecture.representation.Package component : architecture.getAllPackages()) {
            for (Interface itf : component.getImplementedInterfaces()) {
                this.addToResults(itf.getMethods().size());
                numberInterfaces++;
            }
        }
        if (numberInterfaces == 0) {
            this.setResults(0.0);
        } else {
            this.divideToResults(numberInterfaces);
        }
    }
}
