package br.otimizes.oplatool.core.jmetal4.problems;

import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.encodings.solutionType.ArchitectureSolutionType;
import br.otimizes.oplatool.architecture.builders.ArchitectureBuilderPapyrus;
import br.otimizes.oplatool.architecture.builders.ArchitectureBuilderSMarty;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.Fitness;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctions;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class OPLA extends Problem {

    private static final Logger LOGGER = Logger.getLogger(OPLA.class);
    private static final long serialVersionUID = 884633138619836573L;

    public static int countPackage = 0;
    public static int countInterface = 0;
    public static int countClass = 0;
    public static int contDiscardedSolutions_ = 0;

    public Architecture architecture_;
    private List<String> selectedMetrics;
    private ExperimentCommonConfigs configs;

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

        if (xmiFilePath.contains(".smty")) {
            architecture_ = new ArchitectureBuilderSMarty().create(xmiFilePath);
        } else {
            architecture_ = new ArchitectureBuilderPapyrus().create(xmiFilePath);
        }
    }


    public OPLA(String xmiFilePath, ExperimentCommonConfigs oplaConfig) throws Exception {
        this.configs = oplaConfig;
        numberOfVariables_ = 1;
        numberOfObjectives_ = oplaConfig.getOplaConfigs().getNumberOfObjectives();
        numberOfConstraints_ = 0;
        problemName_ = "OPLA";
        solutionType_ = new ArchitectureSolutionType(this);
        variableType_ = new java.lang.Class[numberOfVariables_];
        length_ = new int[numberOfVariables_];
        variableType_[0] = java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE);
        architecture_ = oplaConfig.getArchitectureBuilder().getBuilder().create(xmiFilePath);
        selectedMetrics = oplaConfig.getOplaConfigs().getSelectedObjectiveFunctions();
    }

    @Override
    public void evaluate(Solution solution) {
        List<Fitness> fitnesses = new ArrayList<>();

        for (String selectedMetric : selectedMetrics) {
            ObjectiveFunctions metric = ObjectiveFunctions.valueOf(selectedMetric);
            fitnesses.add(new Fitness(metric.evaluate((Architecture) solution.getDecisionVariables()[0])));
        }
        for (int i = 0; i < fitnesses.size(); i++) {
            solution.setObjective(i, fitnesses.get(i).getValue());
        }
    }

    public void evaluateLinkOverload(Solution solution) {
        List<Fitness> fitnesses = new ArrayList<>();

        for (String selectedMetric : selectedMetrics) {
            ObjectiveFunctions metric = ObjectiveFunctions.valueOf(selectedMetric);
            fitnesses.add(new Fitness(metric.evaluate((Architecture) solution.getDecisionVariables()[0])));
        }


        for (int i = 0; i < fitnesses.size(); i++) {
            double peso = fitnesses.get(i).getValue()*100;
            solution.setObjective(i, fitnesses.get(i).getValue() + (peso * ((Architecture)solution.getDecisionVariables()[0]).getExceedLink()));
        }
    }

    public SolutionSet removeDominadas(SolutionSet result) {
        List<Solution> collect = result.getSolutionSet().stream().filter(r -> (r.getEvaluation() >= 5)).collect(Collectors.toList());
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
                    result.remove(j);
                    j = j - 1;
                } else if (dominado) {
                    result.remove(i);
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

    public SolutionSet removeRepetidas(SolutionSet result) {
        List<Solution> collect = result.getSolutionSet().stream().filter(r -> (r.getEvaluation() >= 5)).collect(Collectors.toList());
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

    private boolean searchForGeneralizations(Class cls) { // ok
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
                    if (itf.getMethods().isEmpty() && !ultimaInterface) {
                        ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
                    }
                    if (itf.getMethods().isEmpty() && ultimaInterface && comp.getAllClasses().size() < 1) {
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
                if (itf.getMethods().isEmpty() && !ultimaInterface) {
                    ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
                }
                if (itf.getMethods().isEmpty() && ultimaInterface && comp.getAllClasses().size() < 1) {
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

    public boolean isPatterns(Solution solution) {
        Architecture a = (Architecture) solution.getDecisionVariables()[0];
        return a.isAppliedPatterns();
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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
}

