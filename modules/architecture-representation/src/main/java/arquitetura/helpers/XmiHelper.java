package arquitetura.helpers;

import arquitetura.exceptions.NodeIdNotFound;
import arquitetura.representation.Element;
import arquitetura.representation.Variant;
import com.google.common.base.Joiner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class XmiHelper {

    static Logger LOGGER = LogManager.getLogger(XmiHelper.class.getName());


    private static Document originalNotation;

    /**
     * Busca por {@link Node} dado um id e um {@link Documnet}.
     *
     * @param docNotaion - Deve ser o arquivo .notation
     * @param id         - Id a ser buscado
     * @return {@link Node}
     */
    public static Node findByIDInNotationFile(Document docNotaion, String id) {
        NodeList node = docNotaion.getElementsByTagName("children");
        Node nodeFound = null;
        for (int i = 0; i < node.getLength(); i++) {
            NodeList nodes = node.item(i).getChildNodes();
            for (int j = 0; j < nodes.getLength(); j++) {
                if (nodes.item(j).getNodeName().equalsIgnoreCase("element")) {
                    NamedNodeMap attrs = nodes.item(j).getAttributes();
                    for (int k = 0; k < attrs.getLength(); k++) {
                        if (attrs.item(k).getNodeValue().contains(id)) {
                            nodeFound = node.item(i);
                        }
                    }
                }
            }
        }

        if (nodeFound == null) {
            LOGGER.warn("\nNode with id " + id + " cannot be found. Retuns null");
            return null;
        }
        return nodeFound;
    }


    public static String findIdByName(String name, List<Element> list) {
        for (Element element : list) {
            if (element.getName().equalsIgnoreCase(name))
                return element.getId();
        }
        return "";
    }

    public static Node findByID(Document doc, String id, String tagName) {
        NodeList node = doc.getElementsByTagName(tagName);
        for (int i = 0; i < node.getLength(); i++) {
            NamedNodeMap attributtes = node.item(i).getAttributes();
            for (int j = 0; j < attributtes.getLength(); j++)
                if (id.equalsIgnoreCase(attributtes.item(j).getNodeValue()))
                    return node.item(i);
        }

        return null;
    }

    /**
     * Retorna o Id de um dado {@link Node}.
     *
     * @param node
     * @return String
     * @throws NodeIdNotFound
     */
    public static String getIdForNode(Node node) throws NodeIdNotFound {
        if (node != null) {
            String nodeId = node.getAttributes().getNamedItem("xmi:id").getNodeValue();
            if (nodeId == null) throw new NodeIdNotFound("Cannot find id for node: " + node);
            return nodeId;
        } else {
            throw new NodeIdNotFound("Cannot find id for node: " + node);
        }
    }

    public static String isClassAbstract(boolean isAbstract) {
        return (isAbstract) ? "true" : "false";
    }

    public static String getOnlyIdOfXmiAttribute(NodeList elements, int i) {
        Node href = elements.item(i).getAttributes().getNamedItem("href");
        if (href != null) {
            String currentValue = href.getNodeValue();
            return currentValue.substring(currentValue.indexOf("#") + 1, currentValue.length());
        } else {
            return null;
        }
    }

    /**
     * Retorna o atributo xmi:id como uma <b>String</b> para um dado eObject.
     * Retrona <b>null</b>caso xmiResources for null.
     *
     * @param eObject
     * @return <b>String</b>
     */
    public static String getXmiId(EObject eObject) {
        Resource xmiResource = eObject.eResource();
        return ((XMLResource) xmiResource).getID(eObject);
    }

    private static String getXYValueForElement(Document docNotation, String elementId, String type) {
        Node x = XmiHelper.findByIDInNotationFile(docNotation, elementId);
        NodeList nodes = x.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeName().equals("layoutConstraint")) {
                if ("position".equals(type)) {
                    String xy = nodes.item(i).getAttributes().getNamedItem("x").getNodeValue() + ",";
                    xy += nodes.item(i).getAttributes().getNamedItem("y").getNodeValue();
                    return xy;
                } else if ("size".equals(type)) {
                    String xy = nodes.item(i).getAttributes().getNamedItem("width").getNodeValue() + ",";
                    xy += nodes.item(i).getAttributes().getNamedItem("height").getNodeValue();
                    return xy;
                }
            }

        }
        return null;
    }

    public static String getXValueForElement(String id) {
        return getXYValueForElement(getOriginalNotation(), id, "position").split(",")[0];
    }

    public static String getYValueForElement(String id) {
        return getXYValueForElement(getOriginalNotation(), id, "position").split(",")[1];
    }

    public static String getWidhtForPackage(String idPackage) {
        return getXYValueForElement(getOriginalNotation(), idPackage, "size").split(",")[0];
    }

    public static String getHeightForPackage(String idPackage) {
        return getXYValueForElement(getOriginalNotation(), idPackage, "size").split(",")[1];
    }

    public static void setNotationOriginalFile(String xmiFilePath) {
        String pathToNotation = xmiFilePath.substring(0, xmiFilePath.length() - 4) + ".notation";

        DocumentBuilderFactory docBuilderFactoryNotation = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilderNotation = null;
        try {
            docBuilderNotation = docBuilderFactoryNotation.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        try {
            setOriginalNotation(docBuilderNotation.parse(pathToNotation));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Document getOriginalNotation() {
        return originalNotation;
    }

    private static void setOriginalNotation(Document notation) {
        originalNotation = notation;
    }

    /**
     * @param document         - Document que deseja-se remover o nodo
     * @param nodeNameToRemove - nome do nodo
     * @param nodeIdToRemove   - id do nodo
     */
    public static void removeNode(Document document, String nodeNameToRemove, String nodeIdToRemove) {
        NodeList elements = document.getElementsByTagName(nodeNameToRemove);
        for (int i = 0; i < elements.getLength(); i++) {
            NamedNodeMap attrs = elements.item(i).getAttributes();
            for (int j = 0; j < attrs.getLength(); j++) {
                if (attrs.item(j).getNodeValue().equals(nodeIdToRemove)) {
                    Node toDelete = elements.item(i);
                    toDelete.getParentNode().removeChild(toDelete);
                }
            }
        }
    }

    /**
     * Retorna se o elemento é uma classe ou um comentário (note).
     * <p>
     * Caso não for encontrado retorna uma string vazia ("").
     *
     * @param id
     * @param umlDocument
     * @return
     */
    public String findTypeById(String id, Document umlDocument) {
        Node element = umlDocument.getElementsByTagName("uml:Model").item(0);

        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            String elementName = element.getChildNodes().item(i).getNodeName();
            String elementId = "";
            try {
                elementId = element.getChildNodes().item(i).getAttributes().getNamedItem("xmi:id").getNodeValue();
            } catch (Exception e) {
            }
            if ("packagedElement".equalsIgnoreCase(elementName)) {
                try {
                    String elementValue = element.getChildNodes().item(i).getAttributes().getNamedItem("xmi:type").getNodeValue();
                    if ("uml:Class".equalsIgnoreCase(elementValue) && (id.equalsIgnoreCase(elementId)))
                        return "class";
                } catch (NullPointerException e) {

                }
            } else {
                if ("ownedComment".equalsIgnoreCase(elementName) && (id.equalsIgnoreCase(elementId))) return "comment";
            }
        }
        return "";

    }

    /**
     * Método usado para gerar as posições de X e Y para os elementos.
     *
     * @return String
     */
    public String randomNum() {
        Random rn = new Random();
        int range = 1000 - 0 + 1;
        int randomNum = rn.nextInt(range) + 0;
        return Integer.toString(randomNum);
    }

    public String splitVariants(List<Variant> list) {
        return Joiner.on(", ").join(list);
    }
}