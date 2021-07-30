package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.exceptions.NotSuppportedOperation;
import br.otimizes.oplatool.architecture.representation.Architecture;

/**
 * Realizations operations
 */
public class RealizationsOperations implements Relationship {

    private static final String REALIZATION = "realization";

    private final DocumentManager documentManager;

    private String clientElement;
    private String supplierElement;
    private String name;
    private Architecture a;

    public RealizationsOperations(DocumentManager doc) {
        this.documentManager = doc;
    }

    public RealizationsOperations(DocumentManager documentManager2, String name2) {
        this.documentManager = documentManager2;
        this.name = name2;
    }

    public RealizationsOperations createRelation() {
        return new RealizationsOperations(this.documentManager);
    }

    public RealizationsOperations between(String idElement) {
        this.clientElement = idElement;
        return this;
    }

    public RealizationsOperations and(String idElement) {
        this.supplierElement = idElement;
        return this;
    }

    public String build() {
        final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement,
                this.supplierElement, null, a);
        Document.executeTransformation(documentManager, () -> dependencyNode.createDependency(REALIZATION));
        return "";
    }

    public Relationship withMultiplicity(String string) throws NotSuppportedOperation {
        throw new NotSuppportedOperation("Realization dont have multiplicity");
    }

    public RealizationsOperations withName(String realizationName) {
        if (realizationName == null)
            this.name = "";
        else
            this.name = realizationName;
        return this;
    }
}