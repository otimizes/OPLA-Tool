package br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionBase;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyOut;

/**
 * Class Coupling (Santos et al., 2015)
 * <p>
 * It aims to measure the 'number of input and output elements
 * in which one class depends on another.
 * <p>
 * (CDepIn + CDedOut)
 */
public class ACLASS extends ObjectiveFunctionBase {

    public ACLASS(Architecture architecture) {
        super(architecture);
        double aclassFitness = 0.0;
        ClassDependencyIn CDepIN = new ClassDependencyIn(architecture);
        ClassDependencyOut CDepOUT = new ClassDependencyOut(architecture);
        aclassFitness = CDepIN.getResults() + CDepOUT.getResults();
        this.setResults(aclassFitness);
    }

}
