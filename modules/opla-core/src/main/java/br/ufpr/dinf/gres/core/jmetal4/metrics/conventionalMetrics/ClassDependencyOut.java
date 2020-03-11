package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.relationship.DependencyRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.RealizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.UsageRelationship;

import java.util.ArrayList;
import java.util.List;

public class ClassDependencyOut {

    private int results;

    public ClassDependencyOut(Architecture architecture) {
        this.results = 0;
        int depOut = 0;

        for (Package component : architecture.getAllPackages()) {
            for (br.ufpr.dinf.gres.architecture.representation.Class cls : component.getAllClasses()) {
                depOut += searchClassDependencies(cls, component);
            }
            this.results += depOut;
            depOut = 0;
        }
    }

    private int searchClassDependencies(Class source, Package comp) {
        List<Class> depClasses = new ArrayList<>();

        for (Class c : comp.getAllClasses()) {
            List<Relationship> relationships = new ArrayList<>(source.getRelationships());
            for (Relationship relationship : relationships) {

                if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getClient().equals(source) && (!(depClasses.contains(c)))) {
                        depClasses.add(c);
                    }
                }
                if (relationship instanceof RealizationRelationship) {
                    RealizationRelationship dependency = (RealizationRelationship) relationship;
                    if (dependency.getClient().equals(source) && (!(depClasses.contains(c)))) {
                        depClasses.add(c);
                    }
                }
                if (relationship instanceof UsageRelationship) {
                    UsageRelationship dependency = (UsageRelationship) relationship;
                    if (dependency.getClient().equals(source) && (!(depClasses.contains(c)))) {
                        depClasses.add(c);
                    }
                }
            }
        }

        return depClasses.size();
    }

    public int getResults() {
        return results;
    }
}