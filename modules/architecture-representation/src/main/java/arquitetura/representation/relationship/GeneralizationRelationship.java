package arquitetura.representation.relationship;

import arquitetura.helpers.ElementsTypes;
import arquitetura.helpers.UtilResources;
import arquitetura.representation.Class;
import arquitetura.representation.Element;
import arquitetura.representation.RelationshipsHolder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class GeneralizationRelationship extends Relationship {

    private Element parent;
    private Element child;
    private RelationshipsHolder relationshipHolder;

    public GeneralizationRelationship(Element parentClass, Element childClass, RelationshipsHolder relationshipHolder, String id) {
        setParent(parentClass);
        setChild(childClass);
        this.relationshipHolder = relationshipHolder;
        setId(id);
        super.setType(ElementsTypes.GENERALIZATION);
    }

    public GeneralizationRelationship(Element parentClass, Element childClass, RelationshipsHolder relationshipHolder) {
        setParent(parentClass);
        setChild(childClass);
        this.relationshipHolder = relationshipHolder;
        setId(UtilResources.getRandonUUID());
        super.setType(ElementsTypes.GENERALIZATION);
    }

    /**
     * @return the child
     */
    public Element getChild() {
        return child;
    }

    /**
     * @param child the child to set
     */
    public void setChild(Element child) {
        this.child = child;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    /**
     * Método que retorna todas as classes filhas para a parent class (general)
     *
     * @return
     */
    public Set<Element> getAllChildrenForGeneralClass() {
        Set<Element> childreen = new HashSet<Element>();
        for (GeneralizationRelationship generalization : this.relationshipHolder.getAllGeneralizations())
            if (generalization.getParent().getName().equalsIgnoreCase(this.parent.getName()))
                childreen.add(generalization.getChild());

        return Collections.unmodifiableSet(childreen);

    }

    public void replaceChild(Class newChild) {
        this.child = newChild;
    }

    public void replaceParent(Class parent) {
        setParent(parent);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((child == null) ? 0 : child.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
        final GeneralizationRelationship other = (GeneralizationRelationship) obj;
        if (this.parent != other.parent && (this.parent == null || !this.parent.equals(other.parent))) {
            return false;
        }
        return this.child == other.child || (this.child != null && this.child.equals(other.child));
    }

}