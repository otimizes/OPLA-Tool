package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Fitness;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Metrics;
import br.ufpr.dinf.gres.core.jmetal4.metrics.all.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAO;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CIBClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.IIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.OOBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.all.PLAExtensibility;

import java.util.ArrayList;
import java.util.List;

public class MetricsEvaluation {

    public static List<Fitness> evaluate(List<String> selectedMetrics, Solution solution) {
        List<Fitness> fitnesses = new ArrayList<>();

        for (String selectedMetric : selectedMetrics) {
            Metrics metric = Metrics.valueOf(selectedMetric);
            switch (metric) {
                case ACLASS:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateACLASS((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case ACOMP:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateACOMP((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case AV:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateAV((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case CBCS:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateCBCS((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case COE:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateCOE((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case CONVENTIONAL:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateCONVENTIONAL((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case DC:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateDC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case EC:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateEC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case ELEGANCE:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateELEGANCE((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case FM:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateFM((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case LCC:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateLCC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case MSIDESIGNOUTSET:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateMSIDESIGNOUTSET((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case PLAEXTENSIBILITY:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluatePLAEXTENSIBILITY((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case SSC:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateSSC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case SVC:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateSVC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case TAM:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateTAM((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case WOCSCLASS:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateWOCSCLASS((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case WOCSINTERFACE:
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateWOCSINTERFFACE((Architecture) solution.getDecisionVariables()[0])));
                    break;
                default:
            }
        }
        return fitnesses;
    }

    public static Double evaluateATMRElegance(Architecture architecture) {
        return new ATMRElegance(architecture).getResults();
    }

    public static Double evaluateECElegance(Architecture architecture) {
        return new ECElegance(architecture).getResults();
    }

    public static Double evaluateNACElegance(Architecture architecture) {
        return new NACElegance(architecture).getResults();
    }

    public static Double evaluateELEGANCE(Architecture architecture) {
        return new Elegance(architecture).getResults();
    }

    public static Double evaluatePLAEXTENSIBILITY(Architecture architecture) {
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

    public static Double evaluateSumClassesDepIn(Architecture architecture) {
        return new ClassDependencyIn(architecture).getResults();
    }

    public static Double evaluateSumClassesDepOut(Architecture architecture) {
        return new ClassDependencyOut(architecture).getResults();
    }

    public static Double evaluateSumDepIn(Architecture architecture) {
        return new DependencyIn(architecture).getResults();
    }

    public static Double evaluateSumDepOut(Architecture architecture) {
        return new DependencyOut(architecture).getResults();
    }

    public static Double evaluateHCohesion(Architecture architecture) {
        return new RelationalCohesion(architecture).getResults();
    }

    public static Double evaluateACOMP(Architecture architecture) {
        return new ACOMP(architecture).getResults();
    }

    public static Double evaluateACLASS(Architecture architecture) {
        return new ACLASS(architecture).getResults();
    }

    public static Double evaluateTAM(Architecture architecture) {
        return new TAM(architecture).getResults();
    }

    public static Double evaluateDC(Architecture architecture) {
        return new DC(architecture).getResults();
    }

    public static Double evaluateEC(Architecture architecture) {
        return new EC(architecture).getResults();
    }

    public static Double evaluateWOCSCLASS(Architecture architecture) {
        return new WOCSClass(architecture).getResults();
    }


    public static Double evaluateWOCSINTERFFACE(Architecture architecture) {
        return new WOCSInterface(architecture).getResults();
    }


    public static Double evaluateCBCS(Architecture architecture) {
        return new CBCS(architecture).getResults();
    }

    public static Double evaluateSVC(Architecture architecture) {
        return new SVC(architecture).getResults();
    }

    public static Double evaluateSSC(Architecture architecture) {
        return new SSC(architecture).getResults();
    }

    public static Double evaluateAV(Architecture architecture) {
        return new AV(architecture).getResults();
    }

    public static Double evaluateDepIN(Architecture architecture) {
        return new ClassDependencyIn(architecture).getResults();
    }

    public static Double evaluateFM(Architecture architecture) {
        return new FM(architecture).getResults();
    }

    public static Double evaluateLCC(Architecture architecture) {
        return new LCC(architecture).getResults();
    }

    public static Double evaluateCONVENTIONAL(Architecture architecture) {
        return new Conventional(architecture).getResults();
    }

    public static Double evaluateCOE(Architecture architecture) {
        return new COE(architecture).getResults();
    }

    public static Double evaluateMSIDESIGNOUTSET(Architecture architecture) {
        return new MSIDesignOutset(architecture).getResults();
    }
}
