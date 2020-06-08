package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.reusability.SSC;

/**
 * Structure Similarity Coefficient (Zhang et al., 2008)
 * <p>
 * Proposal to measure the similarity of PLA.
 * <p>
 * (|Cc| / |Cc| + |Cv|)
 */
public class SD extends ObjectiveFunctionImplementation {

    public SD(Architecture architecture) {
        super(architecture);
        Double results = new SSC(architecture).getResults();
        this.setResults(results != 0 ? 1 / results : 0);
    }

}
