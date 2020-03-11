package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClassComponentResult;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.extensibility.ExtensPLA;

import java.util.ArrayList;
import java.util.Collection;

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
        return evaluateATMRElegance(architecture) + evaluateECElegance(architecture)
                + evaluateNACElegance(architecture);
    }

    public static double evaluatePLAExtensibility(Architecture architecture) {
        double ExtensibilityFitness = 0;
        double Extensibility;
        ExtensPLA PLAExtens = new ExtensPLA(architecture);
        ExtensibilityFitness = PLAExtens.getValue();
        if (ExtensibilityFitness == 0)
            Extensibility = 1000;
        else
            Extensibility = 1 / ExtensibilityFitness;
        return (Extensibility);
    }

    public static Double evaluateCIBC(Architecture architecture) {
        Double sumCIBC = 0.0;

        CIBC cibc = new CIBC(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        return sumCIBC;
    }

    public static Double evaluateIIBC(Architecture architecture) {
        double sumIIBC = 0.0;

        IIBC iibc = new IIBC(architecture);
        for (IIBCResult c : iibc.getResults().values()) {
            sumIIBC += c.getInterlacedConcerns().size();
        }

        return sumIIBC;
    }

    public static Double evaluateOOBC(Architecture architecture) {
        double sumOOBC = 0.0;

        OOBC oobc = new OOBC(architecture);
        for (OOBCResult c : oobc.getResults().values()) {
            sumOOBC += c.getInterlacedConcerns().size();
        }
        return sumOOBC;
    }

    public static Double evaluateCDAC(Architecture architecture) {
        double sumCDAC = 0.0;
        CDAC cdac = new CDAC(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }
        return sumCDAC;
    }

    public static Double evaluateCDAI(Architecture architecture) {
        double sumCDAI = 0.0;

        CDAI cdai = new CDAI(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }
        return sumCDAI;
    }

    public static Double evaluateCDAO(Architecture architecture) {
        double sumCDAO = 0.0;
        CDAO cdao = new CDAO(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }
        return sumCDAO;
    }

    public static Double evaluateCDAClass(Architecture architecture) {
        double sumCDAClass = 0.0;

        CDAClass cdaclass = new CDAClass(architecture);
        for (CDAClassResult c : cdaclass.getResults()) {
            sumCDAClass += c.getElements().size();
        }

        return sumCDAClass;
    }

    public static Double evaluateCIBClass(Architecture architecture) {
        double sumCIBClass = 0.0;

        CIBClass cibclass = new CIBClass(architecture);
        for (CIBClassResult c : cibclass.getResults().values()) {
            sumCIBClass += c.getInterlacedConcerns().size();
        }

        return sumCIBClass;
    }

    public static Double evaluateLCCClass(Architecture architecture) {
        double sumLCCClass = 0.0;
        LCCClass result = new LCCClass();
        for (Class cls : architecture.getAllClasses()) {
            result.getResults().add(new LCCClassComponentResult(cls));
        }

        for (LCCClassComponentResult cls : result.getResults()) {
            sumLCCClass += cls.numberOfConcerns();

        }
        return sumLCCClass;
    }


    public static Double evaluateMeanNumOps(Architecture architecture) {
        MeanNumOpsByInterface numOps = new MeanNumOpsByInterface(architecture);
        return numOps.getResults();
    }

    public static Double evaluateMeanDepComps(Architecture architecture) {
        MeanDepComponents depComps = new MeanDepComponents(architecture);
        return depComps.getResults();
    }

    public static int evaluateSumClassesDepIn(Architecture architecture) {
        ClassDependencyIn classesDepIn = new ClassDependencyIn(architecture);
        return classesDepIn.getResults();
    }

    public static int evaluateSumClassesDepOut(Architecture architecture) {
        ClassDependencyOut classesDepOut = new ClassDependencyOut(architecture);
        return classesDepOut.getResults();
    }

    public static double evaluateSumDepIn(Architecture architecture) {
        DependencyIn DepIn = new DependencyIn(architecture);
        return DepIn.getResults();
    }

    public static double evaluateSumDepOut(Architecture architecture) {
        DependencyOut DepOut = new DependencyOut(architecture);
        return DepOut.getResults();
    }

    public static Double evaluateCohesion(Architecture architecture) {
        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        return cohesion.getResults();
    }

    public static Double evaluateICohesion(Double sumCohesion) {
        return sumCohesion == 0 ? 1.0 : 1 / sumCohesion;
    }

    public static double evaluateACOMP(Architecture architecture) {
        double acompFitness = 0.0;
        DependencyIn depIN = new DependencyIn(architecture);
        DependencyOut depOUT = new DependencyOut(architecture);
        acompFitness = depIN.getResults() + depOUT.getResults();
        return acompFitness;
    }

    public static double evaluateACLASS(Architecture architecture) {
        double aclassFitness = 0.0;
        ClassDependencyIn CDepIN = new ClassDependencyIn(architecture);
        ClassDependencyOut CDepOUT = new ClassDependencyOut(architecture);
        aclassFitness = CDepIN.getResults() + CDepOUT.getResults();
        return aclassFitness;
    }

    public static double evaluateTAM(Architecture architecture) {
        double tamFitness = 0.0;
        MeanNumOpsByInterface NumOps = new MeanNumOpsByInterface(architecture);

        tamFitness = NumOps.getResults();
        return tamFitness;
    }

    public static double evaluateDC(Architecture architecture) {
        double dcFitness = 0.0;
        double sumCDAC = 0.0;
        double sumCDAI = 0.0;
        double sumCDAO = 0.0;

        CDAI cdai = new CDAI(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }

        CDAO cdao = new CDAO(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }

        CDAC cdac = new CDAC(architecture);
        for (CDACResult c : cdac.getResults()) {
            sumCDAC += c.getElements().size();
        }

        dcFitness = sumCDAI + sumCDAO + sumCDAC;
        return dcFitness;
    }

    public static double evaluateEC(Architecture architecture) {
        double ecFitness = 0.0;
        double sumCIBC = 0.0;
        double sumIIBC = 0.0;
        double sumOOBC = 0.0;

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

        ecFitness = sumCIBC + sumIIBC + sumOOBC;
        return ecFitness;
    }


    //addYni
    public static double evaluateWocsC(Architecture architecture) {
        float valorwocsc;
        float tcomplexidade = 0;
        float numclass = architecture.getAllClasses().size();

        for (Package pacote : architecture.getAllPackages()) {

            for (Class classes : pacote.getAllClasses()) {
                int cantparame = 0;
                int complexidade = 0;


                for (Method metodo : classes.getAllMethods()) {

                    cantparame = metodo.getParameters().size() + 1;
                    complexidade += cantparame;
                }

                tcomplexidade = complexidade;
            }
        }
        valorwocsc = tcomplexidade / numclass;
        return valorwocsc;
    }


    public static double evaluateWocsI(Architecture architecture) {
        double wocsiFitness = 0;

        float valorwocsi = 0;
        float tcomplexidade = 0;
        float numclass = architecture.getAllInterfaces().size();

        for (Package pacote : architecture.getAllPackages()) {

            for (Interface interfa : pacote.getAllInterfaces()) {
                int cantparame = 0;
                int complexidade = 0;

                for (Method metodo : interfa.getOperations()) {

                    cantparame = metodo.getParameters().size() + 1;
                    complexidade += cantparame;
                }

                tcomplexidade = complexidade;

            }

        }
        valorwocsi = tcomplexidade / numclass;
        return valorwocsi;
    }


    public static double evaluateCbcs(Architecture architecture) {
        double cbcsFitness = 0;
        float valorcbcs = 0;
        float numinterface = architecture.getAllInterfaces().size();

        for (Interface interf : architecture.getAllInterfaces()) {
            valorcbcs += interf.getRelationships().size();
        }

        return valorcbcs / numinterface;
    }


    public static double evaluateSvc(Architecture architecture) {
        float results;

        float tcommoncomp = 0;
        float tvariablecomp = 0;

        for (Package pacote : architecture.getAllPackages()) {

            int variablecomp = 0;

            for (Element elemento : pacote.getElements()) {
                if (elemento.getVariationPoint() != null) {
                    variablecomp = 1;
                }
            }

            if (variablecomp == 1) {
                tvariablecomp++;
            } else {
                tcommoncomp++;
            }

        }

        float denominador = tcommoncomp + tvariablecomp;

        if (denominador == 0) {
            results = 0;
        } else {
            results = tvariablecomp / denominador;
        }
        return results;
    }

    public static double evaluateSsc(Architecture architecture) {
        float tcommoncomp = 0;
        float results;
        float tvariablecomp = 0;

        for (Package pacote : architecture.getAllPackages()) {
            int variablecomp = 0;
            for (Element elemento : pacote.getElements()) {
                if (elemento.getVariationPoint() != null) {
                    variablecomp = 1;
                    break;
                }
            }
            if (variablecomp == 1) {
                tvariablecomp++;
            } else {
                tcommoncomp++;
            }
        }

        if (tcommoncomp == 0) {
            results = 0;
        } else {
            results = 1 / (tcommoncomp / (tvariablecomp + tcommoncomp));
        }

        return results;
    }

    public static double evaluateAv(Architecture architecture) {
        int a = 0;
        int compvariable = cantComponetvariable(architecture);

        for (Package pacote : architecture.getAllPackages()) {

            int compcomposto = 0;

            for (Element elemento : pacote.getElements()) {
                if (elemento.getTypeElement().equals("package")) {
                    compcomposto++;
                }
            }

            if (compcomposto != 0) {
                a = compcomposto;
            } else {
                a = compvariable;
            }
        }

        return compvariable + a;
    }


    private static int cantComponetvariable(Architecture architecture) {

        //private int tcompvariable;
        int cantvcomponet = 0;

        for (Package pacote : architecture.getAllPackages()) {

            int variablecomp = 0;

            for (Element elemento : pacote.getElements()) {

                if (elemento.getVariationPoint() != null) {
                    variablecomp = 1;
                }
            }

            if (variablecomp == 1) {
                cantvcomponet++;
            }
        }

        return cantvcomponet;
    }


    public static double evaluateDepIN(Architecture architecture) {
        ClassDependencyIn depIn = new ClassDependencyIn(architecture);
        return depIn.getResults();
    }

    public static double evaluateMSIFitness(Architecture architecture) {
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

        sumLCC = evaluateLCC(architecture);

        sumLCCClass = evaluateLCCClass(architecture);

        CIBC cibc = new CIBC(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        CIBClass cibclass = new CIBClass(architecture);
        for (CIBClassResult c : cibclass.getResults().values()) {
            sumCIBClass += c.getInterlacedConcerns().size();
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

        CDAClass cdaclass = new CDAClass(architecture);
        for (CDAClassResult c : cdaclass.getResults()) {
            sumCDAClass += c.getElements().size();
        }

        CDAI cdai = new CDAI(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }

        CDAO cdao = new CDAO(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }

        MSIFitness = sumLCC + sumLCCClass + sumCDAC + sumCDAClass + sumCDAI
                + sumCDAO + sumCIBC + sumCIBClass + sumIIBC + sumOOBC;

        return MSIFitness;
    }

    public static double evaluateLCC(Architecture architecture) {
        double sumLCC = 0.0;
        Collection<LCCComponentResult> results = new ArrayList<>();
        for (Package component : architecture.getAllPackages()) {
            results.add(new LCCComponentResult(component));
        }

        for (LCCComponentResult component : results) {
            sumLCC += component.numberOfConcerns();

        }
        return sumLCC;
    }

    // ----------------------------------------------------------------------------------
    public static Double evaluateMACFitness(Architecture architecture) {
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


    // ---------------------------------------------------------------------------------
    public static double evaluateCohesionFitness(Architecture architecture) {
        double sumCohesion = 0.0;
        double Cohesion = 0.0;

        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        sumCohesion = cohesion.getResults();
        Cohesion = (1 / sumCohesion);
        return Cohesion;
    }


    private double evaluateMSIFitnessDesignOutset(Architecture architecture) {
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

        sumLCC = evaluateLCC(architecture);

        sumLCCClass = evaluateLCCClass(architecture);

        CIBC cibc = new CIBC(architecture);
        for (CIBCResult c : cibc.getResults().values()) {
            sumCIBC += c.getInterlacedConcerns().size();
        }

        CIBClass cibclass = new CIBClass(architecture);
        for (CIBClassResult c : cibclass.getResults().values()) {
            sumCIBClass += c.getInterlacedConcerns().size();
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

        CDAClass cdaclass = new CDAClass(architecture);
        for (CDAClassResult c : cdaclass.getResults()) {
            sumCDAClass += c.getElements().size();
        }

        CDAI cdai = new CDAI(architecture);
        for (CDAIResult c : cdai.getResults()) {
            sumCDAI += c.getElements().size();
        }

        CDAO cdao = new CDAO(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }

        MSIFitness = sumLCC + sumLCCClass + sumCDAC + sumCDAClass + sumCDAI + sumCDAO + sumCIBC + sumCIBClass + sumIIBC
                + sumOOBC;
        return MSIFitness;
    }


}
