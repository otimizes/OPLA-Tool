package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.representation.relationship.UsageRelationship;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;
import java.util.List;

/**
 * Measures the number of UML dependencies where the package is the provider (Wüst, 2014)
 */
public class ClassDependencyIn extends ObjectiveFunctionImplementation {

    public ClassDependencyIn(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        int depIn = 0;

        for (Package component : architecture.getAllPackages()) {
            for (Class cls : component.getAllClasses()) {
                depIn += searchClassDependencies(cls, component);
            }

            this.addToResults(depIn);
            depIn = 0;
        }

    }

    private int searchClassDependencies(Class source, Package comp) {
        List<Class> depClasses = new ArrayList<>();

        for (Class c : comp.getAllClasses()) {
            List<Relationship> relationships = new ArrayList<>(source.getRelationships());
            for (Relationship relationship : relationships) {

                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getSupplier().equals(source) && (!(depClasses.contains(c)))) {
                        depClasses.add(c);
                    }
                }
                if (relationship instanceof RealizationRelationship) {
                    RealizationRelationship dependency = (RealizationRelationship) relationship;
                    if (dependency.getSupplier().equals(source) && (!(depClasses.contains(c)))) {
                        depClasses.add(c);
                    }
                }
                if (relationship instanceof UsageRelationship) {
                    UsageRelationship dependency = (UsageRelationship) relationship;
                    if (dependency.getSupplier().equals(source) && (!(depClasses.contains(c)))) {
                        depClasses.add(c);
                    }
                }
            }
        }

        return depClasses.size();
    }

}
