package arquitetura.touml;


import arquitetura.helpers.UtilResources;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import arquitetura.representation.relationship.AssociationEnd;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class AssociationNode extends XmiHelper {

    static Logger LOGGER = LogManager.getLogger(AssociationNode.class.getName());
    private final String idAssocation;
    private final String newModelName;
    private Document docUml;
    private Document docNotation;
    private ElementXmiGenerator elementXmiGenerator;

    public AssociationNode(DocumentManager doc, Architecture a) {
        this.newModelName = doc.getModelName();
        this.docUml = doc.getDocUml();
        this.docNotation = doc.getDocNotation();

        this.idAssocation = UtilResources.getRandonUUID();
        this.elementXmiGenerator = new ElementXmiGenerator(doc, a);
    }

    /**
     * Cria associacao
     *
     * @param memberEnd1
     * @param memberEnd2
     * @param nameAssociation
     * @param type
     */
    public void createAssociation(AssociationEnd memberEnd1, AssociationEnd memberEnd2, String nameAssociation, String type) {

        Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);

        Element packageElement = this.docUml.createElement("packagedElement");
        packageElement.setAttribute("xmi:type", "uml:Association");
        packageElement.setAttribute("xmi:id", this.idAssocation);
        packageElement.setAttribute("name", nameAssociation);


        String memberEnd1Id = UtilResources.getRandonUUID();
        String memberEnd2Id = UtilResources.getRandonUUID();
        packageElement.setAttribute("memberEnd", memberEnd1Id + " " + memberEnd2Id);

        Element ownedEnd1 = this.docUml.createElement("ownedEnd");
        ownedEnd1.setAttribute("xmi:id", memberEnd1Id);
        ownedEnd1.setAttribute("name", memberEnd1.getCLSClass().getName());
        ownedEnd1.setAttribute("type", memberEnd1.getCLSClass().getId());
        ownedEnd1.setAttribute("association", this.idAssocation);

        if (((memberEnd1.isComposite()) || (memberEnd2.isComposite())) && ("composite".equalsIgnoreCase(type))) {
            ownedEnd1.setAttribute("aggregation", "composite");
        }
        if (((memberEnd1.isAggregation()) || (memberEnd2.isAggregation())) && ("shared".equalsIgnoreCase(type))) {
            ownedEnd1.setAttribute("aggregation", "shared");
        }

        packageElement.appendChild(ownedEnd1);

        Element ownedEnd2 = this.docUml.createElement("ownedEnd");
        ownedEnd2.setAttribute("xmi:id", memberEnd2Id);
        ownedEnd2.setAttribute("name", memberEnd2.getCLSClass().getName());
        ownedEnd2.setAttribute("type", memberEnd2.getCLSClass().getId());
        ownedEnd2.setAttribute("association", this.idAssocation);

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


        Element lowerValue = this.docUml.createElement("lowerValue");
        lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
        lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
        if (memberEnd1.getMultiplicity() == null)
            lowerValue.setAttribute("value", "1");
        else
            lowerValue.setAttribute("value", memberEnd1.getMultiplicity().getLowerValue());
        ownedEnd1.appendChild(lowerValue);

        Element upperValue = this.docUml.createElement("upperValue");
        upperValue.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
        upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
        if (memberEnd1.getMultiplicity() == null)
            upperValue.setAttribute("value", "1");
        else
            upperValue.setAttribute("value", memberEnd1.getMultiplicity().getUpperValue());
        ownedEnd1.appendChild(upperValue);

        Element lowerValue2 = this.docUml.createElement("lowerValue");
        lowerValue2.setAttribute("xmi:type", "uml:LiteralInteger");
        lowerValue2.setAttribute("xmi:id", UtilResources.getRandonUUID());
        if (memberEnd2.getMultiplicity() == null)
            lowerValue2.setAttribute("value", "1");
        else
            lowerValue2.setAttribute("value", memberEnd2.getMultiplicity().getLowerValue());
        ownedEnd2.appendChild(lowerValue2);

        Element upperValue2 = this.docUml.createElement("upperValue");
        upperValue2.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
        upperValue2.setAttribute("xmi:id", UtilResources.getRandonUUID());
        if (memberEnd2.getMultiplicity() == null)
            upperValue2.setAttribute("value", "1");
        else
            upperValue2.setAttribute("value", memberEnd2.getMultiplicity().getUpperValue());
        ownedEnd2.appendChild(upperValue2);

        modelRoot.appendChild(packageElement);

        //ownedAttibute(multiplicityClassDestination);

        elementXmiGenerator.createEgdeAssocationOnNotationFile(docNotation, newModelName, memberEnd1.getCLSClass().getId(), memberEnd2.getCLSClass().getId(), this.idAssocation);

    }

//	private void ownedAttibute(String multiplicityClassDestination){
//		
//		//Primeiro busca pela class que seja a "dona" da associção. Isso é feito por meio do ID.
//		Node packageElementNode = findByID(docUml, this.idClassOwnnerAssociation.getCLSClass().getId(), "packagedElement");
//		
//		Element ownedAttibute = this.docUml.createElement("ownedAttribute");
//		ownedAttibute.setAttribute("xmi:id", memberEndId);
//		ownedAttibute.setAttribute("name", "ClassDestination");
//		ownedAttibute.setAttribute("type", this.idClassDestinationAssociation.getCLSClass().getId());
//		ownedAttibute.setAttribute("association", this.idAssocation);
//		
//		String multiLowerValue = "1";
//		String multiUpperValue = "1";
//		
//		if(multiplicityClassDestination != null){
//			 multiLowerValue = multiplicityClassDestination.substring(0, 1).trim();
//			 multiUpperValue = multiplicityClassDestination.substring(multiplicityClassDestination.length()-1, multiplicityClassDestination.length()).trim();
//		}
//		Element lowerValue = this.docUml.createElement("lowerValue");
//		if(multiLowerValue.equals("*"))
//			LOGGER.warn("Multiplicy lower value cannot be *. FIX IT");
//		else
//			lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
//		lowerValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
//		lowerValue.setAttribute("value",multiLowerValue);
//		ownedAttibute.appendChild(lowerValue);
//		
//		Element upperValue = this.docUml.createElement("upperValue");
//		if(multiUpperValue.equals("*"))
//			upperValue.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
//		else
//			upperValue.setAttribute("xmi:type", "uml:LiteralInteger");	
//		upperValue.setAttribute("xmi:id", UtilResources.getRandonUUID());
//		upperValue.setAttribute("value", multiUpperValue);
//		ownedAttibute.appendChild(upperValue);
//		
//		packageElementNode.appendChild(ownedAttibute);
//	}


    public void removeAssociation(String id) {
        //Busca por node "edges" no arquivo notation.

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

    public String getIdAssocation() {
        return this.idAssocation;
    }

}