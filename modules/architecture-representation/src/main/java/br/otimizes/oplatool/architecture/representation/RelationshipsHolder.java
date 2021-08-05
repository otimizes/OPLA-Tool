package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.Predicate;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.*;

import java.util.*;

/**
 * Relationships holder class
 */
public class RelationshipsHolder {

    private Set<Relationship> relationships = new HashSet<>();

    public void clearLists() {
        relationships.clear();
    }

    public Set<Relationship> getRelationships() {
        return Collections.unmodifiableSet(relationships);
    }

    public void setRelationships(Set<Relationship> relationships) {
        this.relationships = relationships;
    }

    public void removeRelatedRelationships(Element element) {
        for (Iterator<Relationship> relationshipIterator = relationships.iterator(); relationshipIterator.hasNext(); ) {
            Relationship relationship = relationshipIterator.next();
            if (relationship instanceof GeneralizationRelationship) {
                if (((GeneralizationRelationship) relationship).getParent().equals(element)
                        || ((GeneralizationRelationship) relationship).getChild().equals(element)) {
                    relationshipIterator.remove();
                }
            }
            if (relationship instanceof RealizationRelationship) {
                if (((RealizationRelationship) relationship).getClient().equals(element)
                        || ((RealizationRelationship) relationship).getSupplier().equals(element)) {
                    relationshipIterator.remove();
                }
            }
            if (relationship instanceof DependencyRelationship) {
                if (((DependencyRelationship) relationship).getClient().equals(element)
                        || ((DependencyRelationship) relationship).getSupplier().equals(element)) {
                    relationshipIterator.remove();
                }
            }
            if (relationship instanceof AbstractionRelationship) {
                if (((AbstractionRelationship) relationship).getClient().equals(element) || ((AbstractionRelationship) relationship).getSupplier().equals(element)) {
                    relationshipIterator.remove();
                }
            }
            if (relationship instanceof AssociationRelationship) {
                boolean remove = false;
                for (AssociationEnd associationEnd : ((AssociationRelationship) relationship).getParticipants()) {
                    if (associationEnd.getCLSClass() != null && associationEnd.getCLSClass().equals(element)) {
                        remove = true;
                        break;
                    }
                }
                if (remove) {
                    relationshipIterator.remove();
                }
            }

            if (relationship instanceof AssociationClassRelationship) {
                boolean remove = false;
                for (MemberEnd memberEnd : ((AssociationClassRelationship) relationship).getMembersEnd()) {
                    if (memberEnd.getType().equals(element)) {
                        remove = true;
                        break;
                    }
                }
                if (remove) {
                    relationshipIterator.remove();
                }
            }
        }
    }

    public void forceAddAssociationRelationshipsChild(Relationship relationship, Architecture target) {
        if (relationship instanceof AssociationRelationship) {
            AssociationRelationship associationRelationship = (AssociationRelationship) relationship;
            if (associationRelationship.getParticipants().size() == 2) {
                Element firstPart = target.findElementById((((AssociationRelationship) relationship)
                        .getParticipants()).get(0).getCLSClass().getId());
                Element secondPart = target.findElementById((((AssociationRelationship) relationship)
                        .getParticipants()).get(1).getCLSClass().getId());
                if (firstPart != null && secondPart != null) {
                    if ((associationRelationship.getParticipants().get(0).getMultiplicity() != null)
                            && (associationRelationship.getParticipants().get(1).getMultiplicity() != null)) {
                        createRelationshipInArchitecture(relationship, target, firstPart, secondPart);
                    }
                } else {
                    if (firstPart == null) {
                        Class cpai = (Class) associationRelationship.getParticipants().get(0).getCLSClass();
                        for (Relationship relationship1 : cpai.getRelationships()) {
                            if (relationship1 instanceof AssociationRelationship) {
                                AssociationRelationship firstAssociationRelationship = (AssociationRelationship) relationship1;
                                if (firstAssociationRelationship.getParticipants().get(0).getCLSClass().getId().equals(secondPart.getId()))
                                    continue;
                                if (firstAssociationRelationship.getParticipants().get(1).getCLSClass().getId().equals(secondPart.getId()))
                                    continue;
                                if (firstAssociationRelationship.getParticipants().get(1).getCLSClass().getId().equals(associationRelationship.getParticipants().get(0).getCLSClass().getId()))
                                    firstPart = target.findClassById(firstAssociationRelationship.getParticipants().get(0).getCLSClass().getId());
                                else {
                                    firstPart = target.findClassById(firstAssociationRelationship.getParticipants().get(1).getCLSClass().getId());
                                }
                                createRelationshipInArchitecture(relationship, target, firstPart, secondPart);
                            }
                        }
                    } else {
                        Class clsClass = (Class) associationRelationship.getParticipants().get(1).getCLSClass();
                        for (Relationship relationshipFromCLSClass : clsClass.getRelationships()) {
                            if (relationshipFromCLSClass instanceof AssociationRelationship) {
                                AssociationRelationship firstAssociationRelationship = (AssociationRelationship) relationshipFromCLSClass;
                                if (firstAssociationRelationship.getParticipants().get(0).getCLSClass().getId().equals(firstPart.getId()))
                                    continue;
                                if (firstAssociationRelationship.getParticipants().get(1).getCLSClass().getId().equals(firstPart.getId()))
                                    continue;
                                if (firstAssociationRelationship.getParticipants().get(1).getCLSClass().getId().equals(associationRelationship
                                        .getParticipants().get(1).getCLSClass().getId()))
                                    secondPart = target.findClassById(firstAssociationRelationship.getParticipants().get(0).getCLSClass().getId());
                                else {
                                    secondPart = target.findClassById(firstAssociationRelationship.getParticipants().get(1).getCLSClass().getId());
                                }
                                createRelationshipInArchitecture(relationship, target, firstPart, secondPart);
                            }
                        }
                    }
                }
            }
        }
    }

    public void forceAddRealization(Relationship relationship, Architecture target) {
        RealizationRelationship realizationRelationship = (RealizationRelationship) relationship;
        for (Relationship relationshipClient : ((Class) realizationRelationship.getClient()).getRelationships()) {
            if (relationshipClient instanceof AssociationRelationship) {
                AssociationRelationship associationRelationship = (AssociationRelationship) relationshipClient;
                Element clsClass = associationRelationship.getParticipants().get(0).getCLSClass();
                if (clsClass.getId().equals(realizationRelationship.getClient().getId())) {
                    clsClass = associationRelationship.getParticipants().get(1).getCLSClass();
                }
                Element client = target.findElementById(clsClass.getId());
                Element supplier = target.findElementById(realizationRelationship.getSupplier().getId());
                if (client != null && supplier != null) {
                    RealizationRelationship newRelation = new RealizationRelationship(client, supplier, relationship.getName(), relationship.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    ((Class) client).addImplementedInterface((Interface) supplier);
                }
            }
        }
    }

    public void verifyAndAddRelationshipsChild2(Relationship relationship, Architecture target) {
        if (verifyAndAddGeneralizationRelationship(relationship, target)) return;
        if (verifyAndAddUsageRelationship(relationship, target)) return;
        if (verifyAndAddRealizationRelationship(relationship, target)) return;
        if (verifyAndAddDependencyRelationship(relationship, target)) return;
        if (verifyAndAddAbstractionRelationship(relationship, target)) return;
        if (verifyAndAddAssociationRelationship(relationship, target)) return;
        verifyAndAddAssociationClassRelationship(relationship, target);
    }

    private boolean verifyAndAddGeneralizationRelationship(Relationship relationship, Architecture target) {
        if (relationship instanceof GeneralizationRelationship) {
            if ((((GeneralizationRelationship) relationship).getParent() != null) && (((GeneralizationRelationship) relationship).getChild() != null)) {
                Element parent = target.findElementById(((GeneralizationRelationship) relationship).getParent().getId());
                Element child = target.findElementById(((GeneralizationRelationship) relationship).getChild().getId());
                if (parent != null && child != null) {
                    GeneralizationRelationship newRelation = new GeneralizationRelationship(parent, child, target.getRelationshipHolder(), relationship.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyAndAddUsageRelationship(Relationship relationship, Architecture target) {
        if (relationship instanceof UsageRelationship) {
            if ((((UsageRelationship) relationship).getClient() != null) && (((UsageRelationship) relationship).getSupplier() != null)) {
                Element client = target.findElementById(((UsageRelationship) relationship).getClient().getId());
                Element supplier = target.findElementById(((UsageRelationship) relationship).getSupplier().getId());
                if (client != null && supplier != null) {
                    UsageRelationship newRelation = new UsageRelationship(relationship.getName(), supplier, client, relationship.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return true;
                }
            }

        }
        return false;
    }

    private boolean verifyAndAddRealizationRelationship(Relationship relationship, Architecture target) {
        if (relationship instanceof RealizationRelationship) {
            if ((((RealizationRelationship) relationship).getClient() != null) && (((RealizationRelationship) relationship).getSupplier() != null)) {
                Element client = target.findElementById(((RealizationRelationship) relationship).getClient().getId());
                Element supplier = target.findElementById(((RealizationRelationship) relationship).getSupplier().getId());
                if (client != null && supplier != null) {
                    RealizationRelationship newRelation = new RealizationRelationship(client, supplier, relationship.getName(), relationship.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyAndAddDependencyRelationship(Relationship relationship, Architecture target) {
        if (relationship instanceof DependencyRelationship) {
            if ((((DependencyRelationship) relationship).getClient() != null) && (((DependencyRelationship) relationship).getSupplier() != null)) {
                Element supplier = target.findElementById(((DependencyRelationship) relationship).getSupplier().getId());
                Element client = target.findElementById(((DependencyRelationship) relationship).getClient().getId());
                if (supplier != null && client != null) {
                    DependencyRelationship newRelation = new DependencyRelationship(supplier, client, relationship.getName(), relationship.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return true;
                }

            }
        }
        return false;
    }

    private boolean verifyAndAddAbstractionRelationship(Relationship relationship, Architecture target) {
        if (relationship instanceof AbstractionRelationship) {
            if ((((AbstractionRelationship) relationship).getClient() != null) && (((AbstractionRelationship) relationship).getSupplier() != null)) {
                Element client = target.findElementById(((AbstractionRelationship) relationship).getClient().getId());
                Element supplier = target.findElementById(((AbstractionRelationship) relationship).getSupplier().getId());
                if (client != null && supplier != null) {
                    AbstractionRelationship newRelation = new AbstractionRelationship(client, supplier, relationship.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean verifyAndAddAssociationRelationship(Relationship relationship, Architecture target) {
        if (relationship instanceof AssociationRelationship) {
            if (((AssociationRelationship) relationship).getParticipants().size() == 2) {
                Element firstPart = target.findElementById((((AssociationRelationship) relationship).getParticipants()).get(0).getCLSClass().getId());
                Element secondPart = target.findElementById((((AssociationRelationship) relationship).getParticipants()).get(1).getCLSClass().getId());
                if (firstPart != null && secondPart != null) {
                    if ((((AssociationRelationship) relationship).getParticipants()).get(0).getMultiplicity() != null && (((AssociationRelationship) relationship).getParticipants()).get(1).getMultiplicity() != null) {
                        createRelationshipInArchitecture(relationship, target, firstPart, secondPart);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void createRelationshipInArchitecture(Relationship relationship, Architecture target, Element firstPart, Element secondPart) {
        AssociationRelationship associationRelationship = new AssociationRelationship(relationship.getId());
        Multiplicity firstMultiplicity = new Multiplicity((((AssociationRelationship) relationship).getParticipants())
                .get(0).getMultiplicity().getLowerValue(), (((AssociationRelationship) relationship).getParticipants())
                .get(0).getMultiplicity().getUpperValue());
        AssociationEnd firstAssociationEnd = new AssociationEnd(firstPart, (((AssociationRelationship) relationship)
                .getParticipants()).get(0).isNavigable(), (((AssociationRelationship) relationship).getParticipants())
                .get(0).getAggregation(), firstMultiplicity, (((AssociationRelationship) relationship).getParticipants())
                .get(0).getName());
        Multiplicity secondMultiplicity = new Multiplicity((((AssociationRelationship) relationship).getParticipants())
                .get(1).getMultiplicity().getLowerValue(), (((AssociationRelationship) relationship).getParticipants())
                .get(1).getMultiplicity().getUpperValue());
        AssociationEnd secondAssociationEnd = new AssociationEnd(secondPart, (((AssociationRelationship) relationship)
                .getParticipants()).get(1).isNavigable(), (((AssociationRelationship) relationship).getParticipants())
                .get(1).getAggregation(), secondMultiplicity, (((AssociationRelationship) relationship).getParticipants())
                .get(1).getName());
        associationRelationship.SetAssociationEnd(firstAssociationEnd);
        associationRelationship.SetAssociationEnd(secondAssociationEnd);
        target.getRelationshipHolder().addRelationship(associationRelationship);
    }

    private boolean verifyAndAddAssociationClassRelationship(Relationship relationship, Architecture target) {
        if (relationship instanceof AssociationClassRelationship) {
            if (((AssociationClassRelationship) relationship).getMembersEnd().size() == 2) {
                Element firstPart = target.findElementById((((AssociationClassRelationship) relationship).getMembersEnd())
                        .get(0).getType().getId());
                Element secondPart = target.findElementById((((AssociationClassRelationship) relationship).getMembersEnd())
                        .get(1).getType().getId());
                if (secondPart != null && firstPart != null) {
                    if ((((AssociationClassRelationship) relationship).getMembersEnd()).get(0).getMultiplicity() != null
                            && (((AssociationClassRelationship) relationship).getMembersEnd()).get(1).getMultiplicity() != null) {
                        AssociationClassRelationship associationClassRelationship = new AssociationClassRelationship(
                                relationship.getName(), ((AssociationClassRelationship) relationship).getMembersEnd(),
                                ((AssociationClassRelationship) relationship).getOwnedEnd(), relationship.getId(),
                                ((AssociationClassRelationship) relationship).getOwnedEnd().getId(),
                                ((AssociationClassRelationship) relationship).getAssociationClass());
                        Multiplicity firstMultiplicity = new Multiplicity((((AssociationClassRelationship) relationship)
                                .getMembersEnd()).get(0).getMultiplicity().getLowerValue(), (((AssociationClassRelationship) relationship)
                                .getMembersEnd()).get(0).getMultiplicity().getUpperValue());
                        MemberEnd firstMemberEnd = new MemberEnd((((AssociationClassRelationship) relationship).getMembersEnd()
                                .get(0).getAggregation()), firstMultiplicity, (((AssociationClassRelationship) relationship)
                                .getMembersEnd()).get(0).getVisibility(), target.findElementById(((AssociationClassRelationship) relationship)
                                .getMembersEnd().get(0).getType().getId()));
                        Multiplicity secondMultiplicity = new Multiplicity((((AssociationClassRelationship) relationship)
                                .getMembersEnd()).get(1).getMultiplicity().getLowerValue(), (((AssociationClassRelationship) relationship)
                                .getMembersEnd()).get(1).getMultiplicity().getUpperValue());
                        MemberEnd secondMemberEnd = new MemberEnd((((AssociationClassRelationship) relationship).getMembersEnd()
                                .get(1).getAggregation()), secondMultiplicity, (((AssociationClassRelationship) relationship)
                                .getMembersEnd()).get(1).getVisibility(), target.findElementById(((AssociationClassRelationship) relationship)
                                .getMembersEnd().get(1).getType().getId()));
                        associationClassRelationship.setMembersEnd(firstMemberEnd);
                        associationClassRelationship.setMembersEnd(secondMemberEnd);
                        target.getRelationshipHolder().addRelationship(associationClassRelationship);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Set<Relationship> getAllRelationships() {
        return Collections.unmodifiableSet(getRelationships());
    }

    public List<GeneralizationRelationship> getAllGeneralizations() {
        Predicate<Relationship> isValid = parent -> parent instanceof GeneralizationRelationship;
        return UtilResources.filter(getRelationships(), isValid);
    }

    public List<AssociationRelationship> getAllAssociationsRelationships() {
        final List<AssociationRelationship> associations = getAllAssociations();
        final List<AssociationRelationship> association = new ArrayList<AssociationRelationship>();
        for (AssociationRelationship associationRelationship : associations) {
            if ((notComposition(associationRelationship)) && (notAggregation(associationRelationship))) {
                association.add(associationRelationship);
            }
        }
        return association;
    }

    private boolean notAggregation(AssociationRelationship associationRelationship) {
        return ((!associationRelationship.getParticipants().get(0).isAggregation()) && (!associationRelationship.getParticipants().get(1).isAggregation()));
    }

    private boolean notComposition(AssociationRelationship associationRelationship) {
        return ((!associationRelationship.getParticipants().get(0).isComposite()) && (!associationRelationship.getParticipants().get(1).isComposite()));
    }

    public List<AssociationRelationship> getAllAssociations() {
        Predicate<Relationship> isValid = parent -> parent instanceof AssociationRelationship;
        return UtilResources.filter(getRelationships(), isValid);
    }

    public List<AssociationRelationship> getAllCompositions() {
        final List<AssociationRelationship> associations = getAllAssociations();
        final List<AssociationRelationship> compositions = new ArrayList<>();
        for (AssociationRelationship associationRelationship : associations) {
            if ((associationRelationship.getParticipants().get(0).isComposite()) || (associationRelationship.getParticipants().size() > 1
                    && associationRelationship.getParticipants().get(1).isComposite())) {
                compositions.add(associationRelationship);
            }
        }
        return compositions;
    }

    public List<AssociationRelationship> getAllAggregations() {
        final List<AssociationRelationship> associations = getAllAssociations();
        final List<AssociationRelationship> aggregations = new ArrayList<>();
        for (AssociationRelationship associationRelationship : associations) {
            if ((associationRelationship.getParticipants().get(0).isAggregation()) || (associationRelationship.getParticipants().size() > 1
                    && associationRelationship.getParticipants().get(1).isAggregation())) {
                aggregations.add(associationRelationship);
            }
        }
        return aggregations;
    }

    public List<UsageRelationship> getAllUsage() {
        Predicate<Relationship> isValid = parent -> parent instanceof UsageRelationship;
        return UtilResources.filter(getRelationships(), isValid);
    }

    public List<DependencyRelationship> getAllDependencies() {
        Predicate<Relationship> isValid = parent -> parent instanceof DependencyRelationship;
        return UtilResources.filter(getRelationships(), isValid);
    }

    public List<RealizationRelationship> getAllRealizations() {
        Predicate<Relationship> realizations = parent -> parent instanceof RealizationRelationship;
        return UtilResources.filter(getRelationships(), realizations);
    }

    public List<AbstractionRelationship> getAllAbstractions() {
        Predicate<Relationship> realizations = parent -> parent instanceof AbstractionRelationship;
        return UtilResources.filter(getRelationships(), realizations);
    }


    public List<AssociationClassRelationship> getAllAssociationsClass() {
        Predicate<Relationship> associationClasses = parent -> parent instanceof AssociationClassRelationship;
        return UtilResources.filter(getRelationships(), associationClasses);
    }

    public boolean haveRelationship(Relationship relationship) {
        for (Relationship relationshipFromAllRelationships : getAllRelationships()) {
            if ((relationshipFromAllRelationships instanceof AssociationRelationship) && (relationship instanceof AssociationRelationship)) {
                final List<AssociationEnd> participantsNew = ((AssociationRelationship) relationship).getParticipants();
                final List<AssociationEnd> participantsExists = ((AssociationRelationship) relationshipFromAllRelationships).getParticipants();
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
