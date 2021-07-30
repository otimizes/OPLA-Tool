package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.representation.relationship.Relationship;

import java.util.Set;

public interface Linkable extends Identifiable {
    Set<Relationship> getRelationships();
}
