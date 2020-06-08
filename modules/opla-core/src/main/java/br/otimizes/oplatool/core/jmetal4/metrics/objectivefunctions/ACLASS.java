package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.ClassDependencyIn;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.ClassDependencyOut;

/**
 * Class Coupling (Santos et al., 2015)
 * <p>
 * It aims to measure the 'number of input and output elements
 * in which one class depends on another.
 * <p>
 * (CDepIn + CDedOut)
 */
public class ACLASS extends ObjectiveFunctionImplementation {

    public ACLASS(Architecture architecture) {
        super(architecture);
        double aclassFitness = 0.0;
        ClassDependencyIn CDepIN = new ClassDependencyIn(architecture);
        ClassDependencyOut CDepOUT = new ClassDependencyOut(architecture);
        aclassFitness = CDepIN.getResults() + CDepOUT.getResults();
        this.setResults(aclassFitness);
    }

}
