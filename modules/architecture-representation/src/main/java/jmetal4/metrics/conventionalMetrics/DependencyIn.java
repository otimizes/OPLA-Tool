package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AbstractionRelationship;

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

        for (Package component : this.architecture.getAllPackages()) {
            List<AbstractionRelationship> relationships = architecture.getRelationshipHolder().getAllAbstractions();

            for (AbstractionRelationship abstraction : relationships)
                if (abstraction.getSupplier().equals(component)) depIn++;

            this.results += depIn; // somatorio de DepIn da arquitetura como um todo
            depIn = 0;
        }
    }

    public int getResults() {
        return results;
    }

}
