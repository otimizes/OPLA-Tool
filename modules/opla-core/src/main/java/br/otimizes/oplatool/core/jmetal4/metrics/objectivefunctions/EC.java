package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;

/**
 * Feature Interaction (Santos et al., 2015)
 * <p>
 * It aims to measure the intertwining of characteristics in a PLA,
 * adding the numbers of the characteristics with which an interest
 * shares at least with an architectural element, such as component,
 * interface and operation.
 * <p>
 * (CIBC, IIBC, OOBC)
 */
public class EC extends ObjectiveFunctionImplementation {

    public EC(Architecture architecture) {
        super(architecture);
        double ecFitness = 0.0;
        double sumCIBC = 0.0;
        double sumIIBC = 0.0;
        double sumOOBC = 0.0;

        CIBCConcerns cibc = new CIBCConcerns(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        IIBCConcerns iibc = new IIBCConcerns(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        OOBCConcerns oobc = new OOBCConcerns(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }

        ecFitness = sumCIBC + sumIIBC + sumOOBC;
        this.setResults(ecFitness);
    }

}
