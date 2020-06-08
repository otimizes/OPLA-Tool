package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;

/**
 * Feature Driven Function
 * <p>
 * It aims to measure the modularization of characteristics and, for this,
 * it is composed of a set of various metrics directed to the characteristics
 * proposed in (Sant'Anna, 2008).
 * Sum of (LCC, CDAC, CDAI, CDAO, CIBC, IIBC , OOBC)
 */
public class FM extends ObjectiveFunctionImplementation {

    public FM(Architecture architecture) {
        super(architecture);
        double sumCIBC = 0.0;
        double sumIIBC = 0.0;
        double sumOOBC = 0.0;
        double sumCDAC = 0.0;
        double sumCDAI = 0.0;
        double sumCDAO = 0.0;
        double sumLCC = 0.0;
        double MSIFitness = 0.0;
        double sumCDAClass = 0.0;
        double sumCIBClass = 0.0;
        double sumLCCClass = 0.0;

        sumLCC = new LCC(architecture).getResults();

        sumLCCClass = new LCCClass(architecture).getResults();

        CIBCConcerns cibc = new CIBCConcerns(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        CIBClassConcerns cibclass = new CIBClassConcerns(architecture);
        for (CIBClassResult c : cibclass.getResults().values()) {
            sumCIBClass += c.getInterlacedConcerns().size();
        }

        IIBCConcerns iibc = new IIBCConcerns(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        OOBCConcerns oobc = new OOBCConcerns(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }

        CDACConcerns cdac = new CDACConcerns(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }

        CDAClassConcerns cdaclass = new CDAClassConcerns(architecture);
        for (CDAClassResult c : cdaclass.getResults()) {
            sumCDAClass += c.getElements().size();
        }

        CDAIConcerns cdai = new CDAIConcerns(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }

        CDAOConcerns cdao = new CDAOConcerns(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }

        MSIFitness = sumLCC + sumLCCClass + sumCDAC + sumCDAClass + sumCDAI
                + sumCDAO + sumCIBC + sumCIBClass + sumIIBC + sumOOBC;

        this.setResults(MSIFitness);
    }

}
