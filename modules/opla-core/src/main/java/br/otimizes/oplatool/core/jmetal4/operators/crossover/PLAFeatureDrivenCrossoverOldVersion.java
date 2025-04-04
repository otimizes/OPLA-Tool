package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.architecture.exceptions.ClassNotFound;
import br.otimizes.oplatool.architecture.exceptions.ConcernNotFoundException;
import br.otimizes.oplatool.architecture.exceptions.NotFoundException;
import br.otimizes.oplatool.architecture.exceptions.PackageNotFound;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.common.Configuration;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.operators.IOperator;
import br.otimizes.oplatool.core.jmetal4.util.PseudoRandom;

import java.util.*;
import java.util.logging.Level;

/**
 * PLA Crossover old version
 */
public class PLAFeatureDrivenCrossoverOldVersion implements IOperator<Solution[]> {

    private CrossoverUtils crossoverutils;
    private boolean variabilitiesOk = true;
    private String SCOPE_LEVEL;

    public PLAFeatureDrivenCrossoverOldVersion() {
        crossoverutils = new CrossoverUtils();
    }

    @Override
    public Solution[] execute(Map<String, Object> parameters, Solution[] solution, String scope) {
        SCOPE_LEVEL = scope;
        Solution[] offspring = new Solution[2];

        Solution[] crossFeature = new Solution[0];
        try {
            crossFeature = this.crossoverFeatures(((Double) parameters.get("probability")), solution[0], solution[1], scope);
        } catch (JMException | CloneNotSupportedException | ClassNotFound | PackageNotFound | NotFoundException | ConcernNotFoundException e) {
            e.printStackTrace();
        }
        offspring[0] = crossFeature[0];
        offspring[1] = crossFeature[1];

        return offspring;
    }


    public static boolean isChild(Class cls) {
        boolean child = false;

        for (Relationship relationship : cls.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getChild().equals(cls)) {
                    child = true;
                    return child;
                }
            }
        }
        return child;
    }

    public static Class getParent(Class cls) {
        for (Relationship relationship : cls.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship)
                if (((GeneralizationRelationship) relationship).getChild().equals(cls))
                    return (Class) ((GeneralizationRelationship) relationship).getParent();
        }
        return null;
    }

    private static GeneralizationRelationship getGeneralizationForClass(Element cls) {
        for (Relationship relationship : ((Class) cls).getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) relationship).getParent().equals(cls))
                    return (GeneralizationRelationship) relationship;
            }
        }

        return null;
    }

    public Solution[] crossoverFeatures(double probability, Solution parent1, Solution parent2, String scope) throws JMException, CloneNotSupportedException, ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
        Solution[] offspring = new Solution[2];
        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);

        try {
            if (parent1.getDecisionVariables()[0].getVariableType() == java.lang.Class.forName(Architecture.ARCHITECTURE_TYPE)) {
                if (PseudoRandom.randDouble() < probability) {
                    List<Concern> concernsArchitecture = new ArrayList<Concern>(((Architecture) offspring[0].getDecisionVariables()[0]).getAllConcerns());
                    Concern feature = randomObject(concernsArchitecture);

                    obtainChild(feature, (Architecture) parent2.getDecisionVariables()[0], (Architecture) offspring[0].getDecisionVariables()[0], scope);
                    if (!(isValidSolution((Architecture) offspring[0].getDecisionVariables()[0]))) {
                        offspring[0] = parent1;
                    }
                    this.variabilitiesOk = true;
                    obtainChild(feature, (Architecture) parent1.getDecisionVariables()[0], (Architecture) offspring[1].getDecisionVariables()[0], scope);
                    if (!(isValidSolution((Architecture) offspring[1].getDecisionVariables()[0]))) {
                        offspring[0] = parent1;
                    }
                    concernsArchitecture = null;
                }
            } else {
                Configuration.logger_.log(Level.SEVERE, "PLACrossover.doCrossover: " + "invalid type{0}", parent1.getDecisionVariables()[0].getVariableType());
                java.lang.Class<String> cls = java.lang.String.class;
                String name = cls.getName();
                throw new JMException("Exception in " + name + ".doCrossover()");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return offspring;
    }

    public void obtainChild(Concern feature, Architecture parent, Architecture offspring, String scope) throws ClassNotFound, PackageNotFound, NotFoundException, ConcernNotFoundException {
        crossoverutils.removeArchitecturalElementsRealizingFeature(feature, offspring, scope);
        addElementsToOffspring(feature, offspring, parent, scope);
        this.variabilitiesOk = updateVariabilitiesOffspring(offspring);
    }

    public <T> T randomObject(List<T> allObjects) throws JMException {
        int numObjects = allObjects.size();
        int key;
        T object;
        if (numObjects == 0) {
            object = null;
        } else {
            key = PseudoRandom.randInt(0, numObjects - 1);
            object = allObjects.get(key);
        }
        return object;
    }

    public void addElementsToOffspring(Concern feature, Architecture offspring, Architecture parent, String scope) {
        try {
            for (Package parentPackage : parent.getAllPackages()) {
                addOrCreatePackageIntoOffspring(feature, offspring, parent, parentPackage);
            }
            CrossoverRelationship.cleanRelationships();
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
        }

    }

    public void addOrCreatePackageIntoOffspring(Concern feature, Architecture offspring, Architecture parent, Package parentPackage) throws Exception {
        if ((parentPackage.getOwnConcerns().size() == 1) && parentPackage.containsConcern(feature)) {
            Package packageInOffspring = offspring.findPackageByName(parentPackage.getName());
            if (packageInOffspring == null)
                packageInOffspring = offspring.createPackage(parentPackage.getName());
            addImplementedInterfacesByPackageInOffspring(parentPackage, offspring, parent);
            addRequiredInterfacesByPackageInOffspring(parentPackage, offspring, parent);
            addInterfacesToPackageInOffSpring(parentPackage, packageInOffspring, offspring, parent);

            addClassesToOffspring(feature, parentPackage, packageInOffspring, offspring, parent);
        } else {
            addClassesRealizingFeatureToOffspring(feature, parentPackage, offspring, parent, SCOPE_LEVEL, parentPackage.getAllClasses());
            addInterfacesRealizingFeatureToOffspring(feature, parentPackage, offspring, parent);

        }

        saveAllRelationshiopForElement(parentPackage, parent, offspring);

    }

    private void addClassesRealizingFeatureToOffspring(Concern feature, Package parentPackage, Architecture offspring, Architecture parent, String SCOPE_LEVEL2, Set<Class> allClasses) throws Exception {
        Package newComp = offspring.findPackageByName(parentPackage.getName());
        for (Class classComp : allClasses) {
            if (classComp.getOwnConcerns().size() == 1 && classComp.containsConcern(feature)) {
                if (newComp == null)
                    newComp = offspring.createPackage(parentPackage.getName());
                if (!classComp.doesBelongToGeneralization()) {
                    //addClassToOffspring(classComp, newComp, offspring, parent);
                    newComp.addExternalClass(classComp);
                    saveAllRelationshiopForElement(classComp, parent, offspring);
                } else {
                    if (isHierarchyInASameComponent(classComp, parent)) {
                        moveHierarchyToSameComponent(classComp, newComp, parentPackage, offspring, parent, feature);
                        saveAllRelationshiopForElement(classComp, parent, offspring);
                    } else {
                        newComp.addExternalClass(classComp);
                        moveHierarchyToDifferentPackage(classComp, newComp, parentPackage, offspring, parent);
                        saveAllRelationshiopForElement(classComp, parent, offspring);
                    }
                }
            } else {
                if ((SCOPE_LEVEL.equals("allLevels")) && (!classComp.doesBelongToGeneralization())) {
                    addAttributesRealizingFeatureToOffspring(feature, classComp, parentPackage, offspring, parent);
                    addMethodsRealizingFeatureToOffspring(feature, classComp, parentPackage, offspring, parent);
                }
            }
            addInterfacesImplementedByClass(classComp, offspring, parent, newComp);
            addInterfacesRequiredByClass(classComp, offspring, parent, newComp);
        }

    }

    private void move(Concern feature, Package parentPackage, Architecture offspring, Architecture parent, Package newComp, Class classComp) {
        if (isHierarchyInASameComponent(classComp, parent)) {
            moveHierarchyToSameComponent(classComp, newComp, parentPackage, offspring, parent, feature);
            saveAllRelationshiopForElement(classComp, parent, offspring);
        } else {
            newComp.addExternalClass(classComp);
            moveHierarchyToDifferentPackage(classComp, newComp, parentPackage, offspring, parent);
            saveAllRelationshiopForElement(classComp, parent, offspring);
        }
    }

    public void addAttributesRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent) throws Exception {
        List<Class> klasses = offspring.findClassByName(classComp.getName());
        Class targetClass = null;
        if (klasses != null)
            targetClass = klasses.get(0);

        Iterator<Attribute> iteratorAttributes = classComp.getAllAttributes().iterator();
        while (iteratorAttributes.hasNext()) {
            Attribute attribute = iteratorAttributes.next();
            if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size() == 1) {
                if (targetClass == null) {
                    Package newComp = offspring.findPackageByName(comp.getName());
                    if (newComp == null)
                        newComp = offspring.createPackage(comp.getName());
                    targetClass = newComp.createClass(classComp.getName(), false);
                    targetClass.addConcern(feature.getName());
                }
                targetClass.addExternalAttribute(attribute);
            }
        }
    }

    private void addMethodsRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring, Architecture parent) throws Exception {
        List<Class> klasses = offspring.findClassByName(classComp.getName());
        Class targetClass = null;
        if (klasses != null)
            targetClass = klasses.get(0);

        Iterator<Method> iteratorMethods = classComp.getAllMethods().iterator();
        while (iteratorMethods.hasNext()) {
            Method method = iteratorMethods.next();
            if ((method.getOwnConcerns().size() == 1) && (method.containsConcern(feature))) {
                if (targetClass == null) {
                    Package newComp = offspring.findPackageByName(comp.getName());
                    if (newComp == null)
                        newComp = offspring.createPackage(comp.getName());
                    targetClass = newComp.createClass(classComp.getName(), false);
                    targetClass.addConcern(feature.getName());
                }
                saveAllRelationshiopForElement(classComp, parent, offspring);
                targetClass.addExternalMethod(method);

            }
        }
    }

    private void addInterfacesToPackageInOffSpring(Package parentPackage, Package packageInOffspring, Architecture offspring, Architecture parent) {
        for (Interface inter : parentPackage.getAllInterfaces()) {
            packageInOffspring.addExternalInterface(inter);
            saveAllRelationshiopForElement(inter, parent, offspring);
        }
    }

    private void addInterfacesRealizingFeatureToOffspring(Concern feature, Package comp, Architecture offspring, Architecture parent) throws Exception {

        Iterator<Interface> iteratorInterfaces = comp.getOnlyInterfacesImplementedByPackage().iterator();
        while (iteratorInterfaces.hasNext()) {
            Interface interfaceComp = iteratorInterfaces.next();
            if (interfaceComp.getOwnConcerns().size() == 1 && interfaceComp.containsConcern(feature)) {
                Package newComp = offspring.findPackageByName(comp.getName());
                if (newComp == null) {
                    newComp = offspring.createPackage(comp.getName());
                    saveAllRelationshiopForElement(newComp, parent, offspring);
                }
                if (interfaceComp.getNamespace().equalsIgnoreCase("model"))
                    offspring.addExternalInterface(interfaceComp);
                else {
                    interfaceComp = findOrCreatePakage(UtilResources.extractPackageName(interfaceComp.getNamespace()), offspring).createInterface(interfaceComp.getName());
                }
                saveAllRelationshiopForElement(interfaceComp, parent, offspring);
            } else {
                addOperationsRealizingFeatureToOffspring(feature, interfaceComp, comp, offspring, parent);
            }
        }
    }

    private void addOperationsRealizingFeatureToOffspring(Concern feature, Interface interfaceComp, Package comp, Architecture offspring, Architecture parent) throws Exception {
        Interface targetInterface = offspring.findInterfaceByName(interfaceComp.getName());

        Iterator<Method> iteratorOperations = interfaceComp.getMethods().iterator();
        while (iteratorOperations.hasNext()) {
            Method operation = iteratorOperations.next();
            if (operation.containsConcern(feature) && operation.getOwnConcerns().size() == 1) {
                if (targetInterface == null) {
                    Package newComp;
                    newComp = offspring.findPackageByName(comp.getName());

                    if (newComp == null) {
                        saveAllRelationshiopForElement(offspring.createPackage(comp.getName()), parent, offspring);
                    }
                    if (interfaceComp.getNamespace().equalsIgnoreCase("model"))
                        targetInterface = offspring.createInterface(interfaceComp.getName());
                    else {
                        targetInterface = findOrCreatePakage(UtilResources.extractPackageName(interfaceComp.getNamespace()), offspring).createInterface(interfaceComp.getName());
                    }
                    targetInterface.addConcern(feature.getName());
                    saveAllRelationshiopForElement(interfaceComp, parent, offspring);
                }
                //interfaceComp.moveOperationToInterface(operation, targetInterface);
                targetInterface.addExternalMethod(operation);
            }
        }
    }

    private Package findOrCreatePakage(String packageName, Architecture offspring) {
        Package pkg = null;
        pkg = offspring.findPackageByName(packageName);
        if (pkg == null)
            return offspring.createPackage(packageName);
        return pkg;
    }

    private void addClassesToOffspring(Concern feature, Package parentPackage, Package packageInOffspring, Architecture offspring, Architecture parent) {
        Iterator<Class> iteratorClasses = parentPackage.getAllClasses().iterator();
        while (iteratorClasses.hasNext()) {
            Class classComp = iteratorClasses.next();
            if (!classComp.doesBelongToGeneralization()) {
                addClassToOffspring(classComp, packageInOffspring, offspring, parent);
            } else {
                if (this.isHierarchyInASameComponent(classComp, parent)) {
                    moveHierarchyToSameComponent(classComp, packageInOffspring, parentPackage, offspring, parent, feature);
                } else {
                    packageInOffspring.addExternalClass(classComp);
                    moveHierarchyToDifferentPackage(classComp, packageInOffspring, parentPackage, offspring, parent);
                }
                saveAllRelationshiopForElement(classComp, parent, offspring);
            }
            addInterfacesImplementedByClass(classComp, offspring, parent, parentPackage);
            addInterfacesRequiredByClass(classComp, offspring, parent, parentPackage);
        }
    }

    private void addImplementedInterfacesByPackageInOffspring(Package parentPackage, Architecture offspring, Architecture parent) {
        final Iterator<Interface> iteratorInterfaces = parentPackage.getOnlyInterfacesImplementedByPackage().iterator();
        while (parentPackage.getOnlyInterfacesImplementedByPackage().iterator().hasNext()) {
            final Interface interfaceComp = iteratorInterfaces.next();
            if (interfaceComp.getNamespace().equalsIgnoreCase("model"))
                offspring.addExternalInterface(interfaceComp);
            else {
                findOrCreatePakage(UtilResources.extractPackageName(interfaceComp.getNamespace()), offspring).addExternalInterface(interfaceComp);
            }
            saveAllRelationshiopForElement(interfaceComp, parent, offspring);
        }
    }

    private void addRequiredInterfacesByPackageInOffspring(Package parentPackage, Architecture offspring, Architecture parent) {
        final Iterator<Interface> iteratorInterfaces = parentPackage.getOnlyInterfacesRequiredByPackage().iterator();
        while (iteratorInterfaces.hasNext()) {
            final Interface interfaceComp = iteratorInterfaces.next();
            if (interfaceComp.getNamespace().equalsIgnoreCase("model"))
                offspring.addExternalInterface(interfaceComp);
            else {
                findOrCreatePakage(UtilResources.extractPackageName(interfaceComp.getNamespace()), offspring).addExternalInterface(interfaceComp);
            }
            saveAllRelationshiopForElement(interfaceComp, parent, offspring);
        }
    }

    private boolean searchForGeneralizations(Class cls) {
        for (Relationship relationship : cls.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship)
                if (((GeneralizationRelationship) relationship).getChild().equals(cls) || ((GeneralizationRelationship) relationship).getParent().equals(cls))
                    return true;
        }
        return false;
    }

    private boolean isHierarchyInASameComponent(Class class_, Architecture architecture) {
        boolean sameComponent = true;
        Class parent = class_;
        Package componentOfClass = null;
        componentOfClass = architecture.findPackageOfClass(class_);
        Package componentOfParent = architecture.findPackageOfClass(class_);
        while (CrossoverOperations.isChild(parent)) {
            parent = CrossoverOperations.getParent(parent);
            componentOfParent = architecture.findPackageOfClass(parent);
            if (!(componentOfClass.equals(componentOfParent))) {
                sameComponent = false;
                return false;
            }
        }
        return sameComponent;
    }

    private void moveChildrenToSameComponent(Class parent, Package sourceComp, Package targetComp, Architecture offspring, Architecture parentArch) {

        final Collection<Element> children = getChildren(parent);
        for (Element child : children) {
            moveChildrenToSameComponent((Class) child, sourceComp, targetComp, offspring, parentArch);
        }
        if (sourceComp.getAllClasses().contains(parent)) {
            addClassToOffspring(parent, targetComp, offspring, parentArch);
        } else {
            try {
                for (Package auxComp : parentArch.getAllPackages()) {
                    if (auxComp.getAllClasses().contains(parent)) {
                        sourceComp = auxComp;
                        if (sourceComp.getName() != targetComp.getName()) {
                            targetComp = offspring.findPackageByName(sourceComp.getName());
                            if (targetComp == null) {
                                targetComp = offspring.createPackage(sourceComp.getName());
                                for (Concern feature : sourceComp.getOwnConcerns())
                                    targetComp.addConcern(feature.getName());
                            }
                        }
                    }
                    addClassToOffspring(parent, targetComp, offspring, parentArch);
                    break;
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    private void moveChildrenToDifferentComponent(Class root, Package newComp, Architecture offspring, Architecture parent) {
        final String rootPackageName = UtilResources.extractPackageName(root.getNamespace());
        Package rootTargetPackage = offspring.findPackageByName(rootPackageName);
        if (rootPackageName == null)
            rootTargetPackage = offspring.createPackage(rootPackageName);

        addClassToOffspring(root, rootTargetPackage, offspring, parent);

        saveAllRelationshiopForElement(parent.findPackageByName(rootPackageName), parent, offspring);
        for (Element child : getChildren(root)) {
            final String packageName = UtilResources.extractPackageName(child.getNamespace());
            Package targetPackage = parent.findPackageByName(packageName);
            if (targetPackage != null)
                moveChildrenToDifferentComponent((Class) child, targetPackage, offspring, parent);
        }
    }

    public void addClassToOffspring(Class klass, Package targetComp, Architecture offspring, Architecture parent) {
        targetComp.addExternalClass(klass);
        saveAllRelationshiopForElement(klass, parent, offspring);
    }

    private void addInterfacesImplementedByClass(Class klass, Architecture offspring, Architecture parent, Package targetComp) {

        for (Interface itf : klass.getImplementedInterfaces()) {
            if (itf.getNamespace().equalsIgnoreCase("model"))
                offspring.addExternalInterface(itf);
            else
                findOrCreatePakage(UtilResources.extractPackageName(itf.getNamespace()), offspring).addExternalInterface(itf);
            saveAllRelationshiopForElement(itf, parent, offspring);
        }
    }

    private void addInterfacesRequiredByClass(Class klass, Architecture offspring, Architecture parent, Package targetComp) {
        for (Interface itf : klass.getRequiredInterfaces()) {
            if (itf.getNamespace().equalsIgnoreCase("model"))
                offspring.addExternalInterface(itf);
            else
                findOrCreatePakage(UtilResources.extractPackageName(itf.getNamespace()), offspring).addExternalInterface(itf);

            saveAllRelationshiopForElement(itf, parent, offspring);
        }
    }

    private void moveHierarchyToSameComponent(Class classComp, Package targetComp, Package sourceComp, Architecture offspring, Architecture parent, Concern concern) {
        Class root = classComp;
        while (isChild(root)) {
            root = getParent(root);
        }
        if (sourceComp.getAllClasses().contains(root)) {
            moveChildrenToSameComponent(root, sourceComp, targetComp, offspring, parent);
        }
    }

    private void moveHierarchyToDifferentPackage(Class classComp, Package newComp, Package parentPackage, Architecture offspring, Architecture parent) {
        Class root = classComp;
        while (isChild(root)) {
            root = getParent(root);
        }
        moveChildrenToDifferentComponent(root, newComp, offspring, parent);
    }

    private boolean updateVariabilitiesOffspring(Architecture offspring) {
        boolean variabilitiesOk = true;
        for (Variability variability : offspring.getAllVariabilities()) {
            final VariationPoint variationPoint = variability.getVariationPoint();
            if (variationPoint != null) {
                Element elementVP = variationPoint.getVariationPointElement();
                Element VP = offspring.findElementByName(elementVP.getName());
                if (VP != null) {
                    if (!(VP.equals(elementVP)))
                        variationPoint.replaceVariationPointElement(offspring.findElementByName(elementVP.getName(), "class"));
                }
            }
        }
        return variabilitiesOk;
    }

    private boolean isValidSolution(Architecture solution) {
        boolean isValid = true;

        final List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getMethods().isEmpty())) {
                    return false;
                }
            }
        }
        if (!(this.variabilitiesOk))
            return false;
        return isValid;
    }

    private void saveAllRelationshiopForElement(Element element, Architecture parent, Architecture offspring) {

        if (element instanceof Class) {
            for (Relationship r : ((Class) element).getRelationships())
                offspring.getRelationshipHolder().addRelationship(r);
            return;
        }
        if (element instanceof Interface) {
            for (Relationship r : ((Interface) element).getRelationships())
                offspring.getRelationshipHolder().addRelationship(r);
            return;
        }
        if (element instanceof Package) {
            for (Relationship r : ((Package) element).getRelationships())
                offspring.getRelationshipHolder().addRelationship(r);
            return;
        }
    }

    private Set<Element> getChildren(Element cls) {
        GeneralizationRelationship g = getGeneralizationForClass(cls);
        if (g != null)
            return g.getAllChildrenForGeneralClass();
        return Collections.emptySet();
    }
}
