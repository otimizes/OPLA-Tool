package br.otimizes.oplatool.architecture.representation.relationship;

import br.otimizes.oplatool.architecture.representation.Element;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Relationship commons class
 */
public class RelationshipCommons {

    public static Set<Relationship> getRelationships(Set<Relationship> relationships, Element element) {
        Set<Relationship> relations = new HashSet<>();
        for (Relationship r : relationships) {
            if (r instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) r).getParent().equals(element) || ((GeneralizationRelationship) r).getChild().equals(element)) {
                    relations.add(r);
                }
            }
            if (r instanceof RealizationRelationship) {
                if (((RealizationRelationship) r).getClient().equals(element) || ((RealizationRelationship) r).getSupplier().equals(element)) {
                    relations.add(r);
                }
            }
            if (r instanceof DependencyRelationship) {
                if (((DependencyRelationship) r).getClient().equals(element) || ((DependencyRelationship) r).getSupplier().equals(element)) {
                    relations.add(r);
                }
            }
            if (r instanceof UsageRelationship) {
                if (((UsageRelationship) r).getClient().equals(element) || ((UsageRelationship) r).getSupplier().equals(element)) {
                    relations.add(r);
                }
            }
            if (r instanceof AbstractionRelationship) {
                if (((AbstractionRelationship) r).getClient().equals(element) || ((AbstractionRelationship) r).getSupplier().equals(element)) {
                    relations.add(r);
                }
            }
            if (r instanceof AssociationRelationship) {
                for (AssociationEnd a : ((AssociationRelationship) r).getParticipants()) {
                    if (a != null && a.getCLSClass() != null && a.getCLSClass().equals(element)) {
                        relations.add(r);
                    }
                }
            }

            if (r instanceof AssociationClassRelationship) {
                for (MemberEnd memberEnd : ((AssociationClassRelationship) r).getMembersEnd()) {
                    if (memberEnd != null && memberEnd.getType() != null && memberEnd.getType().equals(element)) {
                        relations.add(r);
                    }
                }
            }
        }
        return Collections.unmodifiableSet(relations);
    }

}
