package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.DependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.DependencyOut;
import br.ufpr.dinf.gres.core.jmetal4.metrics.extensibility.ExtensVarComponent;

public class ACOMP extends BaseMetricResults {

    public ACOMP(Architecture architecture) {
        super(architecture);
        double acompFitness = 0.0;
        DependencyIn depIN = new DependencyIn(architecture);
        DependencyOut depOUT = new DependencyOut(architecture);
        acompFitness = depIN.getResults() + depOUT.getResults();
        this.setResults(acompFitness);
    }

}
