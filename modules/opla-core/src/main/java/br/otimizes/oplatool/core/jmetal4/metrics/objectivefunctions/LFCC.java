package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.Metrics;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Lack of Concern-based Cohesion (Sant’Anna et al, 2008)
 * <p>
 * It indicates the lack of feature-based cohesion by summing the number of features assessed by each class of a PLA.
 * Adopted by Luciane Baldo Nicolodi (2020).
 */
public class LFCC extends ObjectiveFunctionImplementation {

    public LFCC(Architecture architecture) {
        super(architecture);
        this.setResults(Metrics.LCCClass.evaluate(architecture));
    }

}
