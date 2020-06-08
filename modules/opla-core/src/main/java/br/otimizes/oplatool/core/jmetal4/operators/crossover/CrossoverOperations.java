package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.exceptions.NotFoundException;
import br.otimizes.oplatool.architecture.exceptions.PackageNotFound;
import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Crossover operations methods
 */
public class CrossoverOperations {

    private static Logger LOGGER = LogManager.getLogger(CrossoverOperations.class.getName());

    public static void addAttributesRealizingFeatureToOffspring(Concern feature, Class classComp, Package comp, Architecture offspring) {
        Class targetClass = offspring.findClassByName(classComp.getName()).get(0);
        List<Attribute> allAttributes = new ArrayList<Attribute>(classComp.getAllAttributes());
        if (!allAttributes.isEmpty()) {
            Iterator<Attribute> iteratorAttributes = allAttributes.iterator();
            try {
                while (iteratorAttributes.hasNext()) {
                    Attribute attribute = iteratorAttributes.next();
                    if (attribute.containsConcern(feature) && attribute.getOwnConcerns().size() == 1) {
                        if (targetClass == null) {
                            Package newComp = null;
                            newComp = offspring.findPackageByName(comp.getName());
                            if (newComp == null)
                                newComp = offspring.createPackage(comp.getName());
                            targetClass = newComp.createClass(classComp.getName(), false);
                            targetClass.addConcern(feature.getName());
                        }
                        classComp.moveAttributeToClass(attribute, targetClass);
                    }
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void moveHierarchyToSameComponent(Class classComp, Package targetComp, Package sourceComp, Architecture offspring, Architecture parent, Concern concern) {
        Class root = classComp;
        while (isChild(root)) {
            root = getParent(root);
        }
        if (sourceComp.getAllClasses().contains(root)) {
            moveChildrenAndRelationshipsToSameComponent(root, sourceComp, targetComp, offspring, parent);
        }
    }

    private static void moveChildrenAndRelationshipsToSameComponent(Element parent, Package sourceComp, Package targetComp, Architecture offspring, Architecture parentArch) {

        Collection<Element> children = getChildren(parent);
        if (sourceComp.getAllClasses().contains(parent)) {
            addClassToOffspring(parent, targetComp, offspring);
        } else {
            System.out.println("Nao encontrou a superclasse");
        }

        for (Element child : children) {
            moveChildrenAndRelationshipsToSameComponent(child, sourceComp, targetComp, offspring, parentArch);
        }
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
        Class parent = null;
        for (Relationship relationship : cls.getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getChild().equals(cls)) {
                    parent = (Class) generalization.getParent();
                    return parent;
                }
            }
        }
        return parent;
    }

    public static void addClassToOffspring(Element klass, Package targetComp, Architecture offspring) {
        targetComp.addExternalClass((Class) klass);
    }

    public static Set<Element> getChildren(Element cls) {
        GeneralizationRelationship g = getGeneralizationForClass(cls);
        if (g != null)
            return g.getAllChildrenForGeneralClass();
        return Collections.emptySet();
    }

    public static void addInterfacesToOffspring(Concern feature, Package comp, Package newComp, Architecture offspring, Architecture parent) throws PackageNotFound, NotFoundException {
        List<Interface> allInterfaces = new ArrayList<Interface>(comp.getImplementedInterfaces());

        if (!allInterfaces.isEmpty()) {
            Iterator<Interface> iteratorInterfaces = allInterfaces.iterator();
            while (iteratorInterfaces.hasNext()) {
                Interface interfaceComp = iteratorInterfaces.next();
                interfaceComp.removeInterfaceFromRequiredOrImplemented();
                offspring.addExternalInterface(interfaceComp);
                offspring.addImplementedInterface(interfaceComp, newComp);

                Collection<DependencyRelationship> dependencies = interfaceComp.getDependencies();
                for (DependencyRelationship dependency : dependencies) {
                    Package dependent = offspring.findPackageByName(dependency.getPackageOfDependency().getName());
                    dependency.setClient(dependent);
                    offspring.addRelationship(dependency);
                }
            }
        }
    }

    private static GeneralizationRelationship getGeneralizationForClass(Element cls) {
        for (Relationship relationship : ((Class) cls).getRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
                if (generalization.getParent().equals(cls))
                    return (GeneralizationRelationship) relationship;
            }
        }

        return null;
    }
}