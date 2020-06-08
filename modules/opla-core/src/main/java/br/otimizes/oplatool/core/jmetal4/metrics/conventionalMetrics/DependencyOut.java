package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Measures the number of UML dependencies where the package is the client (Wüst, 2014)
 */
public class DependencyOut extends ObjectiveFunctionImplementation {

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
