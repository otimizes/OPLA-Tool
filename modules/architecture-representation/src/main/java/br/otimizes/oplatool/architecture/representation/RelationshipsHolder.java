package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.exceptions.ClassNotFound;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.architecture.helpers.Predicate;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.*;

import java.util.*;

/**
 * Relationships holder class
 */
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
                boolean remove = false;
                for (AssociationEnd a : ((AssociationRelationship) r).getParticipants()) {
                    if (a.getCLSClass().equals(element)) {
                        remove = true;
                    }
                }
                if(remove){
                    i.remove();
                }
            }

            if (r instanceof AssociationClassRelationship) {
                boolean remove = false;
                for (MemberEnd memberEnd : ((AssociationClassRelationship) r).getMemebersEnd()) {
                    if (memberEnd.getType().equals(element)) {
                        remove = true;
                    }
                }
                if(remove){
                    i.remove();
                }
            }
        }
    }

    public void forceAddAssociationRelationshipsChild(Relationship r, Architecture target) throws ClassNotFound {


        if (r instanceof AssociationRelationship) {

            AssociationRelationship re1 = (AssociationRelationship)r;
            if(re1.getParticipants().size() == 2){

                Element part1 = target.findElementById((((AssociationRelationship) r).getParticipants()).get(0).getCLSClass().getId());

                Element part2 = target.findElementById((((AssociationRelationship) r).getParticipants()).get(1).getCLSClass().getId());

                if(part1 != null && part2 != null) {

                    if ((re1.getParticipants().get(0).getMultiplicity() != null) && (re1.getParticipants().get(1).getMultiplicity() != null)) {
                        AssociationRelationship ar = new AssociationRelationship(r.getId());
                        Multiplicity mult1 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getUpperValue());
                        AssociationEnd ae1 = new AssociationEnd(part1, (((AssociationRelationship) r).getParticipants()).get(0).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(0).getAggregation(), mult1, (((AssociationRelationship) r).getParticipants()).get(0).getName());
                        Multiplicity mult2 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getUpperValue());
                        AssociationEnd ae2 = new AssociationEnd(part2, (((AssociationRelationship) r).getParticipants()).get(1).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(1).getAggregation(), mult2, (((AssociationRelationship) r).getParticipants()).get(1).getName());
                        ar.SetAssociationEnd(ae1);
                        ar.SetAssociationEnd(ae2);
                        target.getRelationshipHolder().addRelationship(ar);
                        return;
                    }
                }
                else{
                    if(part1==null){
                        Class cpai = (Class) re1.getParticipants().get(0).getCLSClass();
                        for(Relationship rpai : cpai.getRelationships()){
                            if(rpai instanceof  AssociationRelationship){
                                AssociationRelationship re2 = (AssociationRelationship)rpai;
                                if(re2.getParticipants().get(0).getCLSClass().getId().equals(part2.getId()))
                                    continue;
                                if(re2.getParticipants().get(1).getCLSClass().getId().equals(part2.getId()))
                                    continue;
                                if(re2.getParticipants().get(1).getCLSClass().getId().equals(re1.getParticipants().get(0).getCLSClass().getId()))
                                    part1 = target.findClassById(re2.getParticipants().get(0).getCLSClass().getId());
                                else{
                                    part1 = target.findClassById(re2.getParticipants().get(1).getCLSClass().getId());
                                }
                                AssociationRelationship ar = new AssociationRelationship(r.getId());
                                Multiplicity mult1;
                                mult1 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getUpperValue());
                                AssociationEnd ae1 = new AssociationEnd(part1, (((AssociationRelationship) r).getParticipants()).get(0).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(0).getAggregation(), mult1, (((AssociationRelationship) r).getParticipants()).get(0).getName());
                                Multiplicity mult2;
                                mult2 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getUpperValue());
                                AssociationEnd ae2 = new AssociationEnd(part2, (((AssociationRelationship) r).getParticipants()).get(1).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(1).getAggregation(), mult2, (((AssociationRelationship) r).getParticipants()).get(1).getName());
                                ar.SetAssociationEnd(ae1);
                                ar.SetAssociationEnd(ae2);
                                target.getRelationshipHolder().addRelationship(ar);
                            }
                        }
                    }
                    else{

                        Class cpai = (Class) re1.getParticipants().get(1).getCLSClass();
                        for(Relationship rpai : cpai.getRelationships()){
                            if(rpai instanceof  AssociationRelationship){
                                AssociationRelationship re2 = (AssociationRelationship)rpai;
                                if(re2.getParticipants().get(0).getCLSClass().getId().equals(part1.getId()))
                                    continue;
                                if(re2.getParticipants().get(1).getCLSClass().getId().equals(part1.getId()))
                                    continue;

                                if(re2.getParticipants().get(1).getCLSClass().getId().equals(re1.getParticipants().get(1).getCLSClass().getId()))
                                    part2 = target.findClassById(re2.getParticipants().get(0).getCLSClass().getId());
                                else{
                                    part2 = target.findClassById(re2.getParticipants().get(1).getCLSClass().getId());
                                }
                                AssociationRelationship ar = new AssociationRelationship(r.getId());
                                Multiplicity mult1;
                                mult1 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getUpperValue());
                                AssociationEnd ae1 = new AssociationEnd(part1, (((AssociationRelationship) r).getParticipants()).get(0).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(0).getAggregation(), mult1, (((AssociationRelationship) r).getParticipants()).get(0).getName());
                                Multiplicity mult2;
                                mult2 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getUpperValue());
                                AssociationEnd ae2 = new AssociationEnd(part2, (((AssociationRelationship) r).getParticipants()).get(1).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(1).getAggregation(), mult2, (((AssociationRelationship) r).getParticipants()).get(1).getName());
                                ar.SetAssociationEnd(ae1);
                                ar.SetAssociationEnd(ae2);
                                target.getRelationshipHolder().addRelationship(ar);
                            }
                        }
                    }
                }
            }else{
            }
        }
    }

    public void forceAddRealization(Relationship r, Architecture target){
        RealizationRelationship re1 = (RealizationRelationship)r;
        for(Relationship r_class : ((Class)re1.getClient()).getRelationships()){
            if(r_class instanceof  AssociationRelationship){
                AssociationRelationship associationRelationship = (AssociationRelationship)r_class;
                Element c_relation = associationRelationship.getParticipants().get(0).getCLSClass();
                if(c_relation.getId().equals(re1.getClient().getId())){
                    c_relation = associationRelationship.getParticipants().get(1).getCLSClass();
                }
                Element client =  target.findElementById(c_relation.getId());
                Element supplier = target.findElementById(re1.getSupplier().getId());
                if(client != null && supplier != null) {
                    RealizationRelationship newRelation = new RealizationRelationship(client, supplier, r.getName(), r.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    ((Class)client).addImplementedInterface((Interface) supplier);
                }
            }
        }
    }

    public void verifyAndAddRelationshipsChild2(Relationship r, Architecture target) {

        if (r instanceof GeneralizationRelationship) {
            if ((((GeneralizationRelationship) r).getParent() != null) && (((GeneralizationRelationship) r).getChild() != null)) {
                Element parent = target.findElementById(((GeneralizationRelationship) r).getParent().getId());
                Element child = target.findElementById(((GeneralizationRelationship) r).getChild().getId());
                if(parent != null && child != null) {
                    GeneralizationRelationship newRelation = new GeneralizationRelationship(parent, child, target.getRelationshipHolder(), r.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return;
                }
            }
        }
        if (r instanceof UsageRelationship) {

            if((((UsageRelationship) r).getClient() != null) && (((UsageRelationship) r).getSupplier() != null)) {
                Element client =  target.findElementById(((UsageRelationship) r).getClient().getId());
                Element supplier = target.findElementById(((UsageRelationship) r).getSupplier().getId());
                if(client != null && supplier != null) {
                    UsageRelationship newRelation = new UsageRelationship(r.getName(), supplier, client, r.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return;
                }
            }

        }
        if (r instanceof RealizationRelationship) {
            if((((RealizationRelationship) r).getClient() != null) && (((RealizationRelationship) r).getSupplier() != null)) {
                Element client =  target.findElementById(((RealizationRelationship) r).getClient().getId());
                Element supplier = target.findElementById(((RealizationRelationship) r).getSupplier().getId());
                if(client != null && supplier != null) {
                    RealizationRelationship newRelation = new RealizationRelationship(client, supplier, r.getName(), r.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return;
                }
            }
        }
        if (r instanceof DependencyRelationship) {
            if((((DependencyRelationship) r).getClient() != null) && (((DependencyRelationship) r).getSupplier() != null)) {
                Element supplier = target.findElementById(((DependencyRelationship) r).getSupplier().getId());
                Element client = target.findElementById(((DependencyRelationship) r).getClient().getId());
                if(supplier != null && client != null) {
                    DependencyRelationship newRelation = new DependencyRelationship(supplier, client, r.getName(), r.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return;
                }

            }
        }
        if (r instanceof AbstractionRelationship) {
            if((((AbstractionRelationship) r).getClient() != null) && (((AbstractionRelationship) r).getSupplier() != null)) {
                Element client = target.findElementById(((AbstractionRelationship) r).getClient().getId());
                Element supplier = target.findElementById(((AbstractionRelationship) r).getSupplier().getId());
                if(client != null && supplier != null) {
                    AbstractionRelationship newRelation = new AbstractionRelationship(client, supplier, r.getId());
                    target.getRelationshipHolder().addRelationship(newRelation);
                    return;
                }

            }
        }
        if (r instanceof AssociationRelationship) {

            if(((AssociationRelationship) r).getParticipants().size() == 2){

                Element part1 = target.findElementById((((AssociationRelationship) r).getParticipants()).get(0).getCLSClass().getId());

                Element part2 = target.findElementById((((AssociationRelationship) r).getParticipants()).get(1).getCLSClass().getId());
                if(part1 != null && part2 != null) {

                    if ((((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity() != null && (((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity() != null) {
                        AssociationRelationship ar = new AssociationRelationship(r.getId());
                        Multiplicity mult1 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(0).getMultiplicity().getUpperValue());
                        AssociationEnd ae1 = new AssociationEnd(part1, (((AssociationRelationship) r).getParticipants()).get(0).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(0).getAggregation(), mult1, (((AssociationRelationship) r).getParticipants()).get(0).getName());
                        Multiplicity mult2 = new Multiplicity((((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getLowerValue(), (((AssociationRelationship) r).getParticipants()).get(1).getMultiplicity().getUpperValue());
                        AssociationEnd ae2 = new AssociationEnd(part2, (((AssociationRelationship) r).getParticipants()).get(1).isNavigable(), (((AssociationRelationship) r).getParticipants()).get(1).getAggregation(), mult2, (((AssociationRelationship) r).getParticipants()).get(1).getName());
                        ar.SetAssociationEnd(ae1);
                        ar.SetAssociationEnd(ae2);
                        target.getRelationshipHolder().addRelationship(ar);
                        return;
                    }
                }


            }
        }

        if (r instanceof AssociationClassRelationship) {


            if(((AssociationClassRelationship) r).getMemebersEnd().size() == 2){

                Element part1 = target.findElementById((((AssociationClassRelationship) r).getMemebersEnd()).get(0).getType().getId());

                Element part2 = target.findElementById((((AssociationClassRelationship) r).getMemebersEnd()).get(1).getType().getId());

                if(part2 != null && part1 != null) {
                    if ((((AssociationClassRelationship) r).getMemebersEnd()).get(0).getMultiplicity() != null && (((AssociationClassRelationship) r).getMemebersEnd()).get(1).getMultiplicity() != null) {
                        AssociationClassRelationship ar = new AssociationClassRelationship(r.getName(), ((AssociationClassRelationship) r).getMemebersEnd(), ((AssociationClassRelationship) r).getOwnedEnd(), r.getId(), ((AssociationClassRelationship) r).getOwnedEnd().getId(), ((AssociationClassRelationship) r).getAssociationClass());
                        Multiplicity mult1 = new Multiplicity((((AssociationClassRelationship) r).getMemebersEnd()).get(0).getMultiplicity().getLowerValue(), (((AssociationClassRelationship) r).getMemebersEnd()).get(0).getMultiplicity().getUpperValue());
                        MemberEnd me1 = new MemberEnd((((AssociationClassRelationship) r).getMemebersEnd().get(0).getAggregation()), mult1, (((AssociationClassRelationship) r).getMemebersEnd()).get(0).getVisibility(), target.findElementById(((AssociationClassRelationship) r).getMemebersEnd().get(0).getType().getId()));
                        Multiplicity mult2 = new Multiplicity((((AssociationClassRelationship) r).getMemebersEnd()).get(1).getMultiplicity().getLowerValue(), (((AssociationClassRelationship) r).getMemebersEnd()).get(1).getMultiplicity().getUpperValue());
                        MemberEnd me2 = new MemberEnd((((AssociationClassRelationship) r).getMemebersEnd().get(1).getAggregation()), mult2, (((AssociationClassRelationship) r).getMemebersEnd()).get(1).getVisibility(), target.findElementById(((AssociationClassRelationship) r).getMemebersEnd().get(1).getType().getId()));
                        ar.SetMemebersEnd(me1);
                        ar.SetMemebersEnd(me2);
                        target.getRelationshipHolder().addRelationship(ar);
                        return;
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
