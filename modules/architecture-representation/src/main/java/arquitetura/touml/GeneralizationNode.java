package arquitetura.touml;


import arquitetura.helpers.UtilResources;
import arquitetura.helpers.XmiHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class GeneralizationNode extends XmiHelper {

    private DocumentManager documentManager;
    private Document docUml;
    private Document docNotation;
    private String clientElement;
    private String general;
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


        String idSource = findByIDInNotationFile(docNotation, clientElement).getAttributes().getNamedItem("xmi:id").getNodeValue();
        String idTarget = findByIDInNotationFile(docNotation, general).getAttributes().getNamedItem("xmi:id").getNodeValue();

        Node node = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
        Element edges = this.docNotation.createElement("edges");
        edges.setAttribute("xmi:type", "notation:Connector");
        edges.setAttribute("xmi:id", UtilResources.getRandonUUID());
        edges.setAttribute("type", "4002");
        edges.setAttribute("source", idSource);
        edges.setAttribute("target", idTarget);
        edges.setAttribute("lineColor", "0");

        Element element = docNotation.createElement("element");
        element.setAttribute("xmi:type", "uml:Dependency");
        element.setAttribute("href", documentManager.getNewModelName() + ".uml#" + this.id);
        edges.appendChild(element);

        Element childrenDocorationnode1 = this.docNotation.createElement("children");
        childrenDocorationnode1.setAttribute("xmi:type", "notation:DecorationNode");
        childrenDocorationnode1.setAttribute("xmi:id", UtilResources.getRandonUUID());
        childrenDocorationnode1.setAttribute("type", "6007");
        edges.appendChild(childrenDocorationnode1);


        Element bendpoints = docNotation.createElement("bendpoints");
        bendpoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
        bendpoints.setAttribute("xmi:id", UtilResources.getRandonUUID());
        bendpoints.setAttribute("points", "[0, 0, 476, 181]$[-467, -170, 9, 11]");
        edges.appendChild(bendpoints);

        Element sourceAnchor = docNotation.createElement("sourceAnchor");
        sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
        sourceAnchor.setAttribute("xmi:id", UtilResources.getRandonUUID());
        sourceAnchor.setAttribute("id", "(0.42,0.0)");
        edges.appendChild(sourceAnchor);

        Element targetAnchor = docNotation.createElement("targetAnchor");
        targetAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
        targetAnchor.setAttribute("xmi:id", UtilResources.getRandonUUID());
        targetAnchor.setAttribute("id", "(0.82,0.89)");
        edges.appendChild(targetAnchor);

        node.appendChild(edges);
    }

    private void createNodeInUmlFile() {
        this.id = UtilResources.getRandonUUID();
        Element classClient = (Element) findByID(this.docUml, this.clientElement, "packagedElement");
        Element generalization = this.docUml.createElement("generalization");
        generalization.setAttribute("xmi:id", id);
        generalization.setAttribute("general", this.general);

        classClient.appendChild(generalization);
    }

}