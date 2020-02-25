package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.*;

import java.util.ArrayList;
import java.util.List;

public class DependencyOut {

    private int results;

    public DependencyOut(Architecture architecture) {

        this.results = 0;
        int depOut = 0;

        for (Package component : architecture.getAllPackages()) {
            List<Relationship> relationships = new ArrayList<Relationship>(architecture.getRelationshipHolder().getAllRelationships());

            for (Relationship relationship : relationships) {
                if (relationship instanceof AbstractionRelationship) {
                    AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
                    if (abstraction.getClient().getNamespace().contains(component.getName())) depOut++;

                }else if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient().getNamespace().contains(component.getName())) depOut++;

                }else if (relationship instanceof UsageRelationship) {
                    UsageRelationship usage = (UsageRelationship) relationship;
                    if (usage.getClient().getNamespace().contains(component.getName())) depOut++;

                }else if (relationship instanceof RealizationRelationship) {
                    RealizationRelationship realization = (RealizationRelationship) relationship;
                    if (realization.getClient().getNamespace().contains(component.getName())) depOut++;

                }
            }

            this.results += depOut; // somatorio de DepOut da arquitetura como um todo
            depOut= 0;
        }
    }

    public int getResults() {
        return results;
    }

}
