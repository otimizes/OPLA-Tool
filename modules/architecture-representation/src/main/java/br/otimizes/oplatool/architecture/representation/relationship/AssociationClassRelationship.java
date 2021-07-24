package br.otimizes.oplatool.architecture.representation.relationship;

import br.otimizes.oplatool.architecture.helpers.ElementsTypes;
import br.otimizes.oplatool.architecture.representation.*;
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

    public List<MemberEnd> memebersEnd = new ArrayList<MemberEnd>();
    private final Element ownedEnd;
    private final Class associationClass;
    private final String idOwner;

    public AssociationClassRelationship(String name, List<MemberEnd> ends, Element ownedEnd, String id, String idOwner, Class associationClass) {
        super.setName(name);
        this.memebersEnd = ends;
        this.ownedEnd = ownedEnd;
        this.idOwner = idOwner;
        this.associationClass = associationClass;
        super.setId(id);
        super.setType(ElementsTypes.ASSOCIATIONCLASS);
    }


    /**
     * @return the attributes
     */
    public Set<Attribute> getAllAttributes() {
        return this.associationClass.getAllAttributes();
    }


    public List<MemberEnd> getMemebersEnd() {
        return memebersEnd;
    }

    public void SetMemebersEnd(MemberEnd me){
        memebersEnd.add(me);
    }

    /**
     * Retorna  {@link Element } dona da AssociationClass.
     *
     * @return
     */
    public Element getOwnedEnd() {
        return ownedEnd;
    }

    /**
     * Retorna o ID do pacote que a associationClass pertence.
     *
     * @return String
     */
    public String getPackageOwner() {
        return this.idOwner;
    }

    /**
     * Retorna os métodos para associationClass
     *
     * @return {@link Method}
     */
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

    /**
     * @return the associationClass
     */
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
                + ((memebersEnd == null) ? 0 : memebersEnd.hashCode());
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
        return ((AssociationClassRelationship) obj).getMemebersEnd().get(0).getType().getName().equals(this.getMemebersEnd().get(0).getType().getName())
                && ((AssociationClassRelationship) obj).getMemebersEnd().get(1).getType().getName().equals(this.getMemebersEnd().get(1).getType().getName());
    }


}