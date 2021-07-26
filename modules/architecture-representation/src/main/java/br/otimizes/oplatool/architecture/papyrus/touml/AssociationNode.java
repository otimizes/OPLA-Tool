package br.otimizes.oplatool.architecture.papyrus.touml;


import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Association node
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class AssociationNode extends XmiHelper {

    static Logger LOGGER = LogManager.getLogger(AssociationNode.class.getName());
    private final String idAssociation;
    private final String newModelName;
    private final Document docUml;
    private final Document docNotation;
    private final ElementXmiGenerator elementXmiGenerator;

    public AssociationNode(DocumentManager doc, Architecture a) {
        this.newModelName = doc.getModelName();
        this.docUml = doc.getDocUml();
        this.docNotation = doc.getDocNotation();

        this.idAssociation = UtilResources.getRandomUUID();
        this.elementXmiGenerator = new ElementXmiGenerator(doc, a);
    }

    public void createAssociation(AssociationEnd memberEnd1, AssociationEnd memberEnd2, String nameAssociation, String type) {

        Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);

        Element packageElement = getPackageElement(nameAssociation);

        String memberEnd1Id = UtilResources.getRandomUUID();
        String memberEnd2Id = UtilResources.getRandomUUID();
        packageElement.setAttribute("memberEnd", memberEnd1Id + " " + memberEnd2Id);

        Element ownedEnd1 = getOwnedEnd(memberEnd1, memberEnd1Id);
        if (((memberEnd1.isComposite()) || (memberEnd2.isComposite())) && ("composite".equalsIgnoreCase(type))) {
            ownedEnd1.setAttribute("aggregation", "composite");
        }
        if (((memberEnd1.isAggregation()) || (memberEnd2.isAggregation())) && ("shared".equalsIgnoreCase(type))) {
            ownedEnd1.setAttribute("aggregation", "shared");
        }
        packageElement.appendChild(ownedEnd1);

        Element ownedEnd2 = getOwnedEnd(memberEnd2, memberEnd2Id);

        if (((memberEnd1.isComposite()) || (memberEnd2.isComposite())) && ("composite".equalsIgnoreCase(type))) {
            ownedEnd1.setAttribute("aggregation", "composite");
        }
        if (((memberEnd1.isAggregation()) || (memberEnd2.isAggregation())) && ("shared".equalsIgnoreCase(type))) {
            ownedEnd1.setAttribute("aggregation", "shared");
        }

        packageElement.appendChild(ownedEnd2);

        if ((memberEnd2.isNavigable()) && (memberEnd1.isNavigable())) {
            packageElement.setAttribute("navigableOwnedEnd", memberEnd2Id + " " + memberEnd1Id);
        } else if (memberEnd2.isNavigable()) {
            packageElement.setAttribute("navigableOwnedEnd", memberEnd2Id);
        } else if (memberEnd1.isNavigable()) {
            packageElement.setAttribute("navigableOwnedEnd", memberEnd1Id);
        }

        Element lowerValue = getLowerValue(memberEnd1);
        ownedEnd1.appendChild(lowerValue);

        Element upperValue = getUpperValue(memberEnd1);
        ownedEnd1.appendChild(upperValue);

        Element lowerValue2 = getLowerValue(memberEnd2);
        ownedEnd2.appendChild(lowerValue2);

        Element upperValue2 = getUpperValue(memberEnd2);
        ownedEnd2.appendChild(upperValue2);

        modelRoot.appendChild(packageElement);
        elementXmiGenerator.createEgdeAssocationOnNotationFile(docNotation, newModelName, memberEnd1.getCLSClass().getId(), memberEnd2.getCLSClass().getId(), this.idAssociation);

    }

    private Element getPackageElement(String nameAssociation) {
        Element packageElement = this.docUml.createElement("packagedElement");
        packageElement.setAttribute("xmi:type", "uml:Association");
        packageElement.setAttribute("xmi:id", this.idAssociation);
        packageElement.setAttribute("name", nameAssociation);
        return packageElement;
    }

    private Element getLowerValue(AssociationEnd memberEnd2) {
        Element lowerValue2 = this.docUml.createElement("lowerValue");
        lowerValue2.setAttribute("xmi:type", "uml:LiteralInteger");
        lowerValue2.setAttribute("xmi:id", UtilResources.getRandomUUID());
        if (memberEnd2.getMultiplicity() == null)
            lowerValue2.setAttribute("value", "1");
        else
            lowerValue2.setAttribute("value", memberEnd2.getMultiplicity().getLowerValue());
        return lowerValue2;
    }

    private Element getUpperValue(AssociationEnd memberEnd2) {
        Element upperValue2 = this.docUml.createElement("upperValue");
        upperValue2.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
        upperValue2.setAttribute("xmi:id", UtilResources.getRandomUUID());
        if (memberEnd2.getMultiplicity() == null)
            upperValue2.setAttribute("value", "1");
        else
            upperValue2.setAttribute("value", memberEnd2.getMultiplicity().getUpperValue());
        return upperValue2;
    }

    private Element getOwnedEnd(AssociationEnd memberEnd1, String memberEnd1Id) {
        Element ownedEnd1 = this.docUml.createElement("ownedEnd");
        ownedEnd1.setAttribute("xmi:id", memberEnd1Id);
        ownedEnd1.setAttribute("name", memberEnd1.getCLSClass().getName());
        ownedEnd1.setAttribute("type", memberEnd1.getCLSClass().getId());
        ownedEnd1.setAttribute("association", this.idAssociation);
        return ownedEnd1;
    }

    public void removeAssociation(String id) {
        Node nodeToRemove = null;
        Node notationNode = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
        try {
            NodeList nodesEdges = docNotation.getElementsByTagName("edges");
            for (int i = 0; i < nodesEdges.getLength(); i++) {
                NodeList childNodes = nodesEdges.item(i).getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeName().equalsIgnoreCase("element")) {
                        String idHref = childNodes.item(j).getAttributes().getNamedItem("href").getNodeValue();
                        if (idHref.contains(id))
                            nodeToRemove = nodesEdges.item(i);
                    }

                }
            }
            notationNode.removeChild(nodeToRemove);
        } catch (Exception e) {
            LOGGER.error("Cannot remove Association with id: " + id + "." + e.getMessage());
        }

        removeAssociationFromUmlFile(id);
    }

    private void removeAssociationFromUmlFile(String id) {
        NodeList ownedAttributeElement = this.docUml.getElementsByTagName("ownedAttribute");
        NodeList ownedEndElement = this.docUml.getElementsByTagName("ownedEnd");
        NodeList packagedElementElement = this.docUml.getElementsByTagName("packagedElement");

        for (int i = 0; i < ownedAttributeElement.getLength(); i++) {
            if (ownedAttributeElement.item(i).getAttributes().getNamedItem("association") != null) {
                String idNode = ownedAttributeElement.item(i).getAttributes().getNamedItem("association").getNodeValue();
                if (id.equalsIgnoreCase(idNode)) {
                    ownedAttributeElement.item(i).getAttributes().removeNamedItem("association");
                    LOGGER.info("Association with id: " + id + " removed from UML file");
                }
            }
        }

        for (int i = 0; i < ownedEndElement.getLength(); i++) {
            ownedEndElement.item(i).getAttributes().removeNamedItem("association");
            LOGGER.info("Association with id: " + id + " removed from UML file");
        }

        for (int i = 0; i < packagedElementElement.getLength(); i++) {
            String idNode = packagedElementElement.item(i).getAttributes().getNamedItem("xmi:id").getNodeValue();
            if (id.equalsIgnoreCase(idNode)) {
                packagedElementElement.item(i).getParentNode().removeChild(packagedElementElement.item(i));
                LOGGER.info("Association with id: " + id + " removed from UML file");
            }
        }
    }

    public String getIdAssociation() {
        return this.idAssociation;
    }
}