package arquitetura.touml;

import arquitetura.representation.relationship.AssociationEnd;


/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AssociationOperations {

    private DocumentManager documentManager;

    private AssociationEnd idClassOwnnerAssociation;
    private AssociationEnd idClassDestinationAssociation;
    private String name;


    public AssociationOperations(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public AssociationOperations createAssociation() {
        return new AssociationOperations(documentManager);
    }

    public AssociationOperations betweenClass(AssociationEnd associationEnd) {
        this.idClassOwnnerAssociation = associationEnd;
        return this;
    }

    public AssociationOperations andClass(AssociationEnd associationEnd) {
        this.idClassDestinationAssociation = associationEnd;
        return this;
    }

    public String build() {
        //Refactoring, document.getNewName is common for many classes
        final AssociationNode associationNode = new AssociationNode(this.documentManager, null);

        arquitetura.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                associationNode.createAssociation(idClassOwnnerAssociation, idClassDestinationAssociation, name, "none");
            }
        });

        return associationNode.getIdAssocation();
    }

    public AssociationOperations withName(String name) {
        this.name = name;
        return this;
    }

}