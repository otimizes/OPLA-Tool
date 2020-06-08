package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Interface-level Interlacing Between Concerns (Sant’Anna, 2008)
 *
 * Measures the number of features in which a given interest shares at least one interface
 */
public class IIBC extends ObjectiveFunctionImplementation {

    public IIBC(Architecture architecture) {
        super(architecture);
        double sumIIBC = 0.0;

        IIBCConcerns iibc = new IIBCConcerns(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        this.setResults(sumIIBC);
    }

}
