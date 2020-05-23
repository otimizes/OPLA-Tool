package br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.ufpr.dinf.gres.core.jmetal4.metrics.mutability.SVC;

/**
 * Structure Variability Coefficient (Ahrens et al., 2013)
 * <p>
 * Proposal to measure the variability of PLA.
 * <p>
 * (|Cv| / |Cc| + |Cv|)
 */
public class SV extends ObjectiveFunctionImplementation {

    public SV(Architecture architecture) {
        super(architecture);
        Double results = new SVC(architecture).getResults();
        this.setResults(results);
    }

}
