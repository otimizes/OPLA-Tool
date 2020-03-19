package br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionBase;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.DependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.DependencyOut;

/**
 * Component Coupling (Santos et al., 2015)
 * <p>
 * It aims to measure the 'number of operations of an interface.
 * <p>
 * (DepIn + DepOut)
 */
public class ACOMP extends ObjectiveFunctionBase {

    public ACOMP(Architecture architecture) {
        super(architecture);
        double acompFitness = 0.0;
        DependencyIn depIN = new DependencyIn(architecture);
        DependencyOut depOUT = new DependencyOut(architecture);
        acompFitness = depIN.getResults() + depOUT.getResults();
        this.setResults(acompFitness);
    }

}
