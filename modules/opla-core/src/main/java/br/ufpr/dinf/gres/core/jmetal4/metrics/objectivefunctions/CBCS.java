package br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Coupling Between Components or Services (Ribeiro et al., 2010)
 *
 * It is calculated based on the number of relationships between
 * a service A, for example and other services.
 * (Sum AiBj)
 */
public class CBCS extends ObjectiveFunctionImplementation {

    public CBCS(Architecture architecture) {
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
