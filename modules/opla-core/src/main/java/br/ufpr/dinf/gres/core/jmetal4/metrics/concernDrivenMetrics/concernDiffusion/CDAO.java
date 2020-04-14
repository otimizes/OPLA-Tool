package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionImplementation;

public class CDAO extends ObjectiveFunctionImplementation {

    public CDAO(Architecture architecture) {
        super(architecture);
        double sumCDAO = 0.0;
        CDAOConcerns cdao = new CDAOConcerns(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }
        this.setResults(sumCDAO);
    }

}
