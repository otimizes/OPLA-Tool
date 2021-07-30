package br.otimizes.oplatool.architecture.papyrus.touml;


import br.otimizes.oplatool.architecture.exceptions.NotSuppportedOperation;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import org.w3c.dom.Node;

import java.util.Objects;

/**
 * Generalization operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class GeneralizationOperations extends XmiHelper implements Relationship {
    private final DocumentManager documentManager;

    private String general;
    private String client;

    public GeneralizationOperations(DocumentManager doc) {
        this.documentManager = doc;
    }

    public Relationship createRelation() {
        return this;
    }

    public Relationship between(String idElement) throws NotSuppportedOperation {
        if (isElementAClass(idElement)) {
            this.client = idElement;
            return this;
        } else {
            throw new NotSuppportedOperation("Cannot create generalization with package");
        }
    }

    public Relationship and(String idElement) throws NotSuppportedOperation {
        if (isElementAClass(idElement)) {
            this.general = idElement;
            return this;
        } else {
            throw new NotSuppportedOperation("Cannot create generalization with package");
        }
    }


    private boolean isElementAClass(String idElement) {
        Node element = findByID(documentManager.getDocUml(), idElement, "packagedElement");
        return "uml:Class".equalsIgnoreCase(Objects.requireNonNull(element).getAttributes().getNamedItem("xmi:type").getNodeValue());
    }

    public String build() {
        final GeneralizationNode generalizationNode = new GeneralizationNode(this.documentManager, this.general, this.client);
        Document.executeTransformation(documentManager, generalizationNode::createGeneralization);
        return "";
    }

    public Relationship withMultiplicity(String string) throws NotSuppportedOperation {
        throw new NotSuppportedOperation("Generalization not support multiplicity");
    }
}