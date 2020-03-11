package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class CIBC extends BaseMetricResults {

    public CIBC(Architecture architecture) {
        super(architecture);
        double sumCIBC = 0.0;

        CIBCConcerns cibc = new CIBCConcerns(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        this.setResults(sumCIBC);
    }

}
