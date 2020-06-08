package br.otimizes.oplatool.core.jmetal4.operators.crossover;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Crossover relationship methods
 */
public class CrossoverRelationship {

    private static Set<Relationship> relationships = new HashSet<Relationship>();

    public static void saveAllRelationshiopForElement(Element element, Architecture parent, Architecture offspring) {
        if (element instanceof Package) {
            Package packagee = (Package) element;
            Set<Relationship> relations = packagee.getRelationships();
            for (Relationship r : relations)
                offspring.getRelationshipHolder().addRelationship(r);

            relations = null;
            return;
        }
        if (element instanceof Class) {
            Class klass = (Class) element;
            Set<Relationship> relations = klass.getRelationships();
            for (Relationship r : relations)
                offspring.getRelationshipHolder().addRelationship(r);

            relations = null;
            return;
        }
        if (element instanceof Interface) {
            Interface inter = (Interface) element;
            Set<Relationship> relations = inter.getRelationships();
            for (Relationship r : relations)
                offspring.getRelationshipHolder().addRelationship(r);

            relations = null;
            return;
        }
    }

    public static void cleanRelationships() {
        relationships.clear();
    }
}