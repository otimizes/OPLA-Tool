package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.*;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Measures the number of UML dependencies where the package is the provider (Wüst, 2014)
 */
public class DependencyIn extends ObjectiveFunctionImplementation {

    public DependencyIn(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        int depIn = 0;

        for (Package component : architecture.getAllPackages()) {
            List<Relationship> relationships = new ArrayList<Relationship>(architecture.getRelationshipHolder().getAllRelationships());

            for (Relationship relationship : relationships) {
                if (relationship instanceof AbstractionRelationship) {
                    AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
                    if (abstraction.getSupplier().getNamespace().contains(component.getName())) depIn++;

                } else if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getSupplier().getNamespace().contains(component.getName())) depIn++;

                } else if (relationship instanceof UsageRelationship) {
                    UsageRelationship usage = (UsageRelationship) relationship;
                    if (usage.getSupplier().getNamespace().contains(component.getName())) depIn++;

                } else if (relationship instanceof RealizationRelationship) {
                    RealizationRelationship realization = (RealizationRelationship) relationship;
                    if (realization.getSupplier().getNamespace().contains(component.getName())) depIn++;

                }
            }


            this.addToResults(depIn);
            depIn = 0;
        }
    }
}