package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.Metrics;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Component-level Interlacing Between Concerns (Sant’Anna et al, 2008)
 * <p>
 * It measures the feature interlacing of a PLA by summing the numbers of features that the assessed feature share in at least one architectural class.
 * Adopted by Luciane Baldo Nicolodi (2020).
 */
public class CIBF extends ObjectiveFunctionImplementation {

    public CIBF(Architecture architecture) {
        super(architecture);
        this.setResults(Metrics.CIBClass.evaluate(architecture));
    }

}
