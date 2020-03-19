package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.relationship.DependencyRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.RealizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.UsageRelationship;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionBase;

import java.util.ArrayList;
import java.util.List;

public class ClassDependencyIn extends ObjectiveFunctionBase {

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
