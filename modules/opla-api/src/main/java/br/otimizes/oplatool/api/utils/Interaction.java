package br.otimizes.oplatool.api.utils;

import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;

import java.io.Serializable;
import java.util.Objects;

public class Interaction implements Serializable {
    public boolean updated = false;
    public OPLASolutionSet solutionSet;

    public Interaction(boolean updated, SolutionSet solutionSet) {
        setSolutionSet(solutionSet);
        this.updated = updated;
    }

    public Interaction(SolutionSet solutionSet) {
        setSolutionSet(solutionSet);
    }

    public Interaction(boolean updated) {
        this.updated = updated;
    }

    public Interaction() {
    }

    public void setSolutionSet(SolutionSet solutionSet) {
        this.solutionSet = new OPLASolutionSet(solutionSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interaction that = (Interaction) o;
        return updated == that.updated && Objects.equals(solutionSet, that.solutionSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updated, solutionSet);
    }
}
