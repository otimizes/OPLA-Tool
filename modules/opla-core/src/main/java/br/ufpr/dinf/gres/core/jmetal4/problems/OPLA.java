package br.ufpr.dinf.gres.core.jmetal4.problems;

import br.ufpr.dinf.gres.architecture.builders.ArchitectureBuilder;
import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.relationship.*;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Problem;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.encodings.solutionType.ArchitectureSolutionType;
import br.ufpr.dinf.gres.core.jmetal4.experiments.ExperimentCommomConfigs;
import br.ufpr.dinf.gres.core.jmetal4.experiments.Fitness;
import br.ufpr.dinf.gres.core.jmetal4.metrics.MetricsEvaluation;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

//criado por Thelma em agosto/2012
public class OPLA extends Problem {

    private static final Logger LOGGER = Logger.getLogger(OPLA.class);
    private static final long serialVersionUID = 884633138619836573L;

    // variaveis para controlar os contadores de componentes e interfaces
    public static int contComp_ = 0;
    public static int contInt_ = 0;
    public static int contClass_ = 0;
    public static int contDiscardedSolutions_ = 0;

    public Architecture architecture_;
    private List<String> selectedMetrics; // Vai vir da GUI
    private ExperimentCommomConfigs configs;

    public OPLA() {
    }

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


    public OPLA(String xmiFilePath, ExperimentCommomConfigs oplaConfig) throws Exception {
        LOGGER.info("Setando configurações");
        this.configs = oplaConfig;
        numberOfVariables_ = 1;
        LOGGER.info("Recuperando Numero de funcoes objetivo");
        numberOfObjectives_ = oplaConfig.getOplaConfigs().getNumberOfObjectives();
        numberOfConstraints_ = 0;
        problemName_ = "OPLA";
        LOGGER.info("Criando Architecture Solution Type");
        solutionType_ = new ArchitectureSolutionType(this);
        variableType_ = new java.lang.Class[numberOfVariables_];
        length_ = new int[numberOfVariables_];
        LOGGER.info("Criando ARCHITECTURE_TYPE");
        variableType_[0] = java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE);
        LOGGER.info("Instanciando Builder");
        ArchitectureBuilder architectureBuilder = new ArchitectureBuilder();
        LOGGER.info("Construindo br.ufpr.dinf.gres.arquitetura by XML: " + xmiFilePath);
        architecture_ = architectureBuilder.create(xmiFilePath);
        LOGGER.info("Recuperando Metricas");
        selectedMetrics = oplaConfig.getOplaConfigs().getSelectedObjectiveFunctions();
    }

    @Override
    public void evaluate(Solution solution) {
        LOGGER.info("evaluate()");
        List<Fitness> fitnesses = new ArrayList<Fitness>();

        for (int i = 0; i < this.selectedMetrics.size(); i++) {
            String metric = this.selectedMetrics.get(i);

            switch (metric) {
                case "elegance":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateElegance((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "conventional":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateMACFitness((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "featureDriven":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateMSIFitness((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "PLAExtensibility":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluatePLAExtensibility((Architecture) solution.getDecisionVariables()[0])));
                    break;
                //implementado por marcelo
                case "acomp":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateACOMP((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "aclass":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateACLASS((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "tam":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateTAM((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "coe":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateCOE((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "dc":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateDC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "ec":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateEC((Architecture) solution.getDecisionVariables()[0])));
                    //addYni
                case "wocsclass":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateWocsC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "wocsinterface":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateWocsI((Architecture) solution.getDecisionVariables()[0])));
                    break;

                case "cbcs":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateCbcs((Architecture) solution.getDecisionVariables()[0])));
                    break;

                case "svc":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateSvc((Architecture) solution.getDecisionVariables()[0])));
                    break;

                case "ssc":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateSsc((Architecture) solution.getDecisionVariables()[0])));
                    break;
                case "av":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateAv((Architecture) solution.getDecisionVariables()[0])));
                    break;
                //addYni
                case "lcc":
                    fitnesses.add(new Fitness(MetricsEvaluation.evaluateLCC((Architecture) solution.getDecisionVariables()[0])));
                    break;
                default:
            }
        }

        for (int i = 0; i < fitnesses.size(); i++) {
            solution.setObjective(i, fitnesses.get(i).getValue());
        }

    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public SolutionSet removeDominadas(SolutionSet result) {
        List<Solution> collect = result.getSolutionSet().stream().filter(r -> (r.getEvaluation() >= 5)).collect(Collectors.toList());
        LOGGER.info("removeDominadas()");
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
                    this.configs.getLogger().putLog("removido Dominada");
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
                    this.configs.getLogger().putLog("removido Dominada");
                    j = i;
                }
            }
        }

        if (collect != null) {
            result.setCapacity(result.getCapacity() + collect.size());
            collect.forEach(c -> {
                if (!result.getSolutionSet().contains(c)) {
                    result.add(c);
                }
            });
        }
        return result;
    }

    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --
    public SolutionSet removeRepetidas(SolutionSet result) {
        List<Solution> collect = result.getSolutionSet().stream().filter(r -> (r.getEvaluation() >= 5)).collect(Collectors.toList());
        LOGGER.info("removeRepetidas()");
        String solucao;

        for (int i = 0; i < result.size() - 1; i++) {
            solucao = result.get(i).getDecisionVariables()[0].toString();
            for (int j = i + 1; j < result.size(); j++) {
                if (solucao.equals(result.get(j).getDecisionVariables()[0].toString())) {
                    result.remove(j);
                    this.configs.getLogger().putLog("removido Repedita");
                }
            }
        }

        if (collect != null) {
            result.setCapacity(result.getCapacity() + collect.size());
            collect.forEach(c -> {
                if (!result.getSolutionSet().contains(c)) {
                    result.add(c);
                }
            });
        }

        return result;
    }

    // m��todo para verificar se algum dos relacionamentos recebidos ���
    // generaliza������o
    private boolean searchForGeneralizations(Class cls) { // ok
        LOGGER.info("searchForGeneralizations()");
        Collection<Relationship> Relationships = cls.getRelationships();
        for (Relationship relationship : Relationships) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getChild().equals(cls) || generalization.getParent().equals(cls))
                    return true;
            }
        }
        return false;
    }

    private void removeComponentRelationships(Package comp, Architecture architecture) {
        if (comp.isTotalyFreezed()) return;
        LOGGER.info("removeComponentRelationships()");
        Relationship[] allInterElementRelationships = architecture.getRelationshipHolder().getAllRelationships()
                .toArray(new Relationship[0]);
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
        if (cls.isTotalyFreezed()) return;
        LOGGER.info("removeClassRelationships()");
        List<Relationship> relationshipsCls = new ArrayList<Relationship>(cls.getRelationships());
        if (!relationshipsCls.isEmpty()) {
            Iterator<Relationship> iteratorRelationships = relationshipsCls.iterator();
            while (iteratorRelationships.hasNext()) {
                Relationship relationship = iteratorRelationships.next();

                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient().equals(cls) || dependency.getSupplier().equals(cls))
                        architecture.removeRelationship(dependency);
                }

                if (relationship instanceof AssociationRelationship) {
                    AssociationRelationship association = (AssociationRelationship) relationship;
                    for (AssociationEnd associationEnd : association.getParticipants()) {
                        if (associationEnd.getCLSClass().equals(cls)) {
                            architecture.removeRelationship(association);
                            break;
                        }
                    }
                }

                if (relationship instanceof GeneralizationRelationship) {
                    GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                    if ((generalization.getChild().equals(cls)) || (generalization.getParent().equals(cls))) {
                        architecture.removeRelationship(generalization);

                    }
                }

            }
        }
    }

    public void evaluateConstraints(Solution solution) throws JMException {
        LOGGER.info("evaluateConstraints()");
        List<Package> allComponents = new ArrayList<Package>(((Architecture) solution.getDecisionVariables()[0]).getAllPackages());
        for (Package comp : allComponents) {
            List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());
            if (!(allClasses.isEmpty())) {
                Iterator<Class> iteratorClasses = allClasses.iterator();

                while (iteratorClasses.hasNext()) {
                    Class cls = iteratorClasses.next();
                    if ((cls.getAllAttributes().isEmpty()) && (cls.getAllMethods().isEmpty())
                            && (cls.getImplementedInterfaces().isEmpty()) && !(searchForGeneralizations(cls))
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


    public static Logger getLOGGER() {
        return LOGGER;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static int getContComp_() {
        return contComp_;
    }

    public static void setContComp_(int contComp_) {
        OPLA.contComp_ = contComp_;
    }

    public static int getContInt_() {
        return contInt_;
    }

    public static void setContInt_(int contInt_) {
        OPLA.contInt_ = contInt_;
    }

    public static int getContClass_() {
        return contClass_;
    }

    public static void setContClass_(int contClass_) {
        OPLA.contClass_ = contClass_;
    }

    public static int getContDiscardedSolutions_() {
        return contDiscardedSolutions_;
    }

    public static void setContDiscardedSolutions_(int contDiscardedSolutions_) {
        OPLA.contDiscardedSolutions_ = contDiscardedSolutions_;
    }

    public Architecture getArchitecture_() {
        return architecture_;
    }

    public void setArchitecture_(Architecture architecture_) {
        this.architecture_ = architecture_;
    }

    public List<String> getSelectedMetrics() {
        return selectedMetrics;
    }

    public void setSelectedMetrics(List<String> selectedMetrics) {
        this.selectedMetrics = selectedMetrics;
    }

    public ExperimentCommomConfigs getConfigs() {
        return configs;
    }

    public void setConfigs(ExperimentCommomConfigs configs) {
        this.configs = configs;
    }
}

