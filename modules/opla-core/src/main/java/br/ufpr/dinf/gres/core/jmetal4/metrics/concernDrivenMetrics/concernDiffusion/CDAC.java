package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionImplementation;

public class CDAC extends ObjectiveFunctionImplementation {

    public CDAC(Architecture architecture) {
        super(architecture);
        double sumCDAC = 0.0;
        CDACConcerns cdac = new CDACConcerns(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }
        this.setResults(sumCDAC);
    }

}
