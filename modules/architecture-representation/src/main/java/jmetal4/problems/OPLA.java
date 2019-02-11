package jmetal4.problems;

import arquitetura.builders.ArchitectureBuilder;
import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.*;
import jmetal4.core.Problem;
import jmetal4.core.Solution;
import jmetal4.core.SolutionSet;
import jmetal4.encodings.solutionType.ArchitectureSolutionType;
import jmetal4.metrics.PLAMetrics.extensibility.ExtensPLA;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCC;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClass;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCClassComponentResult;
import jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;
import jmetal4.metrics.concernDrivenMetrics.concernDiffusion.*;
import jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns.*;
import jmetal4.metrics.conventionalMetrics.*;
import jmetal4.util.JMException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

//criado por Thelma em agosto/2012
public class OPLA extends Problem {

    private static final long serialVersionUID = 884633138619836573L;

    // variaveis para controlar os contadores de componentes e interfaces
    public static int contComp_ = 0;
    public static int contInt_ = 0;
    public static int contClass_ = 0;
    public static int contDiscardedSolutions_ = 0;

    public Architecture architecture_;

    public OPLA(String xmiFilePath) throws Exception {

        numberOfVariables_ = 1;
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        problemName_ = "OPLA";
        solutionType_ = new ArchitectureSolutionType(this);
        variableType_ = new java.lang.Class[numberOfVariables_];
        length_ = new int[numberOfVariables_];
        variableType_[0] = java.lang.Class
                .forName(Architecture.ARCHITECTURE_TYPE);

        architecture_ = new ArchitectureBuilder().create(xmiFilePath);
    }

    public OPLA() {

    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

    @Override
    public void evaluate(Solution solution) {
        double fitness0 = 0.0;
        double fitness1 = 0.0;
//		double fitness2 = 0.0;

        // double fitness3 = 0.0;

        // DesignOutset
        fitness0 = evaluateMSIFitnessDesignOutset((Architecture) solution
                .getDecisionVariables()[0]);
        // fitness0 = evaluateMSIFitness((Architecture)
        // solution.getDecisionVariables()[0]);
        fitness1 = evaluateMACFitness((Architecture) solution
                .getDecisionVariables()[0]);
        // fitness2 = evaluateElegance((Architecture)
        // solution.getDecisionVariables()[0]);
        // fitness2 = evaluatePLAExtensibility((Architecture)
        // solution.getDecisionVariables()[0]);

        solution.setObjective(0, fitness0);
        solution.setObjective(1, fitness1);
        // solution.setObjective(2, fitness2);
        // solution.setObjective(3, fitness3);

    }

    private double evaluateElegance(Architecture architecture) {
        double EleganceFitness = 0.0;
        ECElegance EC = new ECElegance(architecture);
        ATMRElegance ATMR = new ATMRElegance(architecture);
        NACElegance NAC = new NACElegance(architecture);
        EleganceFitness = EC.getResults() + ATMR.getResults()
                + NAC.getResults();
        return EleganceFitness;
    }

    private double evaluateMSIFitness(Architecture architecture) {
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

        MSIFitness = sumLCC + sumCDAC + sumCDAI + sumCDAO + sumCIBC + sumIIBC
                + sumOOBC;
        return MSIFitness;
    }

    private double evaluateLCC(Architecture architecture) {
        double sumLCC = 0.0;
        LCC result = new LCC(architecture);

        for (LCCComponentResult component : result.getResults()) {
            sumLCC += component.numberOfConcerns();

        }
        return sumLCC;
    }

    // ----------------------------------------------------------------------------------
    private double evaluateMACFitness(Architecture architecture) {
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
        } else
            iCohesion = 1 / sumCohesion;

        // System.out.println("MeanNumOps: "+meanNumOps);
        // System.out.println("meanDepComps: "+meanDepComps);
        // System.out.println("sumClassesDepIn: "+sumClassesDepIn);
        // System.out.println("sumClassesDepOut: "+sumClassesDepOut);
        // System.out.println("sumDepIn: "+sumDepIn);
        // System.out.println("sumDepOut: "+sumDepOut);
        // System.out.println("sumCohesion: "+iCohesion);
        //

        // MACFitness = meanNumOps + meanDepComps + sumClassesDepIn +
        // sumClassesDepOut + sumDepIn + sumDepOut + (1 / sumCohesion);
        // Design Outset
        MACFitness = sumClassesDepIn + sumClassesDepOut + sumDepIn + sumDepOut
                + iCohesion;

        return MACFitness;
    }

    // ---------------------------------------------------------------------------------
    private double evaluateCohesionFitness(Architecture architecture) {
        double sumCohesion = 0.0;
        double Cohesion = 0.0;

        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        sumCohesion = cohesion.getResults();
        Cohesion = (1 / sumCohesion);
        return Cohesion;
    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public SolutionSet removeDominadas(SolutionSet result) {
        boolean dominador, dominado;
        double valor1 = 0;
        double valor2 = 0;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                dominador = true;
                dominado = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
                    valor1 = result.get(i).getObjective(k);
                    valor2 = result.get(j).getObjective(k);

                    if (valor1 > valor2 || dominador == false) {
                        dominador = false;
                    } else if (valor1 <= valor2) {
                        dominador = true;
                    }

                    if (valor2 > valor1 || dominado == false) {
                        dominado = false;
                    } else if (valor2 < valor1) {
                        dominado = true;
                    }
                }

                if (dominador) {
                    // System.out.println("--------------------------------------------");
                    // System.out.println("Solucao [" + i +
                    // "] domina a Solucao [" + j + "]");
                    // System.out.println("[" + i + "] " +
                    // result.get(i).toString());
                    // System.out.println("[" + j + "] " +
                    // result.get(j).toString());

                    result.remove(j);
                    System.out.println("removido Dominada");
                    j = j - 1;
                } else if (dominado) {
                    // System.out.println("--------------------------------------------");
                    // System.out.println("Solucao [" + j +
                    // "] domina a Solucao [" + i + "]");
                    // System.out.println("[" + i + "] " +
                    // result.get(i).toString());
                    // System.out.println("[" + j + "] " +
                    // result.get(j).toString());

                    result.remove(i);
                    System.out.println("removido Dominada");
                    j = i;
                }
            }
        }

        return result;
    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public SolutionSet removeRepetidas(SolutionSet result) {
        String solucao;

        for (int i = 0; i < result.size() - 1; i++) {
            solucao = result.get(i).getDecisionVariables()[0].toString();
            for (int j = i + 1; j < result.size(); j++) {
                if (solucao.equals(result.get(j).getDecisionVariables()[0].toString())) {
                    result.remove(j);
                    System.out.println("removido Repedita");
                }
            }
        }

        return result;
    }

    // método para verificar se algum dos relacionamentos recebidos �
    // generaliza��o
    private boolean searchForGeneralizations(Class cls) { // ok
        Collection<Relationship> Relationships = cls.getRelationships();
        for (Relationship relationship : Relationships) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getChild().equals(cls)
                        || generalization.getParent().equals(cls))
                    return true;
            }
        }
        return false;
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

        MSIFitness = sumLCC + sumLCCClass + sumCDAC + sumCDAClass + sumCDAI
                + sumCDAO + sumCIBC + sumCIBClass + sumIIBC + sumOOBC;
        return MSIFitness;
    }

    private double evaluateLCCClass(Architecture architecture) {
        double sumLCCClass = 0.0;
        LCCClass result = new LCCClass(architecture);

        for (LCCClassComponentResult cls : result.getResults()) {
            sumLCCClass += cls.numberOfConcerns();

        }
        return sumLCCClass;
    }

    private float evaluatePLAExtensibility(Architecture architecture) {
        float ExtensibilityFitness = 0;
        float Extensibility;
        ExtensPLA PLAExtens = new ExtensPLA(architecture);
        ExtensibilityFitness = PLAExtens.getValue();
        if (ExtensibilityFitness == 0)
            Extensibility = 1000;
        else
            Extensibility = 1 / ExtensibilityFitness;
        return (Extensibility);
    }

    private void removeComponentRelationships(Package comp, Architecture architecture) {
        //
        Relationship[] allInterElementRelationships = architecture.getRelationshipHolder().getAllRelationships().toArray(new Relationship[0]);
        for (Relationship relationship : allInterElementRelationships) {
            if (relationship instanceof AbstractionRelationship) {
                AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
                if (abstraction.getClient().equals(comp)) {
                    architecture.removeRelationship(relationship);
                }
            }
            if (relationship instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) relationship;
                if (dependency.getClient().equals(comp)) {
                    architecture.removeRelationship(relationship);
                }
            }
        }
    }

    private void removeClassRelationships(Class cls, Architecture architecture) {
        List<Relationship> relationshipsCls = new ArrayList<Relationship>(
                cls.getRelationships());
        if (!relationshipsCls.isEmpty()) {
            Iterator<Relationship> iteratorRelationships = relationshipsCls
                    .iterator();
            while (iteratorRelationships.hasNext()) {
                Relationship relationship = iteratorRelationships.next();

                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient().equals(cls)
                            || dependency.getSupplier().equals(cls))
                        architecture.removeRelationship(dependency);
                }

                if (relationship instanceof AssociationRelationship) {
                    AssociationRelationship association = (AssociationRelationship) relationship;
                    for (AssociationEnd associationEnd : association
                            .getParticipants()) {
                        if (associationEnd.getCLSClass().equals(cls)) {
                            architecture.removeRelationship(association);
                            break;
                        }
                    }
                }

                if (relationship instanceof GeneralizationRelationship) {
                    GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                    if ((generalization.getChild().equals(cls))
                            || (generalization.getParent().equals(cls))) {
                        architecture.removeRelationship(generalization);

                    }
                }

            }
        }
    }

    public void evaluateConstraints(Solution solution) throws JMException {
        List<Package> allComponents = new ArrayList<Package>(((Architecture) solution.getDecisionVariables()[0]).getAllPackages());
        for (Package comp : allComponents) {
            List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());
            if (!(allClasses.isEmpty())) {
                Iterator<Class> iteratorClasses = allClasses.iterator();

                while (iteratorClasses.hasNext()) {
                    Class cls = iteratorClasses.next();
                    if ((cls.getAllAttributes().isEmpty())
                            && (cls.getAllMethods().isEmpty())
                            && (cls.getImplementedInterfaces().isEmpty())
                            && !(searchForGeneralizations(cls))
                            && (cls.getVariantType() == null)) {
                        comp.removeClass(cls);
                        this.removeClassRelationships(cls, (Architecture) solution.getDecisionVariables()[0]);
                    }
                }
            }

            List<Interface> allItfsComp = new ArrayList<Interface>(comp.getImplementedInterfaces());
            if (!(allItfsComp.isEmpty())) {
                Iterator<Interface> iteratorInterfaces = allItfsComp.iterator();
                while (iteratorInterfaces.hasNext()) {
                    Interface itf = iteratorInterfaces.next();
                    boolean ultimaInterface = false;
                    if (comp.getImplementedInterfaces().size() == 1)
                        ultimaInterface = true;
                    if (itf.getOperations().isEmpty() && !ultimaInterface) {
                        ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
                        // this.removeInterfaceRelationships(itf, (Architecture)
                        // solution.getDecisionVariables()[0]);
                    }
                    if (itf.getOperations().isEmpty() && ultimaInterface && comp.getAllClasses().size() < 1) {
                        ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
                    }
                }
            }
            allItfsComp.clear();

            Iterator<Interface> iteratorInterfaces = ((Architecture) solution.getDecisionVariables()[0]).getAllInterfaces().iterator();
            while (iteratorInterfaces.hasNext()) {
                Interface itf = iteratorInterfaces.next();
                boolean ultimaInterface = false;
                if (comp.getImplementedInterfaces().size() == 1)
                    ultimaInterface = true;
                if (itf.getOperations().isEmpty() && !ultimaInterface) {
                    ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
                    // this.removeInterfaceRelationships(itf, (Architecture)
                    // solution.getDecisionVariables()[0]);
                }
                if (itf.getOperations().isEmpty() && ultimaInterface && comp.getAllClasses().size() < 1) {
                    ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
                }
            }

            if (comp.getAllClasses().isEmpty() && comp.getImplementedInterfaces().isEmpty() && comp.getAllInterfaces().isEmpty()) {
                this.removeComponentRelationships(comp, (Architecture) solution.getDecisionVariables()[0]);
                ((Architecture) solution.getDecisionVariables()[0]).removePackage(comp);
            }
        }
        allComponents.clear();

    }

    public double evaluateACOMP(Architecture architecture) {
        double acompFitness = 0.0;
        DependencyIn depIN = new DependencyIn(architecture);
        DependencyOut depOUT = new DependencyOut(architecture);
        acompFitness = depIN.getResults() + depOUT.getResults();
        return acompFitness;
    }

    public double evaluateACLASS(Architecture architecture) {
        double aclassFitness = 0.0;
        ClassDependencyIn CDepIN = new ClassDependencyIn(architecture);
        ClassDependencyOut CDepOUT = new ClassDependencyOut(architecture);
        aclassFitness = CDepIN.getResults() + CDepOUT.getResults();
        return aclassFitness;
    }

    public double evaluateTAM(Architecture architecture) {
        double tamFitness = 0.0;
        MeanNumOpsByInterface NumOps = new MeanNumOpsByInterface(architecture);

        tamFitness = NumOps.getResults();
        return tamFitness;
    }

    public double evaluateCOE(Architecture architecture) {
        double coeFitness = 0.0;
        double sumLCC = 0.0;

        RelationalCohesion rc = new RelationalCohesion(architecture);


        LCC lcc = new LCC(architecture);
        for (LCCComponentResult c : lcc.getResults()) {
            sumLCC += c.numberOfConcerns();
        }

        coeFitness = rc.getResults() + sumLCC;
        return coeFitness;
    }

    public double evaluateDC(Architecture architecture) {
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

    public double evaluateEC(Architecture architecture) {
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
}
