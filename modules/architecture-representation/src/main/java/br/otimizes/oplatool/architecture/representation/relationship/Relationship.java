package br.otimizes.oplatool.architecture.representation.relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Relationship class
 *
 * @author edipofederle<edipofederle @ gmail.com> <br /><br/>
 * <p>
 * <p>
 * Implementad due to {@link DependencyRelationship}.
 */
public abstract class Relationship {

    private String id;
    private String type;
    private String name;

    private List<String> stereotypes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStereotypes() {
        return Collections.unmodifiableList(this.stereotypes);
    }

    public void setStereotypes(List<String> stereotypes) {
        this.stereotypes = stereotypes;
    }

    public void removeStereotype(String stereotypeName) {
        this.stereotypes.remove(stereotypeName);
    }

    public void setStereotype(String name) {
        this.stereotypes.add(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Relationship other = (Relationship) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}