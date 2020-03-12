package br.ufpr.dinf.gres.core.jmetal4.metrics.all;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class SSC extends BaseMetricResults {

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
            resultsComponents = 1 / (tcommoncomp / (tvariablecomp + tcommoncomp));
        }

        this.setResults(resultsComponents);
    }

}
