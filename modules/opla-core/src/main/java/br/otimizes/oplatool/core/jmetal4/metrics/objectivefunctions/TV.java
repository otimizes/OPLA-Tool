package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.mutability.AV;

/**
 * Architecture Variability (Zhang et al., 2008)
 * <p>
 * It measures the total variability of the PLA.
 * <p>
 * (|Cv| + sum AV)
 */
public class TV extends ObjectiveFunctionImplementation {

    public TV(Architecture architecture) {
        super(architecture);

        Double results = new AV(architecture).getResults();
        this.setResults(results);
    }


}
