package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.classical.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAO;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CIBClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.IIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.OOBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.extensibility.PLAExtensibility;

public class MetricsEvaluation {
    //addYni

    public static double evaluateATMRElegance(Architecture architecture) {
        return new ATMRElegance(architecture).getResults();
    }

    public static double evaluateECElegance(Architecture architecture) {
        return new ECElegance(architecture).getResults();
    }

    public static double evaluateNACElegance(Architecture architecture) {
        return new NACElegance(architecture).getResults();
    }

    public static double evaluateElegance(Architecture architecture) {
        return new Elegance(architecture).getResults();
    }

    public static double evaluatePLAExtensibility(Architecture architecture) {
        return new PLAExtensibility(architecture).getResults();
    }

    public static Double evaluateCIBC(Architecture architecture) {
        return new CIBC(architecture).getResults();
    }

    public static Double evaluateIIBC(Architecture architecture) {
        return new IIBC(architecture).getResults();
    }

    public static Double evaluateOOBC(Architecture architecture) {
        return new OOBC(architecture).getResults();
    }

    public static Double evaluateCDAC(Architecture architecture) {
        return new CDAC(architecture).getResults();
    }

    public static Double evaluateCDAI(Architecture architecture) {
        return new CDAC(architecture).getResults();
    }

    public static Double evaluateCDAO(Architecture architecture) {
        return new CDAO(architecture).getResults();
    }

    public static Double evaluateCDAClass(Architecture architecture) {
        return new CDAClass(architecture).getResults();
    }

    public static Double evaluateCIBClass(Architecture architecture) {
        return new CIBClass(architecture).getResults();
    }

    public static Double evaluateLCCClass(Architecture architecture) {
        return new LCCClass(architecture).getResults();
    }

    public static Double evaluateMeanNumOps(Architecture architecture) {
        return new MeanNumOpsByInterface(architecture).getResults();
    }

    public static Double evaluateMeanDepComps(Architecture architecture) {
        return new MeanDepComponents(architecture).getResults();
    }

    public static double evaluateSumClassesDepIn(Architecture architecture) {
        return new ClassDependencyIn(architecture).getResults();
    }

    public static double evaluateSumClassesDepOut(Architecture architecture) {
        return new ClassDependencyOut(architecture).getResults();
    }

    public static double evaluateSumDepIn(Architecture architecture) {
        return new DependencyIn(architecture).getResults();
    }

    public static double evaluateSumDepOut(Architecture architecture) {
        return new DependencyOut(architecture).getResults();
    }

    public static Double evaluateCohesion(Architecture architecture) {
        return new RelationalCohesion(architecture).getResults();
    }

    public static double evaluateACOMP(Architecture architecture) {
        return new ACOMP(architecture).getResults();
    }

    public static double evaluateACLASS(Architecture architecture) {
        return new ACLASS(architecture).getResults();
    }

    public static double evaluateTAM(Architecture architecture) {
        return new TAM(architecture).getResults();
    }

    public static double evaluateDC(Architecture architecture) {
        return new DC(architecture).getResults();
    }

    public static double evaluateEC(Architecture architecture) {
        return new EC(architecture).getResults();
    }

    public static double evaluateWocsC(Architecture architecture) {
        return new WOCSClass(architecture).getResults();
    }


    public static double evaluateWocsI(Architecture architecture) {
        return new WOCSInterface(architecture).getResults();
    }


    public static double evaluateCbcs(Architecture architecture) {
        return new CBCS(architecture).getResults();
    }


    public static double evaluateSvc(Architecture architecture) {
        return new SVC(architecture).getResults();
    }

    public static double evaluateSsc(Architecture architecture) {
        return new SSC(architecture).getResults();
    }

    public static double evaluateAv(Architecture architecture) {
        return new AV(architecture).getResults();
    }

    public static double evaluateDepIN(Architecture architecture) {
        return new ClassDependencyIn(architecture).getResults();
    }

    public static double evaluateMSIFitness(Architecture architecture) {
        return new MSI(architecture).getResults();
    }

    public static double evaluateLCC(Architecture architecture) {
        return new LCC(architecture).getResults();
    }

    public static Double evaluateMACFitness(Architecture architecture) {
        return new MAC(architecture).getResults();
    }

    public static double evaluateCohesionFitness(Architecture architecture) {
        return new COE(architecture).getResults();
    }

    private double evaluateMSIFitnessDesignOutset(Architecture architecture) {
        return new MSIDesignOutset(architecture).getResults();
    }
}
