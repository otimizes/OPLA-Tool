package jmetal4.operators.crossover;

import arquitetura.representation.*;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;

import java.util.*;

public class CrossoverUtils {

    public boolean removeArchitecturalElementsRealizingFeature(Concern feature, Architecture offspring, String scope) {
        boolean ok = true;
        List<Package> allComponents = new ArrayList<Package>(offspring.getAllPackages());

        if (!allComponents.isEmpty()) {
            Iterator<Package> iteratorComponents = allComponents.iterator();
            while (iteratorComponents.hasNext()) {
                Package comp = iteratorComponents.next();
                if (comp.containsConcern(feature) && comp.getOwnConcerns().size() == 1) {
                    List<Interface> allInterfacesComp = new ArrayList<Interface>(comp.getImplementedInterfaces());
                    if (!allInterfacesComp.isEmpty()) {
                        Iterator<Interface> iteratorInterfaces = allInterfacesComp.iterator();
                        while (iteratorInterfaces.hasNext()) {
                            Interface interfaceComp = iteratorInterfaces.next();
                            offspring.removeInterface(interfaceComp);
                        }
                    }
                    this.removeClassesComponent(comp, offspring, scope);
                    // TODO não deveria remover o componente se ele tem classes
                    // em hierarquia... ver como resolver esta quest�o
                    // removeComponentRelationships(comp, offspring);
                    offspring.removePackage(comp);

                } else {
                    this.removeInterfacesComponentRealizingFeature(comp, feature, offspring);
                    this.removeClassesComponentRealizingFeature(comp, feature, offspring, scope);
                }
            }
        }
        return ok;
    }

    private boolean thereIsHierarchyInDifferentComponents(Package comp, Architecture architecture) {
        boolean sameComponent = false;
        for (Class class_ : comp.getAllClasses()) {
            if (this.searchForGeneralizations(class_))
                if (!(this.isHierarchyInASameComponent(class_, architecture))) {
                    sameComponent = true;
                    return true;
                }
        }
        return sameComponent;
    }

    private boolean searchForGeneralizations(Class cls) {

        Collection<Relationship> relationships = cls.getRelationships();
        for (Relationship relationship : relationships) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getChild().equals(cls) || generalization.getParent().equals(cls))
                    return true;
            }
        }
        return false;
    }

    private boolean isHierarchyInASameComponent(Class class_, Architecture architecture) {
        boolean sameComponent = true;
        Class parent = class_;
        Package componentOfClass = null;
        componentOfClass = architecture.findPackageOfClass(class_);
        Package componentOfParent = componentOfClass;
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

    private void removeClassesComponent(Package comp, Architecture offspring, String scope) {
        List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());
        if (!allClasses.isEmpty()) {
            Iterator<Class> iteratorClasses = allClasses.iterator();
            while (iteratorClasses.hasNext()) {
                Class classComp = iteratorClasses.next();
                if (comp.getAllClasses().contains(classComp)) {
                    // se n�o estiver numa hierarquia elimina os relacionamentos
                    // e a classe
                    if (!searchForGeneralizations(classComp)) {
                        // this.removeClassRelationships(classComp,offspring);
                        comp.removeClass(classComp);
                    } else { // tem que eliminar a hierarquia toda
                        removeHierarchyOfComponent(classComp, comp, offspring);
                    }
                }
            }
        }
    }

    private void removeHierarchyOfComponent(Class cls, Package comp, Architecture architecture) {
        Class parent = cls;

        while (CrossoverOperations.isChild(parent)) {
            parent = CrossoverOperations.getParent(parent);
        }
        removeChildrenOfComponent(parent, comp, architecture);
    }

    private void removeChildrenOfComponent(Element parent, Package comp, Architecture architecture) {

        Collection<Element> children = getChildren(parent);
        for (Element child : children) {
            removeChildrenOfComponent(child, comp, architecture);
        }
        if (comp.getAllClasses().contains(parent)) {
            comp.removeClass(parent);
        } else {
            for (Package auxComp : architecture.getAllPackages()) {
                if (auxComp.getAllClasses().contains(parent)) {
                    auxComp.removeClass(parent);
                    break;
                }
            }
        }
    }

    private Set<Element> getChildren(Element cls) {
        GeneralizationRelationship g = getGeneralizationForClass(cls);
        if (g == null)
            return Collections.emptySet();
        return g.getAllChildrenForGeneralClass();
    }

    private GeneralizationRelationship getGeneralizationForClass(Element cls) {
        for (Relationship relationship : ((Class) cls).getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getParent().equals(cls))
                    return (GeneralizationRelationship) relationship;
            }
        }
        return null;
    }

    private void removeInterfacesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring) {
        List<Interface> allInterfaces = new ArrayList<Interface>(comp.getImplementedInterfaces());

        if (!allInterfaces.isEmpty()) {
            Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
            while (iteratorInterfaces.hasNext()) {
                Interface interfaceComp = iteratorInterfaces.next();
                if (interfaceComp.containsConcern(feature) && interfaceComp.getOwnConcerns().size() == 1)
                    offspring.removeInterface(interfaceComp);
                else
                    removeOperationsOfInterfaceRealizingFeature(interfaceComp, feature);
            }
        }
    }

    private void removeOperationsOfInterfaceRealizingFeature(Interface interfaceComp, Concern feature) {
        List<Method> operationsInterfaceComp = new ArrayList<Method>(interfaceComp.getOperations());
        if (!operationsInterfaceComp.isEmpty()) {
            Iterator<Method> iteratorOperations = operationsInterfaceComp.iterator();
            while (iteratorOperations.hasNext()) {
                Method operation = iteratorOperations.next();
                if (operation.containsConcern(feature) && operation.getOwnConcerns().size() == 1)
                    interfaceComp.removeOperation(operation);
            }
        }
    }

    private void removeClassesComponentRealizingFeature(Package comp, Concern feature, Architecture offspring,
                                                        String scope) {
        List<Class> allClasses = new ArrayList<Class>(comp.getAllClasses());

        if (!allClasses.isEmpty()) {
            Iterator<Class> iteratorClasses = allClasses.iterator();
            while (iteratorClasses.hasNext()) {
                Class classComp = iteratorClasses.next();
                if ((classComp.containsConcern(feature)) && (classComp.getOwnConcerns().size() == 1)) {
                    // se não estiver numa hierarquia elimina os relacionamentos
                    // e a classe
                    if (!searchForGeneralizations(classComp)) {
                        comp.removeClass(classComp);
                    } else { // tem que eliminar a hierarquia toda
                        removeHierarchyOfComponent(classComp, comp, offspring);
                    }
                } else {
                    if (scope == "allLevels") {
                        removeAttributesOfClassRealizingFeature(classComp, feature);
                        removeMethodsOfClassRealizingFeature(classComp, feature);
                    }
                }
            }
        }
    }

    private void removeAttributesOfClassRealizingFeature(Class cls, Concern feature) {
        List<Attribute> attributesClassComp = new ArrayList<Attribute>(cls.getAllAttributes());
        if (!attributesClassComp.isEmpty()) {
            Iterator<Attribute> iteratorAttributes = attributesClassComp.iterator();
            while (iteratorAttributes.hasNext()) {
                Attribute attribute = iteratorAttributes.next();
                if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size() == 1) {
                    if (!searchForGeneralizations(cls))
                        cls.removeAttribute(attribute);
                }
            }
        }
    }

    private void removeMethodsOfClassRealizingFeature(Class cls, Concern feature) {
        List<Method> methodsClassComp = new ArrayList<Method>(cls.getAllMethods());
        if (!methodsClassComp.isEmpty()) {
            Iterator<Method> iteratorMethods = methodsClassComp.iterator();
            while (iteratorMethods.hasNext()) {
                Method method = iteratorMethods.next();
                if (method.containsConcern(feature) && method.getOwnConcerns().size() == 1) {
                    if (!searchForGeneralizations(cls))
                        cls.removeMethod(method);
                }
            }
        }
    }

}