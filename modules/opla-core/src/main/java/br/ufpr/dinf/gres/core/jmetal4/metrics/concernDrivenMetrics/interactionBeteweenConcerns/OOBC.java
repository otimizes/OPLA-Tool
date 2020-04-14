package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionImplementation;

public class OOBC extends ObjectiveFunctionImplementation {

    public OOBC(Architecture architecture) {
        super(architecture);
        double sumOOBC = 0.0;

        OOBCConcerns oobc = new OOBCConcerns(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }
        this.setResults(sumOOBC);
    }

}
