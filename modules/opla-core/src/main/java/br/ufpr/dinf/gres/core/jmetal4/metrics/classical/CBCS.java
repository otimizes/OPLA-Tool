package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class CBCS extends BaseMetricResults {

    public CBCS(Architecture architecture) {
        super(architecture);
        double valorcbcs = 0;
        double numinterface = architecture.getAllInterfaces().size();

        for (Interface interf : architecture.getAllInterfaces()) {
            valorcbcs += interf.getRelationships().size();
        }

        this.setResults(valorcbcs / numinterface);
    }

}
