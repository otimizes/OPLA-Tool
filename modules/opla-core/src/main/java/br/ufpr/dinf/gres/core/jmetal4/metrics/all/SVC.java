package br.ufpr.dinf.gres.core.jmetal4.metrics.all;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class SVC extends BaseMetricResults {

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
