package arquitetura.touml;

import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.representation.Architecture;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class UsageOperations implements Relationship {

    private static final String USAGE = "usage";

    private DocumentManager documentManager;

    private String clientElement;
    private String supplierElement;
    private String name;

    public UsageOperations(DocumentManager doc, Architecture a) {
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
        final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement, this.supplierElement, null, null);

        arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                dependencyNode.createDependency(USAGE);
            }
        });

        return ""; //TODO return id;

    }

    public UsageOperations withMultiplicy(String string) throws NotSuppportedOperation {
        throw new NotSuppportedOperation("Usage dont have multiplicy");
    }

    public Relationship createRelation() {
        return this;
    }

}