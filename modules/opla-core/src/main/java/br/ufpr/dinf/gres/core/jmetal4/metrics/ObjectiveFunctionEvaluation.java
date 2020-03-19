package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Fitness;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Metrics;
import br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CDAO;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.CIBClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.CIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.IIBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.OOBC;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions.EXT;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveFunctionEvaluation {

    public static List<Fitness> evaluate(List<String> selectedMetrics, Solution solution) {
        List<Fitness> fitnesses = new ArrayList<>();

        for (String selectedMetric : selectedMetrics) {
            Metrics metric = Metrics.valueOf(selectedMetric);
            switch (metric) {
                case ACLASS:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateACLASS((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case ACOMP:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateACOMP((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case TV:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateTV((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case CBCS:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateCBCS((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case COE:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateCOE((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case CM:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateCONVENTIONAL((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case DC:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateDC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case EC:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateEC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case ELEG:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateELEG((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case FM:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateFM((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case LCC:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateLCC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case EXT:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateEXT((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case SD:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateSD((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case SV:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateSV((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case TAM:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateTAM((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case WOCSCLASS:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateWOCSCLASS((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case WOCSINTERFACE:
                    fitnesses.add(new Fitness(ObjectiveFunctionEvaluation.evaluateWOCSINTERFFACE((Architecture) solution.getDecisionVariables()[0])));
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

    public static Double evaluateELEG(Architecture architecture) {
        return new ELEG(architecture).getResults();
    }

    public static Double evaluateEXT(Architecture architecture) {
        return new EXT(architecture).getResults();
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
        return new WOCSCLASS(architecture).getResults();
    }


    public static Double evaluateWOCSINTERFFACE(Architecture architecture) {
        return new WOCSINTERFACE(architecture).getResults();
    }


    public static Double evaluateCBCS(Architecture architecture) {
        return new CBCS(architecture).getResults();
    }

    public static Double evaluateSV(Architecture architecture) {
        return new SV(architecture).getResults();
    }

    public static Double evaluateSD(Architecture architecture) {
        return new SD(architecture).getResults();
    }

    public static Double evaluateTV(Architecture architecture) {
        return new TV(architecture).getResults();
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
        return new CM(architecture).getResults();
    }

    public static Double evaluateCOE(Architecture architecture) {
        return new COE(architecture).getResults();
    }

    public static Double evaluateMSIDESIGNOUTSET(Architecture architecture) {
        return new MSIDESIGNOUTSET(architecture).getResults();
    }
}
