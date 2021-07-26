package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;


/**
 * Association operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationOperations {

    private final DocumentManager documentManager;

    private AssociationEnd idClassOwnerAssociation;
    private AssociationEnd idClassDestinationAssociation;
    private String name;


    public AssociationOperations(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public AssociationOperations createAssociation() {
        return new AssociationOperations(documentManager);
    }

    public AssociationOperations betweenClass(AssociationEnd associationEnd) {
        this.idClassOwnerAssociation = associationEnd;
        return this;
    }

    public AssociationOperations andClass(AssociationEnd associationEnd) {
        this.idClassDestinationAssociation = associationEnd;
        return this;
    }

    public String build() {
        final AssociationNode associationNode = new AssociationNode(this.documentManager, null);
        Document.executeTransformation(documentManager, () -> associationNode
                .createAssociation(idClassOwnerAssociation, idClassDestinationAssociation, name, "none"));
        return associationNode.getIdAssociation();
    }

    public AssociationOperations withName(String name) {
        this.name = name;
        return this;
    }
}