package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Concern Diffusion over Architectural Interfaces (Sant'Anna, 2008)
 *
 * Measures the number of interfaces that contribute for realization of a interesting
 *
 */
public class CDAI extends ObjectiveFunctionImplementation {

    public CDAI(Architecture architecture) {
        super(architecture);
        double sumCDAI = 0.0;

        CDAIConcerns cdai = new CDAIConcerns(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }
        this.setResults(sumCDAI);
    }

}
