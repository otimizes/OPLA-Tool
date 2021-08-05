package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Class used to perform operations on Associations. Its use is made as follows:<br/><br/>
 * <p>
 * {@code  a.forAssociation().createAssociationEnd() }<br/>
 * {@code	.withKlass(class1)}<br/>
 * {@code	.withMultiplicity("1..*")}<br/>
 * {@code	.navigable()}<br/>
 * {@code	.and()}<br/>
 * {@code	.createAssociationEnd()}<br/>
 * {@code              .withKlass(class2)}<br/>
 * {@code      .withMultiplicity("1..1)}<br/>
 * {@code	.navigable().build();}<br/>
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class OperationsOverAssociation {

    private AssociationEnd associationEnd;
    private final AssociationRelationship association;
    private final RelationshipsHolder relationshipHolder;

    public OperationsOverAssociation(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
        String id = UtilResources.getRandomUUID();
        association = new AssociationRelationship(id);
    }

    public OperationsOverAssociation createAssociationEnd() {
        associationEnd = new AssociationEnd();
        return this;
    }

    public OperationsOverAssociation withName(String name) {
        this.associationEnd.setName(name);
        return this;
    }

    public OperationsOverAssociation withKlass(Element classId) {
        associationEnd.setCLSClass(classId);
        return this;
    }

    public OperationsOverAssociation withMultiplicity(String multiplicity) {
        if (!multiplicity.equals("1"))
            this.associationEnd.setMultiplicity(new Multiplicity(multiplicity.split("\\..")[0],
                    multiplicity.split("\\..")[1]));
        else
            this.associationEnd.setMultiplicity(new Multiplicity(multiplicity, multiplicity));
        return this;
    }

    public OperationsOverAssociation navigable(boolean navigable) {
        this.associationEnd.setNavigable(navigable);
        return this;
    }

    public void build() {
        this.association.getParticipants().add(associationEnd);
        this.relationshipHolder.addRelationship(this.association);
    }

    public OperationsOverAssociation and() {
        this.association.getParticipants().add(associationEnd);
        this.relationshipHolder.removeRelationship(association);
        return this;
    }

    public OperationsOverAssociation asComposition() {
        this.associationEnd.setAggregation("composite");
        return this;
    }

    public OperationsOverAssociation asAggregation() {
        this.associationEnd.setAggregation("shared");
        return this;
    }

    public void createAssociationClass(Set<Attribute> attributes, Set<Method> methods, Element owner,
                                       Element element, String associationClassName) {
        String namespace = UtilResources.createNamespace(ArchitectureHolder.getName(), "AssociationClass");
        Class asClass = new Class(relationshipHolder, associationClassName, null, false,
                namespace, UtilResources.getRandomUUID());

        for (Attribute attribute : attributes)
            asClass.addExternalAttribute(attribute);

        for (Method method : methods)
            asClass.addExternalMethod(method);

        List<MemberEnd> memberEnds = new ArrayList<MemberEnd>();
        memberEnds.add(new MemberEnd("none", null, "public", owner));
        memberEnds.add(new MemberEnd("none", null, "public", element));

        AssociationClassRelationship asc = new AssociationClassRelationship("",
                memberEnds,
                owner,
                asClass.getId(),
                null,
                asClass);
        this.relationshipHolder.addRelationship(asc);
    }

    public void create(AssociationEnd associationEnd1, AssociationEnd associationEnd2) {
        AssociationEnd firstAssociationEnd = new AssociationEnd();
        firstAssociationEnd.setAggregation(associationEnd1.getAggregation());
        firstAssociationEnd.setNavigable(associationEnd1.isNavigable());
        firstAssociationEnd.setMultiplicity(associationEnd1.getMultiplicity());
        firstAssociationEnd.setCLSClass(associationEnd1.getCLSClass());

        AssociationEnd secondAssociationEnd = new AssociationEnd();
        secondAssociationEnd.setAggregation(associationEnd2.getAggregation());
        secondAssociationEnd.setNavigable(associationEnd2.isNavigable());
        secondAssociationEnd.setMultiplicity(associationEnd2.getMultiplicity());
        secondAssociationEnd.setCLSClass(associationEnd2.getCLSClass());

        this.association.getParticipants().add(firstAssociationEnd);
        this.association.getParticipants().add(secondAssociationEnd);
        this.relationshipHolder.getRelationships().add(this.association);
    }
}
