package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.exceptions.NotSuppportedOperation;

/**
 * Usage operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class UsageOperations implements Relationship {

    private static final String USAGE = "usage";

    private final DocumentManager documentManager;

    private String clientElement;
    private String supplierElement;
    private String name;

    public UsageOperations(DocumentManager doc) {
        this.documentManager = doc;
    }

    public UsageOperations(DocumentManager documentManager, String name) {
        this.name = name;
        this.documentManager = documentManager;
    }

    public UsageOperations createRelation(String name) {
        return new UsageOperations(this.documentManager, name);
    }

    public UsageOperations between(String idElement) {
        this.clientElement = idElement;
        return this;
    }

    public UsageOperations and(String idElement) {
        this.supplierElement = idElement;
        return this;
    }

    public String build() {
        final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name,
                this.clientElement, this.supplierElement, null, null);
        Document.executeTransformation(documentManager, () -> dependencyNode.createDependency(USAGE));
        return "";
    }

    public UsageOperations withMultiplicity(String string) throws NotSuppportedOperation {
        throw new NotSuppportedOperation("Usage dont have multiplicity");
    }

    public Relationship createRelation() {
        return this;
    }
}