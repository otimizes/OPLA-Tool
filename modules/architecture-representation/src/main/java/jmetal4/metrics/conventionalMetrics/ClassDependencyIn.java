package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.Relationship;

import java.util.ArrayList;
import java.util.List;

public class ClassDependencyIn {

    /**
     * @param args
     */
    private Architecture architecture;
    private int results;

    public ClassDependencyIn(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0;
        int depIn = 0;


        //EDIPO - Mudei para Pacote....
        for (Package component : this.architecture.getAllPackages()) {
            for (Class cls : component.getAllClasses()) {
                depIn += searchClassDependencies(cls, component);
                //System.out.println("DepIn- Classe: "+ cls.getName() + " :" + depIn);
            }

            this.results += depIn; // somatorio de DepIn da arquitetura como um todo
            depIn = 0;
        }

    }

//----------------------------------------------------------------------------------

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
            }
        }//end for classes

        return depClasses.size();
    }

    // ---------------------------------------------------------------------------------

    public int getResults() {
        return results;
    }

}
