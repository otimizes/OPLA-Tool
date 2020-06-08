package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Mean of operations number of interface
 */
public class MeanNumOpsByInterface extends ObjectiveFunctionImplementation {

    public MeanNumOpsByInterface(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        int numberInterfaces = 0;
        for (Package component : architecture.getAllPackages()) {
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
