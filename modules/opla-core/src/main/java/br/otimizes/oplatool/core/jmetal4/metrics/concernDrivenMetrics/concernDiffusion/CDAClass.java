package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 *   Concern Diffusion over Architectural Components classes
 */
public class CDAClass extends ObjectiveFunctionImplementation {

    public CDAClass(Architecture architecture) {
        super(architecture);
        double sumCDAClass = 0.0;

        CDAClassConcerns cdaclass = new CDAClassConcerns(architecture);
        for (CDAClassResult c : cdaclass.getResults()) {
            sumCDAClass += c.getElements().size();
        }

        this.setResults(sumCDAClass);
    }

}
