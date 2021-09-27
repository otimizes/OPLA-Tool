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
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverUtils;
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
        List<Fitness> fitness = new ArrayList<>();
        for (String selectedMetric : selectedMetrics) {
            ObjectiveFunctions metric = ObjectiveFunctions.valueOf(selectedMetric);
            fitness.add(new Fitness(metric.evaluate((Architecture) solution.getDecisionVariables()[0])));
        }
        for (int i = 0; i < fitness.size(); i++) {
            solution.setObjective(i, fitness.get(i).getValue());
        }
    }

    public void evaluateLinkOverload(Solution solution) {
        List<Fitness> fitness = new ArrayList<>();
        for (String selectedMetric : selectedMetrics) {
            ObjectiveFunctions metric = ObjectiveFunctions.valueOf(selectedMetric);
            fitness.add(new Fitness(metric.evaluate((Architecture) solution.getDecisionVariables()[0])));
        }
        for (int i = 0; i < fitness.size(); i++) {
            double weight = fitness.get(i).getValue()*100;
            solution.setObjective(i, fitness.get(i).getValue() + (weight * ((Architecture)solution.getDecisionVariables()[0]).getExceedLink()));
        }
    }

    public SolutionSet removeDominated(SolutionSet result) {
        List<Solution> collect = result.getSolutionSet().stream().filter(r -> (r.getEvaluation() >= 5)).collect(Collectors.toList());
        boolean domineering, dominated;
        double firstValue;
        double secondValue;

        for (int i = 0; i < (result.size() - 1); i++) {
            for (int j = (i + 1); j < result.size(); j++) {
                domineering = true;
                dominated = true;

                for (int k = 0; k < result.get(i).numberOfObjectives(); k++) {
                    firstValue = result.get(i).getObjective(k);
                    secondValue = result.get(j).getObjective(k);

                    if (firstValue > secondValue || !domineering) {
                        domineering = false;
                    } else if (firstValue <= secondValue) {
                        domineering = true;
                    }

                    if (secondValue > firstValue || !dominated) {
                        dominated = false;
                    } else if (secondValue < firstValue) {
                        dominated = true;
                    }
                }
                if (domineering) {
                    result.remove(j);
                    j = j - 1;
                } else if (dominated) {
                    result.remove(i);
                    j = i;
                }
            }
        }

        result.setCapacity(result.getCapacity() + collect.size());
        collect.forEach(c -> {
            if (!result.getSolutionSet().contains(c)) {
                result.add(c);
            }
        });
        return result;
    }

    public SolutionSet removeRepeated(SolutionSet result) {
        List<Solution> collect = result.getSolutionSet().stream().filter(r -> (r.getEvaluation() >= 5)).collect(Collectors.toList());
        String solucao;

        for (int i = 0; i < result.size() - 1; i++) {
            solucao = result.get(i).getDecisionVariables()[0].toString();
            for (int j = i + 1; j < result.size(); j++) {
                if (solucao.equals(result.get(j).getDecisionVariables()[0].toString())) {
                    result.remove(j);
                    this.configs.getLogger().putLog("Repeated removed");
                }
            }
        }

        result.setCapacity(result.getCapacity() + collect.size());
        collect.forEach(c -> {
            if (!result.getSolutionSet().contains(c)) {
                result.add(c);
            }
        });

        return result;
    }

    private boolean searchForGeneralizations(Class aClass) {
        return CrossoverUtils.searchForGeneralizations(aClass);
    }

    private void removeComponentRelationships(Package aPackage, Architecture architecture) {
        if (aPackage.isTotalyFreezed()) return;
        Relationship[] allInterElementRelationships = architecture.getRelationshipHolder().getAllRelationships()
                .toArray(new Relationship[0]);
        for (Relationship relationship : allInterElementRelationships) {
            if (relationship instanceof AbstractionRelationship) {
                AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
                if (abstraction.getClient().equals(aPackage)) {
                    architecture.removeRelationship(relationship);
                }
            }
            if (relationship instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) relationship;
                if (dependency.getClient().equals(aPackage)) {
                    architecture.removeRelationship(relationship);
                }
            }
        }
    }

    private void removeClassRelationships(Class aClass, Architecture architecture) {
        if (aClass.isTotalyFreezed()) return;
        List<Relationship> relationshipsCls = new ArrayList<>(aClass.getRelationships());
        if (!relationshipsCls.isEmpty()) {
            for (Relationship relationship : relationshipsCls) {
                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient().equals(aClass) || dependency.getSupplier().equals(aClass))
                        architecture.removeRelationship(dependency);
                }
                if (relationship instanceof AssociationRelationship) {
                    AssociationRelationship association = (AssociationRelationship) relationship;
                    for (AssociationEnd associationEnd : association.getParticipants()) {
                        if (associationEnd.getCLSClass().equals(aClass)) {
                            architecture.removeRelationship(association);
                            break;
                        }
                    }
                }
                if (relationship instanceof GeneralizationRelationship) {
                    GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                    if ((generalization.getChild().equals(aClass)) || (generalization.getParent().equals(aClass))) {
                        architecture.removeRelationship(generalization);
                    }
                }
            }
        }
    }

    public void evaluateConstraints(Solution solution) throws JMException {
        List<Package> allComponents = new ArrayList<>(((Architecture) solution.getDecisionVariables()[0]).getAllPackages());
        for (Package comp : allComponents) {
            List<Class> allClasses = new ArrayList<>(comp.getAllClasses());
            if (!(allClasses.isEmpty())) {
                for (Class cls : allClasses) {
                    if ((cls.getAllAttributes().isEmpty()) && (cls.getAllMethods().isEmpty())
                            && (cls.getImplementedInterfaces().isEmpty()) && !(searchForGeneralizations(cls))
                            && (cls.getVariantType() == null)) {
                        comp.removeClass(cls);
                        this.removeClassRelationships(cls, (Architecture) solution.getDecisionVariables()[0]);
                    }
                }
            }
            List<Interface> allItfsComp = new ArrayList<>(comp.getImplementedInterfaces());
            if (!(allItfsComp.isEmpty())) {
                Iterator<Interface> iteratorInterfaces = allItfsComp.iterator();
                setLastInterfaces(solution, comp, iteratorInterfaces);
            }
            allItfsComp.clear();

            Iterator<Interface> iteratorInterfaces = ((Architecture) solution.getDecisionVariables()[0]).getAllInterfaces().iterator();
            setLastInterfaces(solution, comp, iteratorInterfaces);

            if (comp.getAllClasses().isEmpty() && comp.getImplementedInterfaces().isEmpty() && comp.getAllInterfaces().isEmpty()) {
                this.removeComponentRelationships(comp, (Architecture) solution.getDecisionVariables()[0]);
                ((Architecture) solution.getDecisionVariables()[0]).removePackage(comp);
            }
        }
        allComponents.clear();
    }

    private void setLastInterfaces(Solution solution, Package aPackage, Iterator<Interface> iteratorInterfaces) {
        while (iteratorInterfaces.hasNext()) {
            Interface itf = iteratorInterfaces.next();
            boolean ultimaInterface = aPackage.getImplementedInterfaces().size() == 1;
            if (itf.getMethods().isEmpty() && !ultimaInterface) {
                ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
            }
            if (itf.getMethods().isEmpty() && ultimaInterface && aPackage.getAllClasses().size() < 1) {
                ((Architecture) solution.getDecisionVariables()[0]).removeInterface(itf);
            }
        }
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

