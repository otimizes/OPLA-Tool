package arquitetura.touml;

import arquitetura.exceptions.NotSuppportedOperation;
import arquitetura.representation.Architecture;

import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class DependencyOperations implements Relationship {

    private DocumentManager documentManager;

    private String clientElement;
    private String supplierElement;
    private String name;
    private List<String> stereotypes;
    private Architecture a;

    public DependencyOperations(DocumentManager doc, Architecture a) {
        this.documentManager = doc;
        this.a = a;
    }

    public DependencyOperations(DocumentManager documentManager2, String name2) {
        this.documentManager = documentManager2;
        this.name = name2;
    }

    public DependencyOperations createRelation() {
        if (("".equals(name) || name == null)) name = "dependency";
        return new DependencyOperations(this.documentManager, name);
    }

    public DependencyOperations between(String idElement) {
        this.clientElement = idElement;
        return this;
    }

    public DependencyOperations and(String idElement) {
        this.supplierElement = idElement;
        return this;
    }

    public String build() {
        final DependencyNode dependencyNode = new DependencyNode(this.documentManager, this.name, this.clientElement, this.supplierElement, stereotypes, a);

        arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                dependencyNode.createDependency("dependency");
            }
        });

        return ""; //TODO return id;

    }

    public DependencyOperations withStereotypes(List<String> stereotypes) {
        this.stereotypes = stereotypes;
        return this;
    }

    public DependencyOperations withMultiplicy(String string) throws NotSuppportedOperation {
        throw new NotSuppportedOperation("Dependency dont have multiplicy");
    }

    public DependencyOperations withName(String relationshipName) {
        if (relationshipName == null)
            this.name = "";
        else
            this.name = relationshipName;

        return this;
    }

}