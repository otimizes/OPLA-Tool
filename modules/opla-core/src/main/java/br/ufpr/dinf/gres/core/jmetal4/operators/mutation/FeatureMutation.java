package br.ufpr.dinf.gres.core.jmetal4.operators.mutation;

import br.ufpr.dinf.gres.architecture.exceptions.ConcernNotFoundException;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.relationship.RealizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.common.Configuration;
import br.ufpr.dinf.gres.common.exceptions.JMException;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.operators.IOperator;
import br.ufpr.dinf.gres.core.jmetal4.problems.OPLA;
import br.ufpr.dinf.gres.core.jmetal4.util.PseudoRandom;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Feature mutation operator
 */
public class FeatureMutation implements IOperator<Solution> {
    @Override
    public Solution execute(Map<String, Object> parameters, Solution solution, String scope) {
        try {
            if (PseudoRandom.randDouble() < ((Double) parameters.get("probability"))) {
                if (solution.getDecisionVariables()[0].getVariableType().toString()
                        .equals("class " + Architecture.ARCHITECTURE_TYPE)) {

                    final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);
                    final List<Package> allComponents = new ArrayList<Package>(arch.getAllPackages().stream().filter(pk -> !pk.isTotalyFreezed()).collect(Collectors.toList()));
                    if (!allComponents.isEmpty()) {
                        final Package selectedComp = MutationUtils.getRandomPackage(allComponents);
                        List<Concern> concernsSelectedComp = new ArrayList<Concern>(selectedComp.getAllConcerns());
                        if (concernsSelectedComp.size() > 1) {
                            final Concern selectedConcern = MutationUtils.randomObject(concernsSelectedComp);
                            List<Package> allComponentsAssignedOnlyToConcern = new ArrayList<Package>(
                                    searchComponentsAssignedToConcern(selectedConcern, allComponents));
                            if (allComponentsAssignedOnlyToConcern.isEmpty()) {
                                OPLA.countPackage++;
                                modularizeConcernInComponent(allComponents,
                                        arch.createPackage("Package" + OPLA.countPackage + MutationUtils.getSuffix(selectedComp)),
                                        selectedConcern, arch);
                            } else {
                                if (allComponentsAssignedOnlyToConcern.size() == 1) {
                                    modularizeConcernInComponent(allComponents,
                                            allComponentsAssignedOnlyToConcern.get(0), selectedConcern, arch);
                                } else {
                                    modularizeConcernInComponent(allComponents,
                                            MutationUtils.getRandomPackage(allComponentsAssignedOnlyToConcern), selectedConcern, arch);
                                }
                            }
                            allComponentsAssignedOnlyToConcern.clear();
                        }
                        concernsSelectedComp.clear();
                        allComponents.clear();
                    }

                } else {
                    Configuration.logger_.log(Level.SEVERE, "FeatureMutation.doMutation: invalid type. " + "{0}",
                            solution.getDecisionVariables()[0].getVariableType());
                    java.lang.Class<String> cls = java.lang.String.class;
                    String name = cls.getName();
                    throw new JMException("Exception in " + name + ".doMutation()");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solution;
    }

    private List<Package> searchComponentsAssignedToConcern(Concern concern, List<Package> allComponents) {
        final List<Package> allComponentsAssignedToConcern = new ArrayList<Package>();
        for (Package component : allComponents) {
            final Set<Concern> numberOfConcernsForPackage = getNumberOfConcernsFor(component);
            if (numberOfConcernsForPackage.size() == 1 && (numberOfConcernsForPackage.contains(concern))) {
                allComponentsAssignedToConcern.add(component);
            }
        }
        return allComponentsAssignedToConcern.stream().filter(pk -> !pk.isTotalyFreezed()).collect(Collectors.toList());
    }

    private Set<Concern> getNumberOfConcernsFor(Package pkg) {
        final Set<Concern> listOfOwnedConcern = new HashSet<Concern>();

        for (Class klass : pkg.getAllClasses()) {
            listOfOwnedConcern.addAll(klass.getOwnConcerns());
        }
        for (Interface inte : pkg.getAllInterfaces()) {
            listOfOwnedConcern.addAll(inte.getOwnConcerns());
        }

        return listOfOwnedConcern;
    }

    private void modularizeConcernInComponent(List<Package> allComponents, Package targetComponent, Concern concern,
                                              Architecture arch) {
        try {
            Iterator<Package> itrComp = allComponents.iterator();
            while (itrComp.hasNext()) {
                Package comp = itrComp.next();
                if (!comp.equals(targetComponent) && MutationUtils.checkSameLayer(comp, targetComponent)) {
                    final Set<Interface> allInterfaces = new HashSet<Interface>(comp.getAllInterfaces().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList()));
                    allInterfaces.addAll(comp.getImplementedInterfaces().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList()));

                    Iterator<Interface> itrInterface = allInterfaces.iterator();
                    while (itrInterface.hasNext()) {
                        Interface interfaceComp = itrInterface.next();
                        if (MutationUtils.searchPatternsInterface(interfaceComp)) {
                            if (interfaceComp.getOwnConcerns().size() == 1 && interfaceComp.containsConcern(concern)) {
                                moveInterfaceToComponent(interfaceComp, targetComponent, comp, arch, concern); // EDIPO
                            } else if (!interfaceComp.getPatternsOperations().hasPatternApplied()) {
                                List<Method> operationsInterfaceComp = new ArrayList<Method>(
                                        interfaceComp.getMethods().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                Iterator<Method> itrOperation = operationsInterfaceComp.iterator();
                                while (itrOperation.hasNext()) {
                                    Method operation = itrOperation.next();
                                    if (operation.getOwnConcerns().size() == 1 && operation.containsConcern(concern)) {
                                        moveOperationToComponent(operation, interfaceComp, targetComponent, comp, arch,
                                                concern);
                                    }
                                }
                            }
                        }
                    }

                    allInterfaces.clear();
                    final List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                    Iterator<Class> ItrClass = allClasses.iterator();
                    while (ItrClass.hasNext()) {
                        Class classComp = ItrClass.next();
                        if (MutationUtils.searchPatternsClass(classComp)) {
                            if (comp.getAllClasses().contains(classComp)) {
                                if ((classComp.getOwnConcerns().size() == 1) && (classComp.containsConcern(concern))) {
                                    if (!MutationUtils.searchForGeneralizations(classComp)) {
                                        moveClassToComponent(classComp, targetComponent, comp, arch, concern);
                                    } else {
                                        MutationUtils.moveHierarchyToComponent(classComp, targetComponent, arch);
                                    }
                                } else {
                                    if (!MutationUtils.searchForGeneralizations(classComp)) {
                                        if (!MutationUtils.isVarPointOfConcern(arch, classComp, concern)
                                                && !MutationUtils.isVariantOfConcern(arch, classComp, concern)) {
                                            final List<Attribute> attributesClassComp = new ArrayList<Attribute>(
                                                    classComp.getAllAttributes().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                            Iterator<Attribute> irtAttribute = attributesClassComp.iterator();
                                            while (irtAttribute.hasNext()) {
                                                Attribute attribute = irtAttribute.next();
                                                if (attribute.getOwnConcerns().size() == 1
                                                        && attribute.containsConcern(concern)) {
                                                    moveAttributeToComponent(attribute, classComp, targetComponent,
                                                            comp, arch, concern);
                                                }
                                            }
                                            attributesClassComp.clear();
                                            if (!classComp.getPatternsOperations().hasPatternApplied()) {
                                                final List<Method> methodsClassComp = new ArrayList<Method>(
                                                        classComp.getAllMethods().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()));
                                                Iterator<Method> irtMethod = methodsClassComp.iterator();
                                                while (irtMethod.hasNext()) {
                                                    Method method = irtMethod.next();
                                                    if (method.getOwnConcerns().size() == 1
                                                            && method.containsConcern(concern)) {
                                                        moveMethodToComponent(method, classComp, targetComponent, comp,
                                                                arch, concern);
                                                    }
                                                }
                                                methodsClassComp.clear();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    allClasses.clear();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void moveClassToComponent(Class classComp, Package targetComp, Package sourceComp,
                                      Architecture architecture, Concern concern) {
        if (MutationUtils.searchPatternsClass(classComp)) {
            sourceComp.moveClassToPackage(classComp, targetComp);
        }
    }

    private void moveAttributeToComponent(Attribute attribute, Class classComp, Package targetComp, Package sourceComp,
                                          Architecture architecture, Concern concern) throws ConcernNotFoundException {
        if (MutationUtils.searchPatternsClass(classComp)) {
            final Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
            classComp.moveAttributeToClass(attribute, targetClass);
            MutationUtils.createAssociation(architecture, targetClass, classComp);
        }
    }

    private void moveMethodToComponent(Method method, Class classComp, Package targetComp, Package sourceComp,
                                       Architecture architecture, Concern concern) throws ConcernNotFoundException {
        if (MutationUtils.searchPatternsClass(classComp)) {
            final Class targetClass = findOrCreateClassWithConcern(targetComp, concern);
            classComp.moveMethodToClass(method, targetClass);
            MutationUtils.createAssociation(architecture, targetClass, classComp);
        }
    }

    private Class findOrCreateClassWithConcern(Package targetComp, Concern concern) throws ConcernNotFoundException {
        Class targetClass = null;
        for (Class cls : targetComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList())) {
            if (cls.containsConcern(concern)) {
                targetClass = cls;
            }
        }

        if (targetClass == null) {
            targetClass = targetComp.createClass("Class" + OPLA.countClass++, false);
            targetClass.addConcern(concern.getName());
        }
        return targetClass;
    }

    private void moveInterfaceToComponent(Interface interfaceComp, Package targetComp, Package sourceComp,
                                          Architecture architecture, Concern concernSelected) {
        if (MutationUtils.searchPatternsInterface(interfaceComp)) {
            if (!sourceComp.moveInterfaceToPackage(interfaceComp, targetComp)) {
                architecture.moveElementToPackage(interfaceComp, targetComp);
            }

            for (Element implementor : interfaceComp.getImplementors()) {
                if (implementor instanceof Package) {
                    if (targetComp.getAllClasses().size() == 1) {
                        final Class klass = targetComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()).iterator().next();
                        for (Concern concern : klass.getOwnConcerns()) {
                            if (interfaceComp.containsConcern(concern)) {
                                architecture.removeImplementedInterface(interfaceComp, sourceComp);
                                addExternalInterface(targetComp, architecture, interfaceComp);
                                addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                            }
                        }
                        return;
                    } else if (targetComp.getAllClasses().size() > 1) {
                        final List<Class> targetClasses = allClassesWithConcerns(concernSelected,
                                targetComp.getAllClasses()).stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList());
                        final Class klass = randomClass(targetClasses);
                        architecture.removeImplementedInterface(interfaceComp, sourceComp);
                        addExternalInterface(targetComp, architecture, interfaceComp);
                        addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                        return;
                    } else {
                        final List<Class> targetClasses = allClassesWithConcerns(concernSelected,
                                architecture.getAllClasses()).stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList());
                        final Class klass = randomClass(targetClasses);
                        architecture.removeImplementedInterface(interfaceComp, sourceComp);
                        addExternalInterface(targetComp, architecture, interfaceComp);
                        addImplementedInterface(targetComp, architecture, interfaceComp, klass);
                    }
                }
            }
        }
    }

    private Class randomClass(List<Class> targetClasses) {
        Collections.shuffle(targetClasses);
        return targetClasses.get(0);
    }

    private List<Class> allClassesWithConcerns(Concern c, Set<Class> allClasses) {
        List<Class> klasses = new ArrayList<Class>();
        for (Class klass : allClasses) {
            for (Concern concernKlass : klass.getOwnConcerns()) {
                if (concernKlass.getName().equalsIgnoreCase(c.getName())) {
                    klasses.add(klass);
                }
            }
        }
        return klasses;
    }

    private void moveOperationToComponent(Method operation, Interface sourceInterface, Package targetComp,
                                          Package sourceComp, Architecture architecture, Concern concern) throws ConcernNotFoundException {
        Interface targetInterface = null;
        if (MutationUtils.searchPatternsInterface(sourceInterface)) {
            targetInterface = searchForInterfaceWithConcern(concern, targetComp);

            if (targetInterface == null) {
                targetInterface = targetComp.createInterface("Interface" + OPLA.countInterface++);
                sourceInterface.moveOperationToInterface(operation, targetInterface);
                targetInterface.addConcern(concern.getName());
            } else {
                sourceInterface.moveOperationToInterface(operation, targetInterface);
            }

            addRelationship(sourceInterface, targetComp, sourceComp, architecture, concern, targetInterface);
        }
    }

    private void addRelationship(Interface sourceInterface, Package targetComp, Package sourceComp,
                                 Architecture architecture, Concern concern, Interface targetInterface) {
        for (Element implementor : sourceInterface.getImplementors()) {
            if (implementor instanceof Package) {
                if (targetComp.getAllClasses().size() == 1) {
                    final Class klass = targetComp.getAllClasses().stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList()).iterator().next();
                    if (MutationUtils.searchPatternsClass(klass)) {
                        for (Concern c : klass.getOwnConcerns()) {
                            if (MutationUtils.searchPatternsInterface(targetInterface)) {
                                if (targetInterface.containsConcern(c)) {
                                    architecture.removeImplementedInterface(sourceInterface, sourceComp);
                                    addExternalInterface(targetComp, architecture, targetInterface);
                                    addImplementedInterface(targetComp, architecture, targetInterface, klass);
                                    return;
                                }
                            }
                        }
                    }
                } else if (targetComp.getAllClasses().size() > 1) {
                    final List<Class> targetClasses = allClassesWithConcerns(concern, targetComp.getAllClasses()).stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList());
                    final Class klass = randomClass(targetClasses);
                    if (MutationUtils.searchPatternsClass(klass)) {
                        architecture.removeImplementedInterface(sourceInterface, sourceComp);
                        addExternalInterface(targetComp, architecture, targetInterface);
                        addImplementedInterface(targetComp, architecture, targetInterface, klass);
                        return;
                    }
                } else {
                    final List<Class> targetClasses = allClassesWithConcerns(concern, architecture.getAllClasses()).stream().filter(c -> !c.isTotalyFreezed()).collect(Collectors.toList());
                    final Class klass = randomClass(targetClasses);
                    if (MutationUtils.searchPatternsClass(klass)) {
                        if (klass != null) {
                            architecture.removeImplementedInterface(sourceInterface, sourceComp);
                            addExternalInterface(targetComp, architecture, targetInterface);
                            addImplementedInterface(targetComp, architecture, targetInterface, klass);
                        }
                    }
                }
            }
            if (implementor instanceof Class) {
                if (MutationUtils.searchPatternsInterface(sourceInterface) && MutationUtils.searchPatternsInterface(targetInterface)) {
                    architecture.removeImplementedInterface(sourceInterface, sourceComp);
                    addExternalInterface(targetComp, architecture, targetInterface);
                    addImplementedInterface(targetComp, architecture, targetInterface, (Class) implementor);
                }
            }
        }
    }

    private void addImplementedInterface(Package targetComp, Architecture architecture, Interface targetInterface,
                                         Class klass) {
        if (!packageTargetHasRealization(targetComp, targetInterface)) {
            architecture.addImplementedInterface(targetInterface, klass);
        }
    }

    private void addExternalInterface(Package targetComp, Architecture architecture, Interface targetInterface) {
        final String packageNameInterface = UtilResources.extractPackageName(targetInterface.getNamespace().trim());
        if (packageNameInterface.equalsIgnoreCase("model")) {
            architecture.addExternalInterface(targetInterface);
        } else {
            targetComp.addExternalInterface(targetInterface);
        }
    }

    private boolean packageTargetHasRealization(Package targetComp, Interface targetInterface) {
        for (Relationship r : targetComp.getRelationships()) {
            if (r instanceof RealizationRelationship) {
                final RealizationRelationship realization = (RealizationRelationship) r;
                if (realization.getSupplier().equals(targetInterface)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Interface searchForInterfaceWithConcern(Concern concern, Package targetComp) {
        for (Interface itf : targetComp.getImplementedInterfaces().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList())) {
            if (itf.containsConcern(concern)) {
                return itf;
            }
        }
        for (Interface itf : targetComp.getAllInterfaces().stream().filter(i -> !i.isTotalyFreezed()).collect(Collectors.toList())) {
            if (itf.containsConcern(concern)) {
                return itf;
            }
        }
        return null;
    }
}
