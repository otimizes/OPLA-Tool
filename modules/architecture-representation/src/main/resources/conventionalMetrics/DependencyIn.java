package br.uem.din.metrics.conventionalMetrics;

import br.uem.din.architectureEvolution.representation.AbstractionInterElementRelationship;
import br.uem.din.architectureEvolution.representation.Architecture;
import br.uem.din.architectureEvolution.representation.Component;
import br.uem.din.architectureEvolution.representation.InterElementRelationship;

import java.util.ArrayList;
import java.util.List;

public class DependencyIn {

    /**
     * @param args
     */
    private Architecture architecture;
    private int results;

    public DependencyIn(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0;
        int depIn = 0;

        for (Component component : architecture.getComponents()) {
            List<InterElementRelationship> relationships = new ArrayList<InterElementRelationship>(architecture.getInterElementRelationships());
            for (InterElementRelationship relationship : relationships) {
                if (relationship instanceof AbstractionInterElementRelationship) {
                    AbstractionInterElementRelationship dependency = (AbstractionInterElementRelationship) relationship;
                    if (dependency.getChild().equals(component)) depIn++;
                }
            }
            this.results += depIn; // somatorio de DepIn da arquitetura como um todo
            depIn = 0;
        }
    }

    public int getResults() {
        return results;
    }

}
