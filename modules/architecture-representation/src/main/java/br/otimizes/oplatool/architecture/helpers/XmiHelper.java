package br.otimizes.oplatool.architecture.helpers;

import br.otimizes.oplatool.architecture.exceptions.NodeIdNotFound;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;
import com.google.common.base.Joiner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.OperationImpl;
import org.eclipse.uml2.uml.internal.impl.PropertyImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * XMI utils
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class XmiHelper {

    static Logger LOGGER = LogManager.getLogger(XmiHelper.class.getName());
    private static Document originalNotation;

    public static Node findByIDInNotationFile(Document docNotation, String id) {
        NodeList node = docNotation.getElementsByTagName("children");
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
            NamedNodeMap attributes = node.item(i).getAttributes();
            for (int j = 0; j < attributes.getLength(); j++)
                if (id.equalsIgnoreCase(attributes.item(j).getNodeValue()))
                    return node.item(i);
        }
        return null;
    }

    public static String getIdForNode(Node node) throws NodeIdNotFound {
        if (node != null) {
            String nodeId = node.getAttributes().getNamedItem("xmi:id").getNodeValue();
            if (nodeId == null) throw new NodeIdNotFound("Cannot find id for node: " + node);
            return nodeId;
        } else {
            throw new NodeIdNotFound("Cannot find id for node");
        }
    }

    public static String isClassAbstract(boolean isAbstract) {
        return (isAbstract) ? "true" : "false";
    }

    public static String getOnlyIdOfXmiAttribute(NodeList elements, int i) {
        Node href = elements.item(i).getAttributes().getNamedItem("href");
        if (href != null) {
            String currentValue = href.getNodeValue();
            return currentValue.substring(currentValue.indexOf("#") + 1);
        } else {
            return null;
        }
    }

    public static String getXmiId(EObject eObject) {
        Resource xmiResource = eObject.eResource();
        return ((XMLResource) xmiResource).getID(eObject);
    }

    private static String getXYValueForElement(Document docNotation, String elementId, String type) {
        Node x = XmiHelper.findByIDInNotationFile(docNotation, elementId);
        if (x != null) {
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
        }
        return null;
    }

    public static String getXValueForElement(String id) {
        return Objects.requireNonNull(getXYValueForElement(getOriginalNotation(), id, "position")).split(",")[0];
    }

    public static String getYValueForElement(String id) {
        return Objects.requireNonNull(getXYValueForElement(getOriginalNotation(), id, "position")).split(",")[1];
    }

    public static String getWidhtForPackage(String idPackage) {
        return Objects.requireNonNull(getXYValueForElement(getOriginalNotation(), idPackage, "size")).split(",")[0];
    }

    public static String getHeightForPackage(String idPackage) {
        return Objects.requireNonNull(getXYValueForElement(getOriginalNotation(), idPackage, "size")).split(",")[1];
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
            if (docBuilderNotation != null)
                setOriginalNotation(docBuilderNotation.parse(pathToNotation));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Document getOriginalNotation() {
        return originalNotation;
    }

    private static void setOriginalNotation(Document notation) {
        originalNotation = notation;
    }

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

    public String findTypeById(String id, Document umlDocument) {
        Node element = umlDocument.getElementsByTagName("uml:Model").item(0);
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            String elementName = element.getChildNodes().item(i).getNodeName();
            String elementId = "";
            try {
                elementId = element.getChildNodes().item(i).getAttributes().getNamedItem("xmi:id").getNodeValue();
            } catch (Exception ignored) {
            }
            if ("packagedElement".equalsIgnoreCase(elementName)) {
                try {
                    String elementValue = element.getChildNodes().item(i).getAttributes().getNamedItem("xmi:type").getNodeValue();
                    if ("uml:Class".equalsIgnoreCase(elementValue) && (id.equalsIgnoreCase(elementId)))
                        return "class";
                } catch (NullPointerException ignored) {

                }
            } else {
                if ("ownedComment".equalsIgnoreCase(elementName) && (id.equalsIgnoreCase(elementId))) return "comment";
            }
        }
        return "";
    }

    public static void setRecursiveOwnedComments(NamedElement modelElement, Element element) {
        EList<Comment> ownedComments = modelElement.getOwnedComments();
        if (element instanceof Package) {
            EList<org.eclipse.uml2.uml.Element> ownedElements = modelElement.getOwnedElements();
            for (org.eclipse.uml2.uml.Element ownedElement : ownedElements) {
                for (Comment ownedComment : ownedComments) {
                    Comment newOwnedComment = ownedElement.createOwnedComment();
                    newOwnedComment.setBody(ownedComment.getBody());
                }
            }

        }
        if (element instanceof Class || element instanceof Interface) {
            EList<Comment> ownedCommentsPackage = ((ClassImpl) modelElement).getPackage().getOwnedComments();
            ownedComments.addAll(ownedCommentsPackage);
            applyAnotherComments(modelElement, element, ownedComments);
        }
        if (element instanceof Attribute) {
            EList<Comment> ownedCommentsPackage = ((PropertyImpl) modelElement).getClass_().getOwnedComments();
            ownedComments.addAll(ownedCommentsPackage);
        }

        if (element instanceof Method) {
            EList<Comment> ownedCommentsPackage = ((OperationImpl) modelElement).getClass_().getOwnedComments();
            ownedComments.addAll(ownedCommentsPackage);
        }
        for (Comment ownedComment : ownedComments) {
            element.setComments(element.getComments() + "\n" + ownedComment.getBody());
        }
    }

    private static void applyAnotherComments(NamedElement modelElement, Element element, EList<Comment> ownedComments) {
        String variability = getVariabilityXmiLine(modelElement, element);
        if (variability != null) {

            Comment ownedComment = modelElement.createOwnedComment();
            ownedComment.setBody(variability);
            ownedComments.add(ownedComment);
            ((ClassImpl) modelElement).getPackage().createOwnedComment();
        }
    }

    private static String getVariabilityXmiLine(NamedElement modelElement, Element element) {
        URI uri = modelElement.eContainer().eResource().getURI();
        Path path = Paths.get(uri.toFileString());
        List<String> collect = null;
        try {
            collect = Files.lines(path).filter(txt -> txt.contains("smarty:variability") || txt.contains("<ownedComment")).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (collect != null) {
            String variabilityStr = collect.stream().filter(ln -> ln.contains(element.getId())).findFirst().orElse(null);
            if (variabilityStr == null) return null;

            Pattern compile = Pattern.compile("xmi\\:id\\=\\\".*\" ");
            Matcher matcher = compile.matcher(variabilityStr);
            String finded = null;
            if (matcher.find()) {
                finded = matcher.group(0).replace("xmi:id=", "");
            }
            String finalFound = finded;
            String foundStr = collect.stream().filter(txt -> txt.contains("smarty:variability") && txt.contains(finalFound))
                    .findFirst().orElse(null);


            Pattern compile1 = Pattern.compile("xmi\\:id\\=\\\".*");
            Matcher matcher1 = compile1.matcher(foundStr);
            matcher1.find();
            return matcher1.group(0).replace("/>", "");
        }
        return null;
    }

    public String splitVariants(List<Variant> list) {
        return Joiner.on(", ").join(list);
    }
}