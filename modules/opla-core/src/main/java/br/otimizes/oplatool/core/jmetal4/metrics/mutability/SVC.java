package br.otimizes.oplatool.core.jmetal4.metrics.mutability;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Structure Variability Coefficient (Zhang et al., 2008)
 * <p>
 * and proposal to measure the variability of PLA.
 * <p>
 * (|Cv| / |Cc| + |Cv|)
 */
public class SVC extends ObjectiveFunctionImplementation {

    public SVC(Architecture architecture) {
        super(architecture);
        double resultsComponents;

        double tcommoncomp = 0;
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

        double denominador = tcommoncomp + tvariablecomp;

        if (denominador == 0) {
            resultsComponents = 0;
        } else {
            resultsComponents = tvariablecomp / denominador;
        }
        this.setResults(resultsComponents);
    }

}
