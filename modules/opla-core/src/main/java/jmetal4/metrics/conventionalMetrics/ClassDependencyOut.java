package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.Relationship;

import java.util.ArrayList;
import java.util.List;

public class ClassDependencyOut {

    /**
     * @param args
     */

    private Architecture architecture;
    private int results;

    public ClassDependencyOut(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0;
        int depOut = 0;

        for (Package component : this.architecture.getAllPackages()) {

            for (arquitetura.representation.Class cls : component.getAllClasses()) {
                depOut += searchClassDependencies(cls, component);
                // System.out.println("DepOut- Classe: "+ cls.getName() + " :" +
                // depOut);
            }

            this.results += depOut; // somatorio de DepOut da arquitetura como
            // um todo
            depOut = 0;
        }

    }

    // ----------------------------------------------------------------------------------

    private int searchClassDependencies(Class source, Package comp) {
        List<Class> depClasses = new ArrayList<Class>();

        for (Class c : comp.getAllClasses()) {
            List<Relationship> relationships = new ArrayList<Relationship>(source.getRelationships());
            if (relationships != null) {
                for (Relationship relationship : relationships) {

                    if (relationship instanceof DependencyRelationship) {
                        DependencyRelationship dependency = (DependencyRelationship) relationship;
                        if (dependency.getClient().equals(source) && (!(depClasses.contains(c)))) {
                            depClasses.add(c);
                        }
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
