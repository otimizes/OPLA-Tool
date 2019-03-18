package jmetal4.metrics.conventionalMetrics;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.*;

import java.util.ArrayList;
import java.util.List;

public class DependencyIn {

    /**
     * @param args
     */
    private Architecture architecture;
    private int results;

    public DependencyIn(Architecture architecture) {

        this.architecture = architecture;
        this.results = 0;
        int depIn = 0;

        for (Package component : this.architecture.getAllPackages()) {
            List<Relationship> relationships = new ArrayList<Relationship>(architecture.getRelationshipHolder().getAllRelationships());

            for (Relationship relationship : relationships) {
                if (relationship instanceof AbstractionRelationship) {
                    AbstractionRelationship abstraction = (AbstractionRelationship) relationship;
                    if (abstraction.getSupplier().getNamespace().contains(component.getName())) depIn++;

                }else if (relationship instanceof DependencyRelationship) {
                    DependencyRelationship dependency = (DependencyRelationship) relationship;
                    if (dependency.getSupplier().getNamespace().contains(component.getName())) depIn++;

                }else if (relationship instanceof UsageRelationship) {
                    UsageRelationship usage = (UsageRelationship) relationship;
                    if (usage.getSupplier().getNamespace().contains(component.getName())) depIn++;

                }else if (relationship instanceof RealizationRelationship) {
                    RealizationRelationship realization = (RealizationRelationship) relationship;
                    if (realization.getSupplier().getNamespace().contains(component.getName())) depIn++;

                }
            }


            this.results += depIn; // somatorio de DepIn da arquitetura como um todo
            depIn= 0;
        }
    }

    public int getResults() {
        return results;
    }

}