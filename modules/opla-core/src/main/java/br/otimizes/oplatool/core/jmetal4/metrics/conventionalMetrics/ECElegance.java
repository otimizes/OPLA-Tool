package br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

import java.util.ArrayList;
import java.util.List;

/**
 * EC - External Couples
 * <p>
 * Measures the elegance of external couplings of the classes (Simons e Parmee, 2012)
 */
public class ECElegance extends ObjectiveFunctionImplementation {

    public ECElegance(Architecture architecture) {
        super(architecture);
        this.setResults(0.0);
        double stdDeviationCouples;
        double[] externalCouplesNumbers = new double[10000];
        int i = 0;

        ConventionalStatisticMetrics e = new ConventionalStatisticMetrics();

        for (Class cls : architecture.getAllClasses()) {
            // busca os external couples de cada classe
            externalCouplesNumbers[i] = searchClassDependencies(cls);
            i++;
        }

        e.setArray(externalCouplesNumbers);
        stdDeviationCouples = e.getSampleStandardDeviation();

        this.setResults(stdDeviationCouples);

    }

    private int searchClassDependencies(Class source) {
        int cont = 0;
        List<Relationship> relationships = new ArrayList<>(source.getRelationships());
        for (Relationship relationship : relationships) {
            if (relationship instanceof DependencyRelationship) {
                DependencyRelationship dependency = (DependencyRelationship) relationship;
                if (dependency.getClient().equals(source))
                    cont++;
            } else {
                if (relationship instanceof AssociationRelationship) {
                    AssociationRelationship association = (AssociationRelationship) relationship;
                    for (AssociationEnd associationEnd : association.getParticipants()) {
                        if (associationEnd.getCLSClass().equals(source))
                            cont++;
                    }
                }
            }
        }
        return cont;
    }


}
