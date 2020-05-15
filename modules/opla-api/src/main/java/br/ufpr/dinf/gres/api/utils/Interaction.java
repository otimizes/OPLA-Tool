package br.ufpr.dinf.gres.api.utils;

import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class Interaction {
    public boolean updated = false;
    @JsonIgnoreProperties("architecturalSolutionsEvaluated")
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
}
