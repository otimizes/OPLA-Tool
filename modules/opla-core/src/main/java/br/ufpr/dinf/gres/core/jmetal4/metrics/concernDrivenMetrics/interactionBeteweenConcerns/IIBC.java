package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class IIBC extends BaseMetricResults {

    public IIBC(Architecture architecture) {
        super(architecture);
        double sumIIBC = 0.0;

        IIBCConcerns iibc = new IIBCConcerns(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        this.setResults(sumIIBC);
    }

}
