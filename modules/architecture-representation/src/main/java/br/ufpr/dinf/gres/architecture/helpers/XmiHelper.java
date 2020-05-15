package br.ufpr.dinf.gres.architecture.helpers;

import br.ufpr.dinf.gres.architecture.exceptions.NodeIdNotFound;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
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
import java.util.Random;
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

    /**
     * Busca por {@link Node} dado um id e um {@link Document}.
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
            return currentValue.substring(currentValue.indexOf("#") + 1);
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

    public static void setRecursiveOwnedComments(NamedElement modelElement, Element element) {
        EList<Comment> ownedComments = modelElement.getOwnedComments();
        if (element instanceof Package) {
            EList<org.eclipse.uml2.uml.Element> ownedElements = modelElement.getOwnedElements();
            for (org.eclipse.uml2.uml.Element ownedElement : ownedElements) {
                for (Comment ownedComment : ownedComments) {
                    Comment nownedComment = ownedElement.createOwnedComment();
                    nownedComment.setBody(ownedComment.getBody());
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


//            VariationPointFlyweight variationPointFlyweight = VariationPointFlyweight.getInstance();
//            variationPointFlyweight.setArchitecture(element.getArchitecture());
//            VariationPoint variationPoint = null;
//            try {
//                variationPoint = variationPointFlyweight.getOrCreateVariationPoint((ClassImpl) modelElement);
//            } catch (VariationPointElementTypeErrorException e) {
//                e.printStackTrace();
//            }
//            Stereotype varitionPointSte = StereotypeHelper.getStereotypeByName(modelElement, "variationPoint");
//            Variability variabilityMap = getVariabilityMap(variability, modelElement, element);
//            VariabilityStereotype variabilityStereotype = new VariabilityStereotype(variabilityMap);
//            variabilityMap.setVariationPoint(variationPoint);
        }
    }

    private static Variability getVariabilityMap(String variability, NamedElement modelElement, Element element) {
        String[] split = variability.split("\"");
        String name = null;
        String minSelection = null;
        String maxSelection = null;
        String bindingTime = "DESIGN_TIME";
        Boolean allowAddingVar = null;
        String variants = null;
        for (int i = 0; i < split.length; i++) {
            if (i < split.length - 1) {
                switch (split[i]) {
                    case " name=":
                        name = split[i + 1];
                        break;
                    case " minSelection=":
                        minSelection = split[i + 1];
                        break;
                    case " maxSelection=":
                        maxSelection = split[i + 1];
                        break;
                    case " allowAddingVar=":
                        allowAddingVar = Boolean.valueOf(split[i + 1]);
                        break;
                    case " variants=":
                        variants = split[i + 1];
                        break;
                    case " bindingTime=":
                        bindingTime = split[i + 1];
                        break;
                    default:
                }
            }
        }
        return new Variability(name, minSelection, maxSelection, bindingTime, allowAddingVar,
                element.getName(), XmiHelper.getXmiId(((ClassImpl) modelElement).getPackage()));
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

        String variabilityStr = collect.stream().filter(ln -> ln.contains(element.getId())).findFirst().orElse(null);
        if (variabilityStr == null) return null;

        Pattern compile = Pattern.compile("xmi\\:id\\=\\\".*\" ");
        Matcher matcher = compile.matcher(variabilityStr);
        String finded = null;
        if (matcher.find()) {
            finded = matcher.group(0).replace("xmi:id=", "");
        }
        String finalFinded = finded;
        String findedStr = collect.stream().filter(txt -> txt.contains("smarty:variability") && txt.contains(finalFinded))
                .findFirst().orElse(null);


        Pattern compile1 = Pattern.compile("xmi\\:id\\=\\\".*");
        Matcher matcher1 = compile1.matcher(findedStr);
        matcher1.find();
        return matcher1.group(0).replace("/>", "");
    }


    public String splitVariants(List<Variant> list) {
        return Joiner.on(", ").join(list);
    }
}