package jmetal4.metrics;


import arquitetura.representation.Architecture;
import jmetal4.metrics.PLAMetrics.extensibility.ExtensPLA;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCC;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClassComponentResult;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;
import jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;
import jmetal4.metrics.conventionalMetrics.*;


public class MetricsEvaluation {

    public double evaluateATMRElegance(Architecture architecture) {
        ATMRElegance ATMR = new ATMRElegance(architecture);
        return ATMR.getResults();
    }

    public double evaluateECElegance(Architecture architecture) {
        ECElegance EC = new ECElegance(architecture);
        return EC.getResults();
    }

    public double evaluateNACElegance(Architecture architecture) {
        NACElegance NAC = new NACElegance(architecture);
        return NAC.getResults();
    }


    public double evaluateElegance(Architecture architecture) {
        double EleganceFitness = 0.0;
        EleganceFitness = evaluateATMRElegance(architecture) + evaluateECElegance(architecture) + evaluateNACElegance(architecture);
        return EleganceFitness;
    }

    public float evaluatePLAExtensibility(Architecture architecture) {
        float ExtensibilityFitness = 0;
        float Extensibility;
        ExtensPLA PLAExtens = new ExtensPLA(architecture);
        ExtensibilityFitness = PLAExtens.getValue();
        if (ExtensibilityFitness == 0)
            Extensibility = 1000;
        else Extensibility = 1 / ExtensibilityFitness;
        return (Extensibility);
    }

    public double evaluateMSIFitness(Architecture architecture) {
        double sumCIBC = 0.0;
        double sumIIBC = 0.0;
        double sumOOBC = 0.0;
        double sumCDAC = 0.0;
        double sumCDAI = 0.0;
        double sumCDAO = 0.0;
        double sumLCC = 0.0;
        double MSIFitness = 0.0;

        sumLCC = evaluateLCC(architecture);


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


    public double evaluateCIBC(Architecture architecture) {
        double sumCIBC = 0.0;

        CIBC cibc = new CIBC(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        return sumCIBC;
    }


    public double evaluateIIBC(Architecture architecture) {

        double sumIIBC = 0.0;

        IIBC iibc = new IIBC(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        return sumIIBC;
    }


    public double evaluateOOBC(Architecture architecture) {

        double sumOOBC = 0.0;

        OOBC oobc = new OOBC(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }
        return sumOOBC;
    }


    public double evaluateCDAC(Architecture architecture) {

        double sumCDAC = 0.0;
        CDAC cdac = new CDAC(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }
        return sumCDAC;
    }

    public double evaluateCDAI(Architecture architecture) {
        double sumCDAI = 0.0;

        CDAI cdai = new CDAI(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }
        return sumCDAI;
    }


    public double evaluateCDAO(Architecture architecture) {
        double sumCDAO = 0.0;
        CDAO cdao = new CDAO(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }
        return sumCDAO;
    }

    public double evaluateLCC(Architecture architecture) {
        double sumLCC = 0.0;
        LCC result = new LCC(architecture);

        for (LCCComponentResult component : result.getResults()) {
            sumLCC += component.numberOfConcerns();

        }
        return sumLCC;
    }

    public double evaluateCDAClass(Architecture architecture) {

        double sumCDAClass = 0.0;

        CDAClass cdaclass = new CDAClass(architecture);
        for (CDAClassResult c : cdaclass.getResults()) {
            sumCDAClass += c.getElements().size();
        }

        return sumCDAClass;
    }


    public double evaluateCIBClass(Architecture architecture) {

        double sumCIBClass = 0.0;

        CIBClass cibclass = new CIBClass(architecture);
        for (CIBClassResult c : cibclass.getResults().values()) {
            sumCIBClass += c.getInterlacedConcerns().size();
        }

        return sumCIBClass;
    }


    public double evaluateLCCClass(Architecture architecture) {
        double sumLCCClass = 0.0;
        LCCClass result = new LCCClass(architecture);

        for (LCCClassComponentResult cls : result.getResults()) {
            sumLCCClass += cls.numberOfConcerns();

        }
        return sumLCCClass;
    }

    //----------------------------------------------------------------------------------
    public double evaluateMACFitness(Architecture architecture) {
        double MACFitness = 0.0;
        double meanNumOps = 0.0;
        double meanDepComps = 0.0;
        double sumCohesion = 0.0;
        double sumClassesDepIn = 0.0;
        double sumClassesDepOut = 0.0;
        double sumDepIn = 0.0;
        double sumDepOut = 0.0;
        double iCohesion = 0.0;


        MeanNumOpsByInterface numOps = new MeanNumOpsByInterface(architecture);
        meanNumOps = numOps.getResults();

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
        } else iCohesion = 1 / sumCohesion;


        MACFitness = meanNumOps + meanDepComps + sumClassesDepIn + sumClassesDepOut + sumDepIn + sumDepOut + (1 / sumCohesion);


        return MACFitness;
    }


    public double evaluateMeanNumOps(Architecture architecture) {
        double meanNumOps = 0.0;
        MeanNumOpsByInterface numOps = new MeanNumOpsByInterface(architecture);
        meanNumOps = numOps.getResults();
        return meanNumOps;
    }

    public double evaluateMeanDepComps(Architecture architecture) {
        double meanDepComps = 0.0;
        MeanDepComponents depComps = new MeanDepComponents(architecture);
        meanDepComps = depComps.getResults();
        return meanDepComps;
    }


    public double evaluateSumClassesDepIn(Architecture architecture) {

        double sumClassesDepIn = 0.0;
        ClassDependencyIn classesDepIn = new ClassDependencyIn(architecture);
        sumClassesDepIn = classesDepIn.getResults();
        return sumClassesDepIn;
    }


    public double evaluateSumClassesDepOut(Architecture architecture) {
        double sumClassesDepOut = 0.0;

        ClassDependencyOut classesDepOut = new ClassDependencyOut(architecture);
        sumClassesDepOut = classesDepOut.getResults();

        return sumClassesDepOut;
    }

    public double evaluateSumDepIn(Architecture architecture) {

        double sumDepIn = 0.0;
        DependencyIn DepIn = new DependencyIn(architecture);
        sumDepIn = DepIn.getResults();
        return sumDepIn;
    }


    public double evaluateSumDepOut(Architecture architecture) {

        double sumDepOut = 0.0;
        DependencyOut DepOut = new DependencyOut(architecture);
        sumDepOut = DepOut.getResults();
        return sumDepOut;
    }

    //---------------------------------------------------------------------------------
    public double evaluateCohesion(Architecture architecture) {
        double sumCohesion = 0.0;
        double iCohesion = 0.0;

        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        sumCohesion = cohesion.getResults();
        if (sumCohesion == 0) {
            iCohesion = 1.0;
        } else iCohesion = 1 / sumCohesion;

        return iCohesion;
    }


}
