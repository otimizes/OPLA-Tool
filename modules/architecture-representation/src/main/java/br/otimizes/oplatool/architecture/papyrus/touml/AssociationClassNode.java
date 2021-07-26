package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.exceptions.NullReferenceFoundException;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Association class node
 */
public class AssociationClassNode extends XmiHelper {
    private final String newModelName;
    private final Document docUml;
    private final Document docNotation;
    private final ElementXmiGenerator elementXmiGenerator;
    private String idAssociation;
    private final ClassNotation notation;

    public AssociationClassNode(DocumentManager documentManager, Architecture a) {
        this.newModelName = documentManager.getModelName();
        this.docUml = documentManager.getDocUml();
        this.docNotation = documentManager.getDocNotation();

        this.elementXmiGenerator = new ElementXmiGenerator(documentManager, a);
        this.notation = new ClassNotation(documentManager, docNotation);
    }

    public void createAssociationClass(String id, String ownedEndElement, String associationEndDestination, String nameAssociationClass) {
        this.idAssociation = id;
        String idOwnedAttribute = UtilResources.getRandomUUID();
        String idOwnedEnd = UtilResources.getRandomUUID();

        Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);
        Node nodeToAddOwnedAttributeForAssociationClass = findByID(docUml, ownedEndElement, "packagedElement");

        Element packageElement = getPackageElement(nameAssociationClass, idOwnedAttribute, idOwnedEnd);
        Element ownedEnd = getOwnedEnd(ownedEndElement, idOwnedEnd);
        Element lowerValue = getLowerValue();
        ownedEnd.appendChild(lowerValue);

        Element upperValue = getUpperValue();
        ownedEnd.appendChild(upperValue);
        packageElement.appendChild(ownedEnd);
        modelRoot.appendChild(packageElement);

        Element ownedAttribute = getOwnedAttribute(associationEndDestination, idOwnedAttribute);
        Element newLowerValue = getLowerValue();
        ownedAttribute.appendChild(newLowerValue);

        Element newUpperValue = getUpperValue();
        ownedAttribute.appendChild(newUpperValue);

        if (nodeToAddOwnedAttributeForAssociationClass != null)
            nodeToAddOwnedAttributeForAssociationClass.appendChild(ownedAttribute);

        try {
            String idChildren = notation.createXmiForClassInNotationFile(this.idAssociation, null, "associationClass", null);
            String idEdge = elementXmiGenerator.createEdgeAssociationOnNotationFile(docNotation, newModelName,
                    ownedEndElement, associationEndDestination, this.idAssociation);
            elementXmiGenerator.createEdgeAssociationClassOnNotationFile(idChildren, idEdge);
        } catch (NullReferenceFoundException e) {
            e.printStackTrace();
        }
    }

    private Element getPackageElement(String nameAssociationClass, String idOwnedAttribute, String idOwnedEnd) {
        Element packageElement = this.docUml.createElement("packagedElement");
        packageElement.setAttribute("xmi:type", "uml:AssociationClass");
        packageElement.setAttribute("xmi:id", this.idAssociation);
        packageElement.setAttribute("name", nameAssociationClass);
        packageElement.setAttribute("memberEnd", idOwnedAttribute + " " + idOwnedEnd);
        return packageElement;
    }

    private Element getOwnedEnd(String ownedEndElement, String idOwnedEnd) {
        Element ownedEnd = this.docUml.createElement("ownedEnd");
        ownedEnd.setAttribute("xmi:id", idOwnedEnd);
        ownedEnd.setAttribute("name", "nome");
        ownedEnd.setAttribute("type", ownedEndElement);
        ownedEnd.setAttribute("association", this.idAssociation);
        return ownedEnd;
    }

    private Element getLowerValue() {
        Element lowerValue = this.docUml.createElement("lowerValue");
        lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
        lowerValue.setAttribute("xmi:id", UtilResources.getRandomUUID());
        lowerValue.setAttribute("value", "1");
        return lowerValue;
    }

    private Element getUpperValue() {
        Element newUpperValue = this.docUml.createElement("upperValue");
        newUpperValue.setAttribute("xmi:type", "uml:LiteralInteger");
        newUpperValue.setAttribute("xmi:id", UtilResources.getRandomUUID());
        newUpperValue.setAttribute("value", "1");
        return newUpperValue;
    }

    private Element getOwnedAttribute(String associationEndDestination, String idOwnedAttribute) {
        Element ownedAttribute = this.docUml.createElement("ownedAttribute");
        ownedAttribute.setAttribute("xmi:id", idOwnedAttribute);
        ownedAttribute.setAttribute("name", "ClassDestination");
        ownedAttribute.setAttribute("type", associationEndDestination);
        ownedAttribute.setAttribute("association", this.idAssociation);
        return ownedAttribute;
    }
}
