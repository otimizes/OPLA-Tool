package br.otimizes.oplatool.core.jmetal4.metrics.reusability;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Structure Similarity Coefficient (Zhang et al., 2008)
 * <p>
 * and proposal to measure the similarity of PLA.
 * <p>
 * (|Cc| / |Cc| + |Cv|)
 */
public class SSC extends ObjectiveFunctionImplementation {

    public SSC(Architecture architecture) {
        super(architecture);
        double tcommoncomp = 0;
        double resultsComponents;
        double tvariablecomp = 0;

        for (Package pacote : architecture.getAllPackages()) {
            int variablecomp = 0;
            for (Element elemento : pacote.getElements()) {
                if (elemento.getVariationPoint() != null) {
                    variablecomp = 1;
                    break;
                }
            }
            if (variablecomp == 1) {
                tvariablecomp++;
            } else {
                tcommoncomp++;
            }
        }

        if (tcommoncomp == 0) {
            resultsComponents = 0;
        } else {
            resultsComponents = (tcommoncomp / (tvariablecomp + tcommoncomp));
        }

        this.setResults(resultsComponents);
    }

}
