package br.otimizes.oplatool.architecture.representation.relationship;

import br.otimizes.oplatool.architecture.helpers.ElementsTypes;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Association relationship class
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationClassRelationship extends Relationship {

    public List<MemberEnd> membersEnd;
    private final Element ownedEnd;
    private final Class associationClass;
    private final String idOwner;

    public AssociationClassRelationship(String name, List<MemberEnd> ends, Element ownedEnd, String id, String idOwner, Class associationClass) {
        super.setName(name);
        this.membersEnd = ends;
        this.ownedEnd = ownedEnd;
        this.idOwner = idOwner;
        this.associationClass = associationClass;
        super.setId(id);
        super.setType(ElementsTypes.ASSOCIATIONCLASS);
    }

    public Set<Attribute> getAllAttributes() {
        return this.associationClass.getAllAttributes();
    }

    public List<MemberEnd> getMembersEnd() {
        return membersEnd;
    }

    public void setMembersEnd(MemberEnd me) {
        membersEnd.add(me);
    }

    public Element getOwnedEnd() {
        return ownedEnd;
    }

    public String getPackageOwner() {
        return this.idOwner;
    }

    public Set<Method> getAllMethods() {
        return this.associationClass.getAllMethods();
    }

    public Set<Concern> getOwnConcerns() {
        return this.associationClass.getOwnConcerns();
    }

    public Collection<Concern> getAllConcerns() {
        Collection<Concern> concerns = new ArrayList<Concern>(getOwnConcerns());

        for (Method method : this.associationClass.getAllMethods())
            concerns.addAll(method.getAllConcerns());
        for (Attribute attribute : associationClass.getAllAttributes())
            concerns.addAll(attribute.getAllConcerns());

        return concerns;
    }

    public Class getAssociationClass() {
        return associationClass;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((associationClass == null) ? 0 : associationClass.hashCode());
        result = prime * result
                + ((membersEnd == null) ? 0 : membersEnd.hashCode());
        result = prime * result
                + ((ownedEnd == null) ? 0 : ownedEnd.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return ((AssociationClassRelationship) obj).getMembersEnd().get(0)
                .getType().getName().equals(this.getMembersEnd().get(0).getType().getName())
                && ((AssociationClassRelationship) obj).getMembersEnd().get(1)
                .getType().getName().equals(this.getMembersEnd().get(1).getType().getName());
    }
}