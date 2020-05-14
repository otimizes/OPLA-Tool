package br.ufpr.dinf.gres.architecture.touml;

import br.ufpr.dinf.gres.architecture.exceptions.CustonTypeNotFound;
import br.ufpr.dinf.gres.architecture.exceptions.InvalidMultiplictyForAssociationException;
import br.ufpr.dinf.gres.architecture.exceptions.NodeNotFound;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationEnd;

/**
 * Composition operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class CompositionOperations {

    private DocumentManager doc;
    private AssociationEnd client;
    private AssociationEnd target;
    private String name;

    public CompositionOperations(DocumentManager doc) {
        this.doc = doc;
    }

    public CompositionOperations createComposition() {
        return new CompositionOperations(doc);
    }

    public CompositionOperations between(AssociationEnd idElement) {
        this.client = idElement;
        return this;
    }

    public CompositionOperations and(AssociationEnd idElement) {
        this.target = idElement;
        return this;
    }

    public CompositionOperations withName(String name) {
        this.name = name;
        return this;
    }

    public void build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException {
        final AssociationNode cn = new AssociationNode(doc, null);

        br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(doc, new Transformation() {
            public void useTransformation() {
                cn.createAssociation(client, target, name, "composite");
            }
        });
    }


}