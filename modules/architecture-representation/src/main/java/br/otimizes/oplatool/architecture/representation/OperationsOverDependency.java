package br.otimizes.oplatool.architecture.representation;

import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.helpers.UtilResources;

/**
 * Operations over dependency
 */
public class OperationsOverDependency {

    private DependencyRelationship dependency;
    private final RelationshipsHolder relationshipHolder;

    public OperationsOverDependency(RelationshipsHolder relationshipHolder) {
        this.relationshipHolder = relationshipHolder;
    }

    /**
     * Criar relacionamento do tipo dependencia
     *
     * @param name
     * @return
     */
    public OperationsOverDependency create(String name) {
        dependency = new DependencyRelationship();
        dependency.setId(UtilResources.getRandomUUID());
        dependency.setName(name);
        return this;
    }

    /**
     * Configura client
     *
     * @param client
     * @return
     */
    public OperationsOverDependency withClient(Element client) {
        dependency.setClient(client);
        return this;
    }

    /**
     * Configura Supplier
     *
     * @param supplier
     * @return
     */
    public OperationsOverDependency withSupplier(Element supplier) {
        dependency.setSupplier(supplier);
        return this;
    }

    /**
     * Adiciona na lista de relacionamentos
     */
    public DependencyRelationship build() {
        this.relationshipHolder.addRelationship(dependency);
        return dependency;
    }

}
