package arquitetura.representation;

import arquitetura.helpers.UtilResources;
import arquitetura.representation.relationship.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe usada para realizar operações sobre Associations. Seu uso é feito da seguinte forma:<br/><br/>
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
 * @author edipofederle<edipofederle@gmail.com>
 */
public class OperationsOverAssociation {

    private AssociationEnd associationEnd1;
    private AssociationRelationship association;
    private RelationshipsHolder relationshipHolder;

    public OperationsOverAssociation(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
        String id = UtilResources.getRandonUUID();
        association = new AssociationRelationship(id);
    }

    public OperationsOverAssociation createAssociationEnd() {
        associationEnd1 = new AssociationEnd();
        return this;
    }

    public OperationsOverAssociation withName(String name) {
        this.associationEnd1.setName(name);
        return this;
    }

    public OperationsOverAssociation withKlass(Element idclass1) {
        associationEnd1.setCLSClass(idclass1);
        return this;
    }

    public OperationsOverAssociation withMultiplicity(String multiplicity) {
        if (!multiplicity.equals("1"))
            this.associationEnd1.setMultiplicity(new Multiplicity(multiplicity.split("\\..")[0], multiplicity.split("\\..")[1]));
        else
            this.associationEnd1.setMultiplicity(new Multiplicity(multiplicity, multiplicity));
        return this;
    }

    public OperationsOverAssociation navigable(boolean navigable) {
        this.associationEnd1.setNavigable(navigable);
        return this;
    }

    public void build() {
        this.association.getParticipants().add(associationEnd1);
        this.relationshipHolder.addRelationship(this.association);
    }

    public OperationsOverAssociation and() {
        this.association.getParticipants().add(associationEnd1);
        this.relationshipHolder.removeRelationship(association);
        return this;
    }

    public OperationsOverAssociation asComposition() {
        this.associationEnd1.setAggregation("composite");
        return this;
    }

    public OperationsOverAssociation asAggregation() {
        this.associationEnd1.setAggregation("shared");
        return this;
    }

    /**
     * associationClass null se você deseja que seja criada a classe associativa.
     *
     * @param listAttrs
     * @param listMethods
     * @param owner
     * @param klass
     * @param associationClass
     */
    public void createAssociationClass(Set<Attribute> listAttrs, Set<Method> listMethods, Element owner, Element klass, String associationClassName) {
        String namespace = UtilResources.createNamespace(ArchitectureHolder.getName(), "AssociationClass");
        Class asClass = new Class(relationshipHolder, associationClassName, null, false, namespace, UtilResources.getRandonUUID());

        for (Attribute a : listAttrs)
            asClass.addExternalAttribute(a);

        for (Method m : listMethods)
            asClass.addExternalMethod(m);

        List<MemberEnd> ends = new ArrayList<MemberEnd>();
        ends.add(new MemberEnd("none", null, "public", owner));
        ends.add(new MemberEnd("none", null, "public", klass));


        AssociationClassRelationship asc = new AssociationClassRelationship("",
                ends,
                owner,
                asClass.getId(),
                null,
                asClass);
        this.relationshipHolder.addRelationship(asc);
    }

    /**
     * Cria associação dado dois {@link AssociationEnd}
     *
     * @param associationEnd1
     * @param associationEnd2
     */
    public void create(AssociationEnd associationEnd1, AssociationEnd associationEnd2) {
        AssociationEnd associatioEnd1 = new AssociationEnd();
        associatioEnd1.setAggregation(associationEnd1.getAggregation());
        associatioEnd1.setNavigable(associationEnd1.isNavigable());
        associatioEnd1.setMultiplicity(associationEnd1.getMultiplicity());
        associatioEnd1.setCLSClass(associationEnd1.getCLSClass());

        AssociationEnd associatioEnd2 = new AssociationEnd();
        associatioEnd2.setAggregation(associationEnd2.getAggregation());
        associatioEnd2.setNavigable(associationEnd2.isNavigable());
        associatioEnd2.setMultiplicity(associationEnd2.getMultiplicity());
        associatioEnd2.setCLSClass(associationEnd2.getCLSClass());

        this.association.getParticipants().add(associatioEnd1);
        this.association.getParticipants().add(associatioEnd2);

        this.relationshipHolder.getRelationships().add(this.association);
    }


}
