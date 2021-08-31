package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;

/**
 * Operations over dependency
 */
public class OperationsOverDependency {

    private DependencyRelationship dependency;
    private final RelationshipsHolder relationshipHolder;

    public OperationsOverDependency(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
    }

    public OperationsOverDependency create(String name) {
        dependency = new DependencyRelationship();
        dependency.setId(UtilResources.getRandomUUID());
        dependency.setName(name);
        return this;
    }

    public OperationsOverDependency withClient(Element client) {
        dependency.setClient(client);
        return this;
    }

    public OperationsOverDependency withSupplier(Element supplier) {
        dependency.setSupplier(supplier);
        return this;
    }

    public DependencyRelationship build() {
        this.relationshipHolder.addRelationship(dependency);
        return dependency;
    }
}
