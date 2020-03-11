package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.relationship.*;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

import java.util.ArrayList;
import java.util.List;

public class DependencyOut extends BaseMetricResults {

    public DependencyOut(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        int depOut = 0;

        for (Package component : architecture.getAllPackages()) {
            List<Relationship> relationships = new ArrayList<Relationship>(architecture.getRelationshipHolder().getAllRelationships());

            for (Relationship relationship : relationships) {
                if (relationship instanceof AbstractionRelationship) {
                    AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
                    if (abstraction.getClient().getNamespace().contains(component.getName())) depOut++;

                } else if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient().getNamespace().contains(component.getName())) depOut++;

                } else if (relationship instanceof UsageRelationship) {
                    UsageRelationship usage = (UsageRelationship) relationship;
                    if (usage.getClient().getNamespace().contains(component.getName())) depOut++;

                } else if (relationship instanceof RealizationRelationship) {
                    RealizationRelationship realization = (RealizationRelationship) relationship;
                    if (realization.getClient().getNamespace().contains(component.getName())) depOut++;

                }
            }

            this.addToResults(depOut);
            depOut = 0;
        }
    }
}
