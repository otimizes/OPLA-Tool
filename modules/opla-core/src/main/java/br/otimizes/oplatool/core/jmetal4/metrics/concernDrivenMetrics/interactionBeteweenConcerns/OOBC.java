package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Operation-level Overlapping Between Concerns (Sant’Anna, 2008)
 * <p>
 * Measures the number of features in which a given interest shares at least one operation
 */
public class OOBC extends ObjectiveFunctionImplementation {

    public OOBC(Architecture architecture) {
        super(architecture);
        double sumOOBC = 0.0;

        OOBCConcerns oobc = new OOBCConcerns(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }
        this.setResults(sumOOBC);
    }

}
