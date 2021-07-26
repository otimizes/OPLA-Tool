package br.otimizes.oplatool.architecture.representation.relationship;

import br.otimizes.oplatool.architecture.helpers.ElementsTypes;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.RelationshipsHolder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Generalization relationship class
 *
 * @author edipofederle<edipofederle @ gmail.com>
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
        setId(UtilResources.getRandomUUID());
        super.setType(ElementsTypes.GENERALIZATION);
    }

    public GeneralizationRelationship() {
    }

    public Element getChild() {
        return child;
    }

    public void setChild(Element child) {
        this.child = child;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public Set<Element> getAllChildrenForGeneralClass() {
        Set<Element> children = new HashSet<Element>();
        for (GeneralizationRelationship generalization : this.relationshipHolder.getAllGeneralizations())
            if (generalization.getParent().getName().equalsIgnoreCase(this.parent.getName()))
                children.add(generalization.getChild());

        return Collections.unmodifiableSet(children);

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
        if (!Objects.equals(this.parent, other.parent)) {
            return false;
        }
        return Objects.equals(this.child, other.child);
    }
}