package br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionBase;
import br.ufpr.dinf.gres.core.jmetal4.metrics.reusability.SSC;

/**
 * Structure Similarity Coefficient (Zhang et al., 2008)
 * <p>
 * and proposal to measure the similarity of PLA.
 * <p>
 * (|Cc| / |Cc| + |Cv|)
 */
public class SD extends ObjectiveFunctionBase {

    public SD(Architecture architecture) {
        super(architecture);
        Double results = new SSC(architecture).getResults();
        this.setResults(results != 0 ? 1 / results : 0);
    }

}
