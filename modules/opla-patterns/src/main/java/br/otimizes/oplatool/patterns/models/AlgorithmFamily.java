package br.otimizes.oplatool.patterns.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.otimizes.oplatool.architecture.representation.Element;

/**
 * The Class AlgorithmFamily.
 */
public class AlgorithmFamily implements Comparable<AlgorithmFamily> {

    /** The Constant SUFFIX. */
    public static final String SUFFIX = "suffix";
    
    /** The Constant PREFIX. */
    public static final String PREFIX = "prefix";
    
    /** The Constant METHOD. */
    public static final String METHOD = "method";

    /** The participants. */
    private final List<Element> participants;
    
    /** The name. */
    private String name;
    
    /** The type. */
    private String type;

    /**
     * Instantiates a new algorithm family.
     *
     * @param name the name
     * @param type the type
     */
    public AlgorithmFamily(String name, String type) {
        this.participants = new ArrayList<>();
        this.name = name;
        this.type = type;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name capitalized.
     *
     * @return the name capitalized
     */
    public String getNameCapitalized() {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Gets the participants.
     *
     * @return the list of participants
     */
    public List<Element> getParticipants() {
        return participants;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Hash code.
     *
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.type);
        return hash;
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AlgorithmFamily other = (AlgorithmFamily) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.type, other.type);
    }

    /**
     * Compare to.
     *
     * @param o the o
     * @return the value 0 if this == o; a value less than 0 if this < o; and a value greater than 0 if this > o
     */
    @Override	
    public int compareTo(AlgorithmFamily o) {
        int compare = Integer.compare(this.getParticipants().size(), o.getParticipants().size());
        if (compare == 0) {
            compare = Integer.compare(this.getName().length(), o.getName().length());
            if (compare == 0) {
                compare = this.getName().compareToIgnoreCase(o.getName());
            }
        }
        return compare;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "{" + "name=" + name + ", type=" + type + '}';
    }

}
