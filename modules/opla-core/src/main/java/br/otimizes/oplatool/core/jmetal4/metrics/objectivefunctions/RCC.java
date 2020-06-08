package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Relationship Coupling of Components - RCC(pla) (Ribeiro et al., 2010)
 * (Sum CBCS(itf))
 *
 * CBCS Metric: Coupling Between Components or Services
 *
 * It is calculated based on the number of relationships between
 * a service A, for example and other services.
 * (Sum AiBj)
 */
public class RCC extends ObjectiveFunctionImplementation {

    public RCC(Architecture architecture) {
        super(architecture);
        double valorcbcs = 0;
        double numinterface = architecture.getAllInterfaces().size();

        for (Interface interf : architecture.getAllInterfaces()) {
            valorcbcs += interf.getRelationships().size();
        }
        double v = valorcbcs / numinterface;

        this.setResults(v);
    }

}
