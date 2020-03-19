package br.ufpr.dinf.gres.core.jmetal4.metrics.mutability;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionBase;

/**
 * Architecture Variability (Zhang et al., 2008)
 * <p>
 * It measures the total variability of the PLA.
 * <p>
 * (|Cv| + sum AV)
 */
public class AV extends ObjectiveFunctionBase {

    public AV(Architecture architecture) {
        super(architecture);
        int a = 0;
        int compvariable = cantComponetvariable(architecture);

        for (Package pacote : architecture.getAllPackages()) {

            int compcomposto = 0;

            for (Element elemento : pacote.getElements()) {
                if (elemento.getTypeElement().equals("package")) {
                    compcomposto++;
                }
            }

            if (compcomposto != 0) {
                a = compcomposto;
            } else {
                a = compvariable;
            }
        }

        this.setResults(compvariable + a);
    }

    private static int cantComponetvariable(Architecture architecture) {
        int cantvcomponet = 0;

        for (Package pacote : architecture.getAllPackages()) {
            int variablecomp = 0;

            for (Element elemento : pacote.getElements()) {

                if (elemento.getVariationPoint() != null) {
                    variablecomp = 1;
                    break;
                }
            }

            if (variablecomp == 1) {
                cantvcomponet++;
            }
        }

        return cantvcomponet;
    }


}
