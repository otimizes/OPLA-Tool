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

public class ClassDependencyIn {

    private int results;

    public ClassDependencyIn(Architecture architecture) {

        /**
         * @param args
         */
        this.results = 0;
        int depIn = 0;

        for (Package component : architecture.getAllPackages()) {
            for (Class cls : component.getAllClasses()) {
                depIn += searchClassDependencies(cls, component);
                // System.out.println("DepIn- Classe: "+ cls.getName() + " :" +
                // depIn);
            }

            this.results += depIn; // somatorio de DepIn da br.ufpr.dinf.gres.arquitetura como um todo
            depIn = 0;
        }

    }

    // ----------------------------------------------------------------------------------

    private int searchClassDependencies(Class source, Package comp) {
        List<Class> depClasses = new ArrayList<Class>();

        for (Class c : comp.getAllClasses()) {
            List<Relationship> relationships = new ArrayList<Relationship>(source.getRelationships());
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
        }// end for classes

        return depClasses.size();
    }

    // ---------------------------------------------------------------------------------

    public int getResults() {
        return results;
    }

}
