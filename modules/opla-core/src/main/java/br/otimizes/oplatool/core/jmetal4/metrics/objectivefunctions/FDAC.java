package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.Metrics;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Feature Diffusion over Architectural Classes (Sant’Anna et al, 2008)
 * <p>
 * Considering an SPL architecture PLA, this function measures the feature diffusion by the summing the number of architectural classes associated with the SPL features.
 * Adopted by Luciane Baldo Nicolodi (2020).
 */
public class FDAC extends ObjectiveFunctionImplementation {

    public FDAC(Architecture architecture) {
        super(architecture);
        this.setResults(Metrics.CDAClass.evaluate(architecture));
    }

}
