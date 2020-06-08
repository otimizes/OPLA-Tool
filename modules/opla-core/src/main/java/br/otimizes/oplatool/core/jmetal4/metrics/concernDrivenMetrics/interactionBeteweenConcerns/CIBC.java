package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Component-level Interlacing Between Concerns (Sant’Anna, 2008)
 * <p>
 * Measures the number of features in which a given interest shares the same package
 */
public class CIBC extends ObjectiveFunctionImplementation {

    public CIBC(Architecture architecture) {
        super(architecture);
        double sumCIBC = 0.0;

        CIBCConcerns cibc = new CIBCConcerns(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        this.setResults(sumCIBC);
    }

}
