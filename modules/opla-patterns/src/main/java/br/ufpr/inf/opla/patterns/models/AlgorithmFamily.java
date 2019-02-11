package br.ufpr.inf.opla.patterns.models;

import arquitetura.representation.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AlgorithmFamily implements Comparable<AlgorithmFamily> {

    public static final String SUFFIX = "suffix";
    public static final String PREFIX = "prefix";
    public static final String METHOD = "method";

    private final List<Element> participants;
    private String name;
    private String type;

    public AlgorithmFamily(String name, String type) {
        this.participants = new ArrayList<>();
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCapitalized() {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public List<Element> getParticipants() {
        return participants;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.name);
        hash = 29 * hash + Objects.hashCode(this.type);
        return hash;
    }

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

    @Override
    public String toString() {
        return "{" + "name=" + name + ", type=" + type + '}';
    }

}
