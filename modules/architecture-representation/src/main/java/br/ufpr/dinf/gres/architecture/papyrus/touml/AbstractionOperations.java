package br.ufpr.dinf.gres.architecture.papyrus.touml;

import br.ufpr.dinf.gres.architecture.exceptions.NotSuppportedOperation;
import br.ufpr.dinf.gres.architecture.representation.Architecture;

/**
 * Abstraction operations class
 */
public class AbstractionOperations implements Relationship {

    private static final String ABSTRACTION = "abstraction";

    private DocumentManager documentManager;

    private String clientElement;
    private String supplierElement;
    private String name;
    private Architecture a;

    public AbstractionOperations(DocumentManager doc) {
        this.documentManager = doc;
    }

    public AbstractionOperations(DocumentManager documentManager2, String name2) {
        this.documentManager = documentManager2;
        this.name = name2;
    }

    public AbstractionOperations createRelation() {
        return new AbstractionOperations(this.documentManager);
    }

    public AbstractionOperations between(String idElement) {
        this.clientElement = idElement;
        return this;
    }

    public AbstractionOperations and(String idElement) {
        this.supplierElement = idElement;
        return this;
    }

    public String build() {
        final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement, this.supplierElement, null, a);

        br.ufpr.dinf.gres.architecture.papyrus.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                dependencyNode.createDependency(ABSTRACTION);
            }
        });

        return ""; //TODO return id;

    }

    public Relationship withMultiplicy(String string) throws NotSuppportedOperation {
        throw new NotSuppportedOperation("Realization dont have multiplicy");
    }

    public AbstractionOperations withName(String realizationName) {
        if (realizationName == null)
            this.name = "";
        else
            this.name = realizationName;
        return this;
    }

}
