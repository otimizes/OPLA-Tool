package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 *   Component-level Interlacing Between Concerns for classes
 */
public class CIBClass extends ObjectiveFunctionImplementation {

    public CIBClass(Architecture architecture) {
        super(architecture);
        double sumCIBClass = 0.0;

        CIBClassConcerns cibclass = new CIBClassConcerns(architecture);
        for (CIBClassResult c : cibclass.getResults().values()) {
            sumCIBClass += c.getInterlacedConcerns().size();
        }

        this.setResults(sumCIBClass);
    }

}
