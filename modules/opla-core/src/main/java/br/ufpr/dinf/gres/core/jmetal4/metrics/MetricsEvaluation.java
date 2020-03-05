package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.extensibility.ExtensPLA;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClassComponentResult;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.*;

public class MetricsEvaluation {

    //addYni
    public float evaluateWocsClass(Architecture architecture) {
        return new Wocsclass(architecture).getResults();
    }

    public float evaluateWocsInterface(Architecture architecture) {
        return new Wocsinterface(architecture).getResults();
    }

    public float evaluateCbcs(Architecture architecture) {
        return new Cbcs(architecture).getResults();
    }

    public double evaluateSsc(Architecture architecture) {
        return new Ssc(architecture).getResults();
    }

    public double evaluateSvc(Architecture architecture) {
        return new Svc(architecture).getResults();
    }

    public double evaluateAv(Architecture architecture) {
        return new Av(architecture).getResults();
    }

    //addYni

    public double evaluateATMRElegance(Architecture architecture) {
        return new ATMRElegance(architecture).getResults();
    }

    public double evaluateECElegance(Architecture architecture) {
        return new ECElegance(architecture).getResults();
    }

    public double evaluateNACElegance(Architecture architecture) {
        return new NACElegance(architecture).getResults();
    }

    public double evaluateElegance(Architecture architecture) {
        return evaluateATMRElegance(architecture) + evaluateECElegance(architecture)
                + evaluateNACElegance(architecture);
    }

    public float evaluatePLAExtensibility(Architecture architecture) {
        float extensibilityFitness = 0;
        float extensibility;
        ExtensPLA PLAExtens = new ExtensPLA(architecture);
        extensibilityFitness = PLAExtens.getValue();
        if (extensibilityFitness == 0)
            extensibility = 1000;
        else
            extensibility = 1 / extensibilityFitness;
        return extensibilityFitness;
    }

    public double evaluateMSIFitness(Architecture architecture) {
        double sumCIBC = 0.0;
        double sumIIBC = 0.0;
        double sumOOBC = 0.0;
        double sumCDAC = 0.0;
        double sumCDAI = 0.0;
        double sumCDAO = 0.0;
        double MSIFitness = 0.0;
        Double sumLCC = evaluateLCC(architecture);

        CIBC cibc = new CIBC(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        IIBC iibc = new IIBC(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        OOBC oobc = new OOBC(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }

        CDAC cdac = new CDAC(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }

        CDAI cdai = new CDAI(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }

        CDAO cdao = new CDAO(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }

        MSIFitness = sumLCC + sumCDAC + sumCDAI + sumCDAO + sumCIBC + sumIIBC + sumOOBC;
        return MSIFitness;
    }

    public Double evaluateCIBC(Architecture architecture) {
        Double sumCIBC = 0.0;

        CIBC cibc = new CIBC(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        return sumCIBC;
    }

    public Double evaluateIIBC(Architecture architecture) {
        double sumIIBC = 0.0;

        IIBC iibc = new IIBC(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        return sumIIBC;
    }

    public Double evaluateOOBC(Architecture architecture) {
        double sumOOBC = 0.0;

        OOBC oobc = new OOBC(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }
        return sumOOBC;
    }

    public Double evaluateCDAC(Architecture architecture) {
        double sumCDAC = 0.0;
        CDAC cdac = new CDAC(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }
        return sumCDAC;
    }

    public Double evaluateCDAI(Architecture architecture) {
        double sumCDAI = 0.0;

        CDAI cdai = new CDAI(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }
        return sumCDAI;
    }

    public Double evaluateCDAO(Architecture architecture) {
        double sumCDAO = 0.0;
        CDAO cdao = new CDAO(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }
        return sumCDAO;
    }

    public Double evaluateLCC(Architecture architecture) {
        double sumLCC = 0.0;
        LCC result = new LCC(architecture);

        for (LCCComponentResult component : result.getResults()) {
            sumLCC += component.numberOfConcerns();
        }
        return sumLCC;
    }

    public Double evaluateCDAClass(Architecture architecture) {
        double sumCDAClass = 0.0;

        CDAClass cdaclass = new CDAClass(architecture);
        for (CDAClassResult c : cdaclass.getResults()) {
            sumCDAClass += c.getElements().size();
        }

        return sumCDAClass;
    }

    public Double evaluateCIBClass(Architecture architecture) {
        double sumCIBClass = 0.0;

        CIBClass cibclass = new CIBClass(architecture);
        for (CIBClassResult c : cibclass.getResults().values()) {
            sumCIBClass += c.getInterlacedConcerns().size();
        }

        return sumCIBClass;
    }

    public Double evaluateLCCClass(Architecture architecture) {
        double sumLCCClass = 0.0;
        LCCClass result = new LCCClass(architecture);

        for (LCCClassComponentResult cls : result.getResults()) {
            sumLCCClass += cls.numberOfConcerns();

        }
        return sumLCCClass;
    }

    // ----------------------------------------------------------------------------------
    public Double evaluateMACFitness(Architecture architecture) {
        double MACFitness = 0.0;
        double meanDepComps = 0.0;
        double sumCohesion = 0.0;
        int sumClassesDepIn = 0;
        int sumClassesDepOut = 0;
        int sumDepIn = 0;
        int sumDepOut = 0;
        double iCohesion = 0.0;

        MeanNumOpsByInterface numOps = new MeanNumOpsByInterface(architecture);
        Double meanNumOps = numOps.getResults();

        MeanDepComponents depComps = new MeanDepComponents(architecture);
        meanDepComps = depComps.getResults();

        ClassDependencyOut classesDepOut = new ClassDependencyOut(architecture);
        sumClassesDepOut = classesDepOut.getResults();

        ClassDependencyIn classesDepIn = new ClassDependencyIn(architecture);
        sumClassesDepIn = classesDepIn.getResults();

        DependencyOut DepOut = new DependencyOut(architecture);
        sumDepOut = DepOut.getResults();

        DependencyIn DepIn = new DependencyIn(architecture);
        sumDepIn = DepIn.getResults();

        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        sumCohesion = cohesion.getResults();
        if (sumCohesion == 0) {
            iCohesion = 1.0;
        } else
            iCohesion = 1 / sumCohesion;

        MACFitness = meanNumOps + meanDepComps + sumClassesDepIn + sumClassesDepOut + sumDepIn + sumDepOut
                + (1 / sumCohesion);

        return MACFitness;
    }

    public Double evaluateMeanNumOps(Architecture architecture) {
        MeanNumOpsByInterface numOps = new MeanNumOpsByInterface(architecture);
        return numOps.getResults();
    }

    public Double evaluateMeanDepComps(Architecture architecture) {
        MeanDepComponents depComps = new MeanDepComponents(architecture);
        return depComps.getResults();
    }

    public int evaluateSumClassesDepIn(Architecture architecture) {
        ClassDependencyIn classesDepIn = new ClassDependencyIn(architecture);
        return classesDepIn.getResults();
    }

    public int evaluateSumClassesDepOut(Architecture architecture) {
        ClassDependencyOut classesDepOut = new ClassDependencyOut(architecture);
        return classesDepOut.getResults();
    }

    public double evaluateSumDepIn(Architecture architecture) {
        DependencyIn DepIn = new DependencyIn(architecture);
        return DepIn.getResults();
    }

    public double evaluateSumDepOut(Architecture architecture) {
        DependencyOut DepOut = new DependencyOut(architecture);
        return DepOut.getResults();
    }

    public Double evaluateCohesion(Architecture architecture) {
        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        return cohesion.getResults();
    }

    public Double evaluateICohesion(Double sumCohesion) {
        return sumCohesion == 0 ? 1.0 : 1 / sumCohesion;
    }

}
