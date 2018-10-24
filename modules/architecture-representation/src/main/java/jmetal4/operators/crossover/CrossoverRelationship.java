package jmetal4.operators.crossover;

import arquitetura.representation.*;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.Relationship;

import java.util.HashSet;
import java.util.Set;

public class CrossoverRelationship {

    private static Set<Relationship> relationships = new HashSet<Relationship>();

    public static void saveAllRelationshiopForElement(Element element, Architecture parent, Architecture offspring) {

        if (element instanceof Package) {
            Package packagee = (Package) element;
            Set<Relationship> relations = packagee.getRelationships();
            for (Relationship r : relations)
                offspring.getRelationshipHolder().addRelationship(r);

            relations = null;
            return;
        }
        if (element instanceof Class) {
            Class klass = (Class) element;
            Set<Relationship> relations = klass.getRelationships();
            for (Relationship r : relations)
                offspring.getRelationshipHolder().addRelationship(r);

            relations = null;
            return;
        }
        if (element instanceof Interface) {
            Interface inter = (Interface) element;
            Set<Relationship> relations = inter.getRelationships();
            for (Relationship r : relations)
                offspring.getRelationshipHolder().addRelationship(r);

            relations = null;
            return;
        }
//		
//		for(Relationship r : parentRelationships){
//			if(r instanceof DependencyRelationship){
//				if(((DependencyRelationship) r).getClient().equals(element) || (((DependencyRelationship) r).getSupplier().equals(element)))
//					relationships.add(r);
//			}
//			if(r instanceof RealizationRelationship){
//				if(((RealizationRelationship) r).getClient().equals(element) || (((RealizationRelationship) r).getSupplier().equals(element)) ){
//					relationships.add(r);
//				}
//			}
//			if(r instanceof UsageRelationship){
//				if(((UsageRelationship) r).getClient().equals(element) || (((UsageRelationship) r).getSupplier().equals(element)) ){
//					relationships.add(r);
//				}
//			}
//			if(r instanceof AbstractionRelationship){
//				if(((AbstractionRelationship) r).getClient().equals(element) || (((AbstractionRelationship) r).getSupplier().equals(element)) ){
//					relationships.add(r);
//				}
//			}
//			
//			if(r instanceof GeneralizationRelationship){
//				if(((GeneralizationRelationship) r).getParent().equals(element) || (((GeneralizationRelationship) r).getChild().equals(element)) ){
//					relationships.add(r);
//				}
//			}
//			
//			if(r instanceof AssociationRelationship){
//				AssociationRelationship association = (AssociationRelationship)r;
//				for(AssociationEnd ase : association.getParticipants()){
//					if(ase.getCLSClass().equals(element))
//						relationships.add(r);
//				}
//			}
//		}

    }

    public static void cleanRelationships() {
        relationships.clear();
    }

}