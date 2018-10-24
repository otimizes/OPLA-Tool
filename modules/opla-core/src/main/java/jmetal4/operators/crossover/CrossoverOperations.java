package jmetal4.operators.crossover;

import arquitetura.exceptions.NotFoundException;
import arquitetura.exceptions.PackageNotFound;
import arquitetura.representation.*;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.GeneralizationRelationship;
import arquitetura.representation.relationship.Relationship;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;

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

//	public static void updateClassRelationships(Element classComp, Architecture offspring ) {
//		Collection<Relationship> parentRelationships = ((Class) classComp).getRelationships();
//		for (Relationship relationship : parentRelationships){
//			if (relationship instanceof DependencyRelationship){
//				DependencyRelationship dependency = (DependencyRelationship) relationship;
//				Element client = dependency.getClient();
//				Element supplier = dependency.getSupplier();
//				
//				try{
//					Element clientOffSpring = offspring.findClassByName(client.getName()).get(0);
//					Element supplierOffSpring = offspring.findClassByName(supplier.getName()).get(0);
//					offspring.forDependency().create(dependency.getName()).withClient(clientOffSpring).withSupplier(supplierOffSpring).build();
//				}catch(ClassNotFound e){
//					LOGGER.info("Tentou criar DependencyRelationship em offspring. Porém não encontrou alguma classe: " + e.getMessage());
//				}
//			}
//			
//			if (relationship instanceof UsageRelationship){
//				UsageRelationship dependency = (UsageRelationship) relationship;
//				Element client = dependency.getClient();
//				Element supplier = dependency.getSupplier();
//				
//				try{
//					Element clientOffSpring = offspring.findClassByName(client.getName()).get(0);
//					Element supplierOffSpring = offspring.findClassByName(supplier.getName()).get(0);
//					offspring.forUsage().create(clientOffSpring, supplierOffSpring);
//				}catch(ClassNotFound e){
//					LOGGER.info("Tentou criar UsageRelationship em offspring. Porém não encontrou alguma classe: " + e.getMessage());
//				}
//			}
//			
//			if (relationship instanceof RealizationRelationship){
//				RealizationRelationship dependency = (RealizationRelationship) relationship;
//				Element client = dependency.getClient();
//				Element supplier = dependency.getSupplier();
//				
//				try{
//					Element clientOffSpring = offspring.findClassByName(client.getName()).get(0);
//					Element supplierOffSpring = offspring.findClassByName(supplier.getName()).get(0);
//					offspring.operationsOverRelationship().createNewRealization(clientOffSpring, supplierOffSpring);
//				}catch(ClassNotFound e){
//					LOGGER.info("Tentou criar RealizationRelationship em offspring. Porém não encontrou alguma classe: " + e.getMessage());
//				}
//			}
//			
//			if (relationship instanceof AbstractionRelationship){
//				AbstractionRelationship dependency = (AbstractionRelationship) relationship;
//				Element client = dependency.getClient();
//				Element supplier = dependency.getSupplier();
//				
//				try{
//					Element clientOffSpring = offspring.findClassByName(client.getName()).get(0);
//					Element supplierOffSpring = offspring.findClassByName(supplier.getName()).get(0);
//					offspring.forAbstraction().create(clientOffSpring, supplierOffSpring);
//				}catch(ClassNotFound e){
//					LOGGER.info("Tentou criar Dependency em offspring. Porém não encontrou alguma classe: " + e.getMessage());
//				}
//				
//			}
//			
//			if(relationship instanceof AssociationRelationship){
//				AssociationRelationship association = (AssociationRelationship) relationship;
//				List<AssociationEnd> participants = association.getParticipants();
//				
//				AssociationEnd p1 = participants.get(0);
//				AssociationEnd p2 = participants.get(1);
//				
//				try{
//					Class p1offspring = offspring.findClassByName(p1.getName()).get(0);
//					Class p2offspring =offspring.findClassByName(p2.getName()).get(0);
//					
//					AssociationEnd associationEndOffSpring = new AssociationEnd();
//					associationEndOffSpring.setAggregation(p1.getAggregation());
//					associationEndOffSpring.setNavigable(p1.isNavigable());
//					associationEndOffSpring.setMultiplicity(p1.getMultiplicity());
//					associationEndOffSpring.setCLSClass(p1offspring);
//					
//					AssociationEnd associationEndOffSpring2 = new AssociationEnd();
//					associationEndOffSpring2.setAggregation(p2.getAggregation());
//					associationEndOffSpring2.setNavigable(p2.isNavigable());
//					associationEndOffSpring2.setMultiplicity(p2.getMultiplicity());
//					associationEndOffSpring2.setCLSClass(p2offspring);
//					
//					offspring.forAssociation().create(associationEndOffSpring, associationEndOffSpring2);
//				}catch(ClassNotFound e){
//					LOGGER.info("Tentou criar Association em offspring. Porém não encontrou alguma classe: " + e.getMessage());
//				}catch (Exception e) {
//					
//				}
//			}
//			
//			if (relationship instanceof AssociationClassRelationship){
//				AssociationClassRelationship asc = (AssociationClassRelationship)relationship;
//				try{
//					Class offspringMember1 = offspring.findClassByName(asc.getMemebersEnd().get(0).getType().getName()).get(0);
//					Class offspringMember2 = offspring.findClassByName(asc.getMemebersEnd().get(1).getType().getName()).get(0);
//					offspring.forAssociation().createAssociationClass(asc.getAllAttributes(), asc.getAllMethods(), offspringMember1, offspringMember2, asc.getAssociationClass().getName());
//				}catch(ClassNotFound e){
//					LOGGER.info("Tentou criar AssociationClass em offspring. Porém não encontrou alguma classe: " + e.getMessage());
//				}
//			}
//			
//			if(relationship instanceof GeneralizationRelationship){
//				GeneralizationRelationship generalization = (GeneralizationRelationship) relationship;
//				
//				Element parent = generalization.getParent();
//				Element child = generalization.getChild();
//				
//				try{
//					Element parentOffSpring = offspring.findClassByName(parent.getName()).get(0);
//					Element childOffSpring = offspring.findClassByName(child.getName()).get(0);
//					offspring.forGeneralization().createGeneralization(parentOffSpring, childOffSpring);
//				}catch(ClassNotFound e){
//					LOGGER.info("Tentou criar Generalization em offspring. Porém não encontrou alguma classe: " + e.getMessage());
//				}
//			}
//		}
//	}

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
        //move a super classe
        if (sourceComp.getAllClasses().contains(parent)) {
            addClassToOffspring(parent, targetComp, offspring);
            //sourceComp.moveClassToComponent(parent, targetComp);
            //this.updateClassRelationships(parent, targetComp, sourceComp, offspring, parentArch);
        } else {
            System.out.println("Nao encontrou a superclasse");
        }

        //move cada subclasse
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

    /*
     * método para identificar as subclasses da classe pai na hierarquia de herança
     *
     */
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