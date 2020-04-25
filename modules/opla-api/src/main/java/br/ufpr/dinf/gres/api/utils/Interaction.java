package br.ufpr.dinf.gres.api.utils;

import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;

public class Interaction {
    public boolean updated = false;
    public SolutionSet solutionSet;

    public Interaction(boolean updated, SolutionSet solutionSet) {
        this.updated = updated;
        this.solutionSet = solutionSet;
    }

    public Interaction(SolutionSet solutionSet) {
        this.solutionSet = solutionSet;
    }

    public Interaction(boolean updated) {
        this.updated = updated;
    }

    public Interaction() {
    }
}
