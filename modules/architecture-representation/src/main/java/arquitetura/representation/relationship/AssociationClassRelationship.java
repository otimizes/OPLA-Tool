package arquitetura.representation.relationship;

import arquitetura.helpers.ElementsTypes;
import arquitetura.representation.*;
import arquitetura.representation.Class;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AssociationClassRelationship extends Relationship {

    public List<MemberEnd> memebersEnd = new ArrayList<MemberEnd>();
    private Element ownedEnd;
    private Class associationClass;
    private String idOwner;

    /**
     * @param architecture
     * @param name
     * @param ends
     * @param ownedEnd
     * @param id               - associationEnd
     * @param idOwner          - ex: pacote
     * @param associationClass
     */
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
        if (((AssociationClassRelationship) obj).getMemebersEnd().get(0).getType().getName().equals(this.getMemebersEnd().get(0).getType().getName())
                && ((AssociationClassRelationship) obj).getMemebersEnd().get(1).getType().getName().equals(this.getMemebersEnd().get(1).getType().getName()))
            return true;
        return false;
    }


}