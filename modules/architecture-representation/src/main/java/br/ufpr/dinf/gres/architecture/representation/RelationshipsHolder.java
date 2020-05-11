package br.ufpr.dinf.gres.architecture.representation;

import br.ufpr.dinf.gres.architecture.helpers.Predicate;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.representation.relationship.*;

import java.util.*;

public class RelationshipsHolder {

    private Set<Relationship> relationships = new HashSet<Relationship>();


    public void clearLists() {
        relationships.clear();
    }

    public Set<Relationship> getRelationships() {
        return Collections.unmodifiableSet(relationships);
    }

    public void setRelationships(Set<Relationship> rs) {
        relationships = rs;
    }

    /**
     * Dado um {@link Element} remove todos relacionamentos em que o elemento esteja envolvido
     *
     * @param element
     */
    public void removeRelatedRelationships(Element element) {
        for (Iterator<Relationship> i = relationships.iterator(); i.hasNext(); ) {
            Relationship r = i.next();
            if (r instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) r).getParent().equals(element) || ((GeneralizationRelationship) r).getChild().equals(element)) {
                    i.remove();
                }
            }
            if (r instanceof RealizationRelationship) {
                if (((RealizationRelationship) r).getClient().equals(element) || ((RealizationRelationship) r).getSupplier().equals(element)) {
                    i.remove();
                }
            }
            if (r instanceof DependencyRelationship) {
                if (((DependencyRelationship) r).getClient().equals(element) || ((DependencyRelationship) r).getSupplier().equals(element)) {
                    i.remove();
                }
            }
            if (r instanceof AbstractionRelationship) {
                if (((AbstractionRelationship) r).getClient().equals(element) || ((AbstractionRelationship) r).getSupplier().equals(element)) {
                    i.remove();
                }
            }
            if (r instanceof AssociationRelationship) {
                for (AssociationEnd a : ((AssociationRelationship) r).getParticipants()) {
                    if (a.getCLSClass().equals(element)) {
                        i.remove();
                    }
                }
            }

            if (r instanceof AssociationClassRelationship) {
                for (MemberEnd memberEnd : ((AssociationClassRelationship) r).getMemebersEnd()) {
                    if (memberEnd.getType().equals(element)) {
                        i.remove();
                    }

                }
            }
        }
    }

    public Set<Relationship> getAllRelationships() {
        return Collections.unmodifiableSet(getRelationships());
    }

    public List<GeneralizationRelationship> getAllGeneralizations() {
        Predicate<Relationship> isValid = new Predicate<Relationship>() {
            public boolean apply(Relationship parent) {
                return parent instanceof GeneralizationRelationship;
            }
        };

        final List<GeneralizationRelationship> generalizations = UtilResources.filter(getRelationships(), isValid);

        return generalizations;
    }

    public List<AssociationRelationship> getAllAssociationsRelationships() {
        final List<AssociationRelationship> associations = getAllAssociations();
        final List<AssociationRelationship> association = new ArrayList<AssociationRelationship>();
        for (AssociationRelationship associationRelationship : associations) {
            if ((notComposition(associationRelationship)) && (notAgregation(associationRelationship))) {
                association.add(associationRelationship);
            }
        }
        return association;

    }

    private boolean notAgregation(AssociationRelationship associationRelationship) {
        return ((!associationRelationship.getParticipants().get(0).isAggregation()) && (!associationRelationship.getParticipants().get(1).isAggregation()));
    }

    private boolean notComposition(AssociationRelationship associationRelationship) {
        return ((!associationRelationship.getParticipants().get(0).isComposite()) && (!associationRelationship.getParticipants().get(1).isComposite()));
    }

    public List<AssociationRelationship> getAllAssociations() {
        Predicate<Relationship> isValid = new Predicate<Relationship>() {
            public boolean apply(Relationship parent) {
                return parent instanceof AssociationRelationship;
            }
        };

        final List<AssociationRelationship> allAssociations = UtilResources.filter(getRelationships(), isValid);

        return allAssociations;

    }

    public List<AssociationRelationship> getAllCompositions() {
        final List<AssociationRelationship> associations = getAllAssociations();
        final List<AssociationRelationship> compositions = new ArrayList<AssociationRelationship>();
        for (AssociationRelationship associationRelationship : associations) {
            if ((associationRelationship.getParticipants().get(0).isComposite()) || (associationRelationship.getParticipants().size() > 1 && associationRelationship.getParticipants().get(1).isComposite())) {
                compositions.add(associationRelationship);
            }
        }
        return compositions;
    }

    public List<AssociationRelationship> getAllAgragations() {
        final List<AssociationRelationship> associations = getAllAssociations();
        final List<AssociationRelationship> agragation = new ArrayList<AssociationRelationship>();
        for (AssociationRelationship associationRelationship : associations) {
            if ((associationRelationship.getParticipants().get(0).isAggregation()) || (associationRelationship.getParticipants().size() > 1 && associationRelationship.getParticipants().get(1).isAggregation())) {
                agragation.add(associationRelationship);
            }
        }
        return agragation;
    }

    public List<UsageRelationship> getAllUsage() {
        Predicate<Relationship> isValid = new Predicate<Relationship>() {
            public boolean apply(Relationship parent) {
                return parent instanceof UsageRelationship;
            }
        };

        return UtilResources.filter(getRelationships(), isValid);
    }

    public List<DependencyRelationship> getAllDependencies() {
        Predicate<Relationship> isValid = new Predicate<Relationship>() {
            public boolean apply(Relationship parent) {
                return parent instanceof DependencyRelationship;
            }
        };

        return UtilResources.filter(getRelationships(), isValid);
    }

    public List<RealizationRelationship> getAllRealizations() {
        Predicate<Relationship> realizations = new Predicate<Relationship>() {
            public boolean apply(Relationship parent) {
                return parent instanceof RealizationRelationship;
            }
        };

        return UtilResources.filter(getRelationships(), realizations);
    }

    public List<AbstractionRelationship> getAllAbstractions() {
        Predicate<Relationship> realizations = new Predicate<Relationship>() {
            public boolean apply(Relationship parent) {
                return parent instanceof AbstractionRelationship;
            }
        };

        return UtilResources.filter(getRelationships(), realizations);

    }


    public List<AssociationClassRelationship> getAllAssociationsClass() {
        Predicate<Relationship> associationClasses = new Predicate<Relationship>() {
            public boolean apply(Relationship parent) {
                return parent instanceof AssociationClassRelationship;
            }
        };

        return UtilResources.filter(getRelationships(), associationClasses);
    }

    public boolean haveRelationship(Relationship relationship) {
        //Association
        for (Relationship r : getAllRelationships()) {
            if ((r instanceof AssociationRelationship) && (relationship instanceof AssociationRelationship)) {
                final List<AssociationEnd> participantsNew = ((AssociationRelationship) relationship).getParticipants();
                final List<AssociationEnd> participantsExists = ((AssociationRelationship) r).getParticipants();

                if (participantsNew.equals(participantsExists))
                    return true;
            }
        }

        if (relationship instanceof GeneralizationRelationship)
            if (getAllGeneralizations().contains(relationship)) return true;
        if (relationship instanceof DependencyRelationship)
            if (getAllDependencies().contains(relationship)) return true;
        if (relationship instanceof UsageRelationship)
            if (getAllUsage().contains(relationship)) return true;
        if (relationship instanceof RealizationRelationship)
            if (getAllRealizations().contains(relationship)) return true;
        if (relationship instanceof AbstractionRelationship)
            if (getAllAbstractions().contains(relationship)) return true;
        if (relationship instanceof AssociationClassRelationship)
            return getAllAssociationsClass().contains(relationship);

        return false;

    }

    public boolean removeRelationship(Relationship relation) {
        return this.relationships.remove(relation);
    }

    public boolean addRelationship(Relationship relationship) {
        if (!haveRelationship(relationship)) {
            return this.relationships.add(relationship);
        }
        return false;
    }

}
