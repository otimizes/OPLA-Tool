package br.otimizes.oplatool.architecture.papyrus.touml;


import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

/**
 * Dependency node
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class DependencyNode extends XmiHelper {

    private final Document docUml;
    private final Document docNotation;
    private final DocumentManager documentManager;
    private final String clientElement;
    private final String supplierElement;
    private final List<String> stereotypes;
    private final String name;
    private String id;

    public DependencyNode(DocumentManager documentManager, String name, String clientElement, String supplierElement, List<String> stereotypes, Architecture a) {
        this.documentManager = documentManager;
        this.docUml = documentManager.getDocUml();
        this.docNotation = documentManager.getDocNotation();
        this.clientElement = clientElement;
        this.supplierElement = supplierElement;
        this.stereotypes = stereotypes;
        this.name = name;
    }

    public void createDependency(String type) {
        try {
            createDependencyInUmlFile(type);
            Node node = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
            Element edges = this.docNotation.createElement("edges");
            edges.setAttribute("xmi:type", "notation:Connector");
            edges.setAttribute("xmi:id", UtilResources.getRandomUUID());
            edges.setAttribute("routing", "Rectilinear");
            if ("dependency".equalsIgnoreCase(type))
                edges.setAttribute("type", "4008");
            if ("realization".equalsIgnoreCase(type))
                edges.setAttribute("type", "4005");
            if ("usage".equalsIgnoreCase(type))
                edges.setAttribute("type", "4007");
            if ("abstraction".equalsIgnoreCase(type))
                edges.setAttribute("type", "4006");

            Element firstChildrenDecorationNode = getChildrenDecorationNodeUsage(type);

            if ("usage".equalsIgnoreCase(type)) {
                Element secondChildrenDecorationNode = getChildrenDecorationNodeUsage();
                Element layoutConstraint = getLayoutConstraint();
                firstChildrenDecorationNode.appendChild(layoutConstraint);
                secondChildrenDecorationNode.appendChild(layoutConstraint);
                edges.appendChild(secondChildrenDecorationNode);
            }

            Element layoutConstraint = getLayoutConstraint();
            firstChildrenDecorationNode.appendChild(layoutConstraint);
            edges.appendChild(firstChildrenDecorationNode);

            Node notationFileSource = findByIDInNotationFile(docNotation, clientElement);
            if (notationFileSource != null) {
                String idSource = notationFileSource.getAttributes().getNamedItem("xmi:id").getNodeValue();
                edges.setAttribute("source", idSource);
            }
            Node notationFileTarget = findByIDInNotationFile(docNotation, supplierElement);
            if (notationFileTarget != null) {
                String idTarget = notationFileTarget.getAttributes().getNamedItem("xmi:id").getNodeValue();
                edges.setAttribute("target", idTarget);
            }
            edges.setAttribute("lineColor", "0");

            Element element = getElementWithRelationships(type, edges);
            edges.appendChild(element);

            Element bendPoints = getBendPoints();
            edges.appendChild(bendPoints);

            node.appendChild(edges);
        } catch (Exception ignored) {
        }
    }

    private Element getChildrenDecorationNodeUsage(String type) {
        Element firstChildrenDecorationNode = this.docNotation.createElement("children");
        firstChildrenDecorationNode.setAttribute("xmi:type", "notation:DecorationNode");
        firstChildrenDecorationNode.setAttribute("xmi:id", UtilResources.getRandomUUID());
        if ("dependency".equalsIgnoreCase(type))
            firstChildrenDecorationNode.setAttribute("type", "6026");
        if ("realization".equalsIgnoreCase(type))
            firstChildrenDecorationNode.setAttribute("type", "6012");
        if ("usage".equalsIgnoreCase(type))
            firstChildrenDecorationNode.setAttribute("type", "6016");
        if ("abstraction".equalsIgnoreCase(type))
            firstChildrenDecorationNode.setAttribute("type", "6014");
        return firstChildrenDecorationNode;
    }

    private Element getChildrenDecorationNodeUsage() {
        Element childrenDecorationNode2 = this.docNotation.createElement("children");
        childrenDecorationNode2.setAttribute("xmi:type", "notation:DecorationNode");
        childrenDecorationNode2.setAttribute("xmi:id", UtilResources.getRandomUUID());
        childrenDecorationNode2.setAttribute("type", "6017"); // <usage>
        return childrenDecorationNode2;
    }

    private Element getLayoutConstraint() {
        Element layoutConstraint = this.docNotation.createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Location");
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandomUUID());
        layoutConstraint.setAttribute("y", "20");
        return layoutConstraint;
    }

    private Element getElementWithRelationships(String type, Element edges) {
        Element element = docNotation.createElement("element");
        if ("dependency".equalsIgnoreCase(type)) {
            element.setAttribute("xmi:type", "uml:Dependency");
            if (!this.stereotypes.isEmpty()) {
                Element eAnnotations = RelationshipsXMI.enableVisibleStereotypesWithBraces(this.docNotation, this.stereotypes);
                edges.appendChild(eAnnotations);
            }
            RelationshipsXMI.layoutConstraint(docNotation, edges);
        } else if ("usage".equalsIgnoreCase(type)) {
            element.setAttribute("xmi:type", "uml:Usage");
        } else if ("realization".equalsIgnoreCase(type)) {
            element.setAttribute("xmi:type", "uml:Realization");
        } else if ("abstraction".equals(type)) {
            element.setAttribute("xmi:type", "uml:Abstraction");
        }
        element.setAttribute("href", documentManager.getNewModelName() + ".uml#" + this.id);
        return element;
    }

    private Element getBendPoints() {
        Element bendPoints = docNotation.createElement("bendpoints");
        bendPoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
        bendPoints.setAttribute("xmi:id", UtilResources.getRandomUUID());
        bendPoints.setAttribute("points", "[0, 0, 476, 181]$[-467, -170, 9, 11]");
        return bendPoints;
    }


    private long random() {
        return Math.round(Math.random() * 100);
    }

    private void createDependencyInUmlFile(String dependency) {
        Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);
        String idDependency = UtilResources.getRandomUUID();
        this.id = idDependency;
        Element elementDependency = getElementDependency(dependency, idDependency);
        Element clientElement = (Element) findByID(docUml, this.clientElement, "packagedElement");
        if (clientElement != null)
            clientElement.setAttribute("clientDependency", idDependency);
        modelRoot.appendChild(elementDependency);
        if (this.stereotypes != null) {
            for (String nameSte : this.stereotypes)
                RelationshipsXMI.createStereotypeTagInUmlFile(docUml, nameSte, idDependency);
        }
    }

    private Element getElementDependency(String dependency, String idDependency) {
        Element elementDependency = this.docUml.createElement("packagedElement");
        if ("dependency".equalsIgnoreCase(dependency))
            elementDependency.setAttribute("xmi:type", "uml:Dependency");
        else if ("usage".equalsIgnoreCase(dependency))
            elementDependency.setAttribute("xmi:type", "uml:Usage");
        else if ("realization".equalsIgnoreCase(dependency))
            elementDependency.setAttribute("xmi:type", "uml:Realization");
        else if ("abstraction".equalsIgnoreCase(dependency))
            elementDependency.setAttribute("xmi:type", "uml:Abstraction");
        elementDependency.setAttribute("xmi:id", idDependency);
        elementDependency.setAttribute("name", this.name);
        elementDependency.setAttribute("client", this.clientElement);
        elementDependency.setAttribute("supplier", this.supplierElement);
        return elementDependency;
    }
}
