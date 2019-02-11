package arquitetura.touml;


import arquitetura.helpers.UtilResources;
import arquitetura.helpers.XmiHelper;
import arquitetura.representation.Architecture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class DependencyNode extends XmiHelper {

    private Document docUml;
    private Document docNotation;
    private DocumentManager documentManager;
    private String clientElement;
    private String supplierElement;
    private List<String> stereotypes = new ArrayList<String>();
    private String name;
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
        //Primeiramente cria o xmi necessário no document UML.

        try {
            createDependencyInUmlFile(type);

            Node node = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
            Element edges = this.docNotation.createElement("edges");
            edges.setAttribute("xmi:type", "notation:Connector");
            edges.setAttribute("xmi:id", UtilResources.getRandonUUID());
            if ("dependency".equalsIgnoreCase(type))
                edges.setAttribute("type", "4008");
            if ("realization".equalsIgnoreCase(type))
                edges.setAttribute("type", "4005");
            if ("usage".equalsIgnoreCase(type))
                edges.setAttribute("type", "4007");
            if ("abstraction".equalsIgnoreCase(type))
                edges.setAttribute("type", "4006");

            Element childrenDocorationnode1 = this.docNotation.createElement("children");
            childrenDocorationnode1.setAttribute("xmi:type", "notation:DecorationNode");
            childrenDocorationnode1.setAttribute("xmi:id", UtilResources.getRandonUUID());
            if ("dependency".equalsIgnoreCase(type))
                childrenDocorationnode1.setAttribute("type", "6026");
            if ("realization".equalsIgnoreCase(type))
                childrenDocorationnode1.setAttribute("type", "6012");
            if ("usage".equalsIgnoreCase(type))
                childrenDocorationnode1.setAttribute("type", "6016");
            if ("abstraction".equalsIgnoreCase(type))
                childrenDocorationnode1.setAttribute("type", "6014");

            if ("usage".equalsIgnoreCase(type)) {
                Element childrenDocorationnode2 = this.docNotation.createElement("children");
                childrenDocorationnode2.setAttribute("xmi:type", "notation:DecorationNode");
                childrenDocorationnode2.setAttribute("xmi:id", UtilResources.getRandonUUID());
                childrenDocorationnode2.setAttribute("type", "6017"); // <usage>

                Element layoutConstraint = this.docNotation.createElement("layoutConstraint");
                layoutConstraint.setAttribute("xmi:type", "notation:Location");
                layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
                layoutConstraint.setAttribute("y", "20");
                childrenDocorationnode1.appendChild(layoutConstraint);
                childrenDocorationnode2.appendChild(layoutConstraint);
                edges.appendChild(childrenDocorationnode2);
            }

            Element layoutConstraint = this.docNotation.createElement("layoutConstraint");
            layoutConstraint.setAttribute("xmi:type", "notation:Location");
            layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
            layoutConstraint.setAttribute("y", "20");
            childrenDocorationnode1.appendChild(layoutConstraint);
            edges.appendChild(childrenDocorationnode1);


            String idSource = findByIDInNotationFile(docNotation, clientElement).getAttributes().getNamedItem("xmi:id").getNodeValue();
            String idTarget = findByIDInNotationFile(docNotation, supplierElement).getAttributes().getNamedItem("xmi:id").getNodeValue();

            edges.setAttribute("source", idSource);
            edges.setAttribute("target", idTarget);
            edges.setAttribute("lineColor", "0");

            Element element = docNotation.createElement("element");
            if ("dependency".equalsIgnoreCase(type)) {
                element.setAttribute("xmi:type", "uml:Dependency");
                //Código para aparecer estereótipos no relacionamento
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
            edges.appendChild(element);


            Element bendpoints = docNotation.createElement("bendpoints");
            bendpoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
            bendpoints.setAttribute("xmi:id", UtilResources.getRandonUUID());
            bendpoints.setAttribute("points", "[0, 0, 476, 181]$[-467, -170, 9, 11]");
            edges.appendChild(bendpoints);

            node.appendChild(edges);

        } catch (Exception e) {
        }
    }

    private void createDependencyInUmlFile(String dependency) {
        Node modelRoot = this.docUml.getElementsByTagName("uml:Model").item(0);

        String idDependency = UtilResources.getRandonUUID();
        this.id = idDependency;

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

        //Adiciona dependency no element
        Element clientElement = (Element) findByID(docUml, this.clientElement, "packagedElement");
        clientElement.setAttribute("clientDependency", idDependency);

        modelRoot.appendChild(elementDependency);

        //Estereotipos
        if (this.stereotypes != null) {
            for (String nameSte : this.stereotypes)
                RelationshipsXMI.createStereotypeTagInUmlFile(docUml, nameSte, idDependency);
        }

    }

}
