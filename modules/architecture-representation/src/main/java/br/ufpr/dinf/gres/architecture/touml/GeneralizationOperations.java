package br.ufpr.dinf.gres.architecture.touml;


import br.ufpr.dinf.gres.architecture.exceptions.NotSuppportedOperation;
import br.ufpr.dinf.gres.architecture.helpers.XmiHelper;
import org.w3c.dom.Node;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class GeneralizationOperations extends XmiHelper implements Relationship {


    private DocumentManager documentManager;

    private String general;
    private String client;

    public GeneralizationOperations(DocumentManager doc) {
        this.documentManager = doc;
    }

    public Relationship createRelation() {
        return this;
    }

    /**
     * A client
     */
    public Relationship between(String idElement) throws NotSuppportedOperation {
        if (isElementAClass(idElement)) {
            this.client = idElement;
            return this;
        } else {
            throw new NotSuppportedOperation("Cannot create generaliazation with package");
        }
    }

    /**
     * A general
     */
    public Relationship and(String idElement) throws NotSuppportedOperation {
        if (isElementAClass(idElement)) {
            this.general = idElement;
            return this;
        } else {
            throw new NotSuppportedOperation("Cannot create generaliazation with package");
        }
    }


    private boolean isElementAClass(String idElement) {
        Node element = findByID(documentManager.getDocUml(), idElement, "packagedElement");
        if ("uml:Class".equalsIgnoreCase(element.getAttributes().getNamedItem("xmi:type").getNodeValue()))
            return true;
        return false;
    }

    public String build() {
        final GeneralizationNode generalizationNode = new GeneralizationNode(this.documentManager, this.general, this.client);

        br.ufpr.dinf.gres.architecture.touml.Document.executeTransformation(documentManager, new Transformation() {
            public void useTransformation() {
                generalizationNode.createGeneralization();
            }
        });

        return "";
    }

    public Relationship withMultiplicy(String string) throws NotSuppportedOperation {
        throw new NotSuppportedOperation("Generalization not support multiplicity");
    }

}