package br.otimizes.oplatool.architecture.representation;


/**
 * Concern representation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class Concern {

    private String id;
    private Boolean isPrimitive = false;
    private String name;

    public Concern(String name) {
        this.name = name;
    }

    public Concern() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPrimitive() {
        return isPrimitive;
    }

    public void setPrimitive(Boolean primitive) {
        isPrimitive = primitive;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void updateConcernsList(String newName) {
        this.name += "," + newName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Concern other = (Concern) obj;
        if (name == null) {
            return other.name == null;
        } else return name.equals(other.name);
    }
}