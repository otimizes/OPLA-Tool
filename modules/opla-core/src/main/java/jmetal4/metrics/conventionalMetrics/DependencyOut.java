package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.DependencyRelationship;

import java.util.List;

public class DependencyOut {

    private int results;

    public DependencyOut(Architecture architecture) {

        this.results = 0;
        int depOut = 0;

        for (Package component : architecture.getAllPackages()) {
            List<DependencyRelationship> relationships = architecture.getRelationshipHolder().getAllDependencies();

            for (DependencyRelationship dependency : relationships)
                if (dependency.getClient().equals(component)) depOut++;

            this.results += depOut; // somatorio de DepOut da arquitetura como um todo
            depOut = 0;
        }
    }

    public int getResults() {
        return results;
    }

}
