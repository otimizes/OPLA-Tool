package br.otimizes.oplatool.architecture.papyrus.touml;


import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Generalization node
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class GeneralizationNode extends XmiHelper {

    private final DocumentManager documentManager;
    private final Document docUml;
    private final Document docNotation;
    private final String clientElement;
    private final String general;
    private String id;

    public GeneralizationNode(DocumentManager documentManager, String general, String client) {
        this.documentManager = documentManager;
        this.docUml = documentManager.getDocUml();
        this.docNotation = documentManager.getDocNotation();
        this.clientElement = client;
        this.general = general;
    }

    public void createGeneralization() throws DOMException {
        createNodeInUmlFile();
        Node notationFileClient = findByIDInNotationFile(docNotation, clientElement);
        Node notationFileGeneral = findByIDInNotationFile(docNotation, general);
        if (notationFileClient != null && notationFileGeneral != null) {
            String idSource = notationFileClient.getAttributes().getNamedItem("xmi:id").getNodeValue();
            String idTarget = notationFileGeneral.getAttributes().getNamedItem("xmi:id").getNodeValue();
            Node node = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
            Element edges = getEdges(idSource, idTarget);

            Element element = getElement();
            edges.appendChild(element);

            Element firstChildrenDecorationNode = getChildrenDecorationNode();
            edges.appendChild(firstChildrenDecorationNode);

            Element bendPoints = getBendPoints();
            edges.appendChild(bendPoints);

            Element sourceAnchor = getSourceAnchor();
            edges.appendChild(sourceAnchor);

            Element targetAnchor = getTargetAnchor();
            edges.appendChild(targetAnchor);

            node.appendChild(edges);
        }
    }

    private Element getEdges(String idSource, String idTarget) {
        Element edges = this.docNotation.createElement("edges");
        edges.setAttribute("xmi:type", "notation:Connector");
        edges.setAttribute("xmi:id", UtilResources.getRandomUUID());
        edges.setAttribute("type", "4002");
        edges.setAttribute("source", idSource);
        edges.setAttribute("target", idTarget);
        edges.setAttribute("routing", "Rectilinear");
        edges.setAttribute("lineColor", "0");
        return edges;
    }

    private Element getElement() {
        Element element = docNotation.createElement("element");
        element.setAttribute("xmi:type", "uml:Dependency");
        element.setAttribute("href", documentManager.getNewModelName() + ".uml#" + this.id);
        return element;
    }

    private Element getChildrenDecorationNode() {
        Element firstChildrenDecorationNode = this.docNotation.createElement("children");
        firstChildrenDecorationNode.setAttribute("xmi:type", "notation:DecorationNode");
        firstChildrenDecorationNode.setAttribute("xmi:id", UtilResources.getRandomUUID());
        firstChildrenDecorationNode.setAttribute("type", "6007");
        return firstChildrenDecorationNode;
    }

    private Element getBendPoints() {
        Element bendPoints = docNotation.createElement("bendpoints");
        bendPoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
        bendPoints.setAttribute("xmi:id", UtilResources.getRandomUUID());
        bendPoints.setAttribute("points", "[0, 0, 476, 181]$[-467, -170, 9, 11]");
        return bendPoints;
    }

    private Element getSourceAnchor() {
        Element sourceAnchor = docNotation.createElement("sourceAnchor");
        sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
        sourceAnchor.setAttribute("xmi:id", UtilResources.getRandomUUID());
        sourceAnchor.setAttribute("id", "(0.42,0.0)");
        return sourceAnchor;
    }

    private Element getTargetAnchor() {
        Element targetAnchor = docNotation.createElement("targetAnchor");
        targetAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
        targetAnchor.setAttribute("xmi:id", UtilResources.getRandomUUID());
        targetAnchor.setAttribute("id", "(0.82,0.89)");
        return targetAnchor;
    }

    private void createNodeInUmlFile() {
        this.id = UtilResources.getRandomUUID();
        Element classClient = (Element) findByID(this.docUml, this.clientElement, "packagedElement");
        Element generalization = this.docUml.createElement("generalization");
        generalization.setAttribute("xmi:id", id);
        generalization.setAttribute("general", this.general);
        if (classClient != null) classClient.appendChild(generalization);
    }
}