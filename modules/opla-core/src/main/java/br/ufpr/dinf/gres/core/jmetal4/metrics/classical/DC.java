package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyOut;

public class DC extends BaseMetricResults {

    public DC(Architecture architecture) {
        super(architecture);
        double dcFitness = 0.0;
        double sumCDAC = 0.0;
        double sumCDAI = 0.0;
        double sumCDAO = 0.0;

        CDAIConcerns cdai = new CDAIConcerns(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }

        CDAOConcerns cdao = new CDAOConcerns(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }

        CDACConcerns cdac = new CDACConcerns(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }

        dcFitness = sumCDAI + sumCDAO + sumCDAC;
        this.setResults(dcFitness);
    }

}
