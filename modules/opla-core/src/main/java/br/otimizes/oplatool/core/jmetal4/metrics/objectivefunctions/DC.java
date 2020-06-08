package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;

/**
 * Feature Diffusion (Santos et al., 2015)
 * <p>
 * It aims to measure the diffusion of characteristics,
 * adding the numbers of architectural elements (components, interfaces and operations of the project)
 * that contribute to the realization of the characteristics of the LPS.
 * <p>
 * (CDAI + CDAO + CDAC)
 */
public class DC extends ObjectiveFunctionImplementation {

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
