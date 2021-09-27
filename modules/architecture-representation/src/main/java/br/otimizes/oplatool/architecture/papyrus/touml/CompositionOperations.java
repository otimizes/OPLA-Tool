package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.exceptions.CustonTypeNotFound;
import br.otimizes.oplatool.architecture.exceptions.InvalidMultiplicityForAssociationException;
import br.otimizes.oplatool.architecture.exceptions.NodeNotFound;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;

/**
 * Composition operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class CompositionOperations {

    private final DocumentManager doc;
    private AssociationEnd client;
    private AssociationEnd target;
    private String name;

    public CompositionOperations(DocumentManager doc) {
        this.doc = doc;
    }

    public CompositionOperations createComposition() {
        return new CompositionOperations(doc);
    }

    public CompositionOperations between(AssociationEnd associationEnd) {
        this.client = associationEnd;
        return this;
    }

    public CompositionOperations and(AssociationEnd associationEnd) {
        this.target = associationEnd;
        return this;
    }

    public CompositionOperations withName(String name) {
        this.name = name;
        return this;
    }

    public void build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplicityForAssociationException {
        final AssociationNode associationNode = new AssociationNode(doc, null);
        Document.executeTransformation(doc, () -> associationNode.createAssociation(client, target, name, "composite"));
    }
}