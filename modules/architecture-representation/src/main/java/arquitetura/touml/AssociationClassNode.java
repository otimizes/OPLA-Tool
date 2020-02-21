package arquitetura.touml;

import arquitetura.exceptions.NullReferenceFoundException;
import arquitetura.helpers.UtilResources;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AssociationClassNode extends XmiHelper {


    private String newModelName;
    private Document docUml;
    private Document docNotation;
    private ElementXmiGenerator elementXmiGenerator;
    private String idAssociation;
    private ClassNotation notation;

    public AssociationClassNode(DocumentManager documentManager, Architecture a) {
        this.newModelName = documentManager.getModelName();
        this.docUml = documentManager.getDocUml();
        this.docNotation = documentManager.getDocNotation();

        this.elementXmiGenerator = new ElementXmiGenerator(documentManager, a);
        this.notation = new ClassNotation(documentManager, docNotation);
    }

    public void createAssociationClass(String id, String ownedEndElement, String associationEndDestination, String nameAssociationClass) {

        this.idAssociation = id;
        Node nodeToAddOwnedAttributeForAssociationClass = null;
        String idOwnedAttribute = null;

        idOwnedAttribute = UtilResources.getRandonUUID();
        String idOwnedEnd = UtilResources.getRandonUUID();

        Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);
        nodeToAddOwnedAttributeForAssociationClass = findByID(docUml, ownedEndElement, "packagedElement");

        Element packageElement = this.docUml.createElement("packagedElement");
        packageElement.setAttribute("xmi:type", "uml:AssociationClass");
        packageElement.setAttribute("xmi:id", this.idAssociation);
        packageElement.setAttribute("name", nameAssociationClass);
        packageElement.setAttribute("memberEnd", idOwnedAttribute + " " + idOwnedEnd);

        Element ownedEnd = this.docUml.createElement("ownedEnd");
        ownedEnd.setAttribute("xmi:id", idOwnedEnd);
        ownedEnd.setAttribute("name", "nome");
        ownedEnd.setAttribute("type", ownedEndElement);
        ownedEnd.setAttribute("association", this.idAssociation);

        Element lowerValue = this.docUml.createElement("lowerValue");
        lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
        lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
        lowerValue.setAttribute("value", "1");
        ownedEnd.appendChild(lowerValue);

        Element upperValue = this.docUml.createElement("upperValue");
        upperValue.setAttribute("xmi:type", "uml:LiteralInteger");
        upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
        upperValue.setAttribute("value", "1");
        ownedEnd.appendChild(upperValue);
        packageElement.appendChild(ownedEnd);

        modelRoot.appendChild(packageElement);

        ownedAttibute(nodeToAddOwnedAttributeForAssociationClass, idOwnedAttribute, associationEndDestination);

        try {
            String idChildren = notation.createXmiForClassInNotationFile(this.idAssociation, null, "associationClass");
            String idEdge = elementXmiGenerator.createEgdeAssocationOnNotationFile(docNotation, newModelName, ownedEndElement, associationEndDestination, this.idAssociation);
            elementXmiGenerator.createEgdgeAssociationClassOnNotationFile(idChildren, idEdge);
        } catch (NullReferenceFoundException e) {
            e.printStackTrace();
        }

    }

    private void ownedAttibute(Node nodeToAddAttribute, String idOwnedAttribute, String associationEndDestination) {

        Element ownedAttibute = this.docUml.createElement("ownedAttribute");
        ownedAttibute.setAttribute("xmi:id", idOwnedAttribute);
        ownedAttibute.setAttribute("name", "ClassDestination");
        ownedAttibute.setAttribute("type", associationEndDestination);
        ownedAttibute.setAttribute("association", this.idAssociation);

        Element lowerValue = this.docUml.createElement("lowerValue");
        lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
        lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
        lowerValue.setAttribute("value", "1");
        ownedAttibute.appendChild(lowerValue);

        Element upperValue = this.docUml.createElement("upperValue");
        upperValue.setAttribute("xmi:type", "uml:LiteralInteger");
        upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
        upperValue.setAttribute("value", "1");
        ownedAttibute.appendChild(upperValue);

        nodeToAddAttribute.appendChild(ownedAttibute);
    }

}
