package br.otimizes.oplatool.architecture.papyrus.touml;


import br.otimizes.oplatool.architecture.exceptions.NullReferenceFoundException;
import br.otimizes.oplatool.architecture.helpers.Strings;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Variant;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Element XMI generator
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ElementXmiGenerator extends XmiHelper {

    private static final String METHOD_ID = "3013";
    private static final String METHOD_TYPE = "uml:Operation";
    private static final String LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE = "7018";
    private static final String LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE = "7017";
    private static final String PROPERTY_ID = "3012";
    private static final String PROPERTY_TYPE = "uml:Property";
    static Logger LOGGER = LogManager.getLogger(ElementXmiGenerator.class.getName());
    private Element element;
    private final DocumentManager documentManager;
    private final Node umlModelChild;
    private final Architecture a;
    private Node klass;
    private final ClassNotation notation;

    public ElementXmiGenerator(DocumentManager documentManager, Architecture a) {
        this.documentManager = documentManager;
        this.umlModelChild = documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        Node notationChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
        notation = new ClassNotation(this.documentManager, notationChildren);
        this.a = a;
    }

    public Node generateClass(final br.otimizes.oplatool.architecture.representation.Element clazz, final String idPackage) {
        br.otimizes.oplatool.architecture.papyrus.touml.Document.executeTransformation(documentManager, () -> {
            element = documentManager.getDocUml().createElement("packagedElement");
            element.setAttribute("xmi:type", "uml:Class");
            element.setAttribute("xmi:id", clazz.getId());
            element.setAttribute("name", clazz.getName());
            klass = element;

            try {
                notation.createXmiForClassInNotationFile(clazz.getId(), idPackage, "class", clazz);
                if ((idPackage != null) && !("".equals(idPackage))) {
                    Node packageToAppend = findByID(documentManager.getDocUml(), idPackage, "packagedElement");
                    if (packageToAppend != null) packageToAppend.appendChild(element);
                } else {
                    umlModelChild.appendChild(element);
                }
            } catch (NullReferenceFoundException e) {
                LOGGER.error("A null reference has been found. The process will be interrupted");
            }
        });
        return klass;
    }


    public void generateMethod(Method method, String idClass) {
        final Element ownedOperation = getOwnedOperation(method);
        for (Argument arg : method.getArguments()) {
            if (arg.getDirection().equals("in")) {

                if ((Types.isCustomType(arg.getType().getName()))) {
                    String id = findIdByName(arg.getType().getName(), a.getElements());
                    if ("".equals(id)) LOGGER.warn("Type " + arg.getType().getName() + " not found");
                    Element ownedParameter = getOwnedParameter(arg);
                    ownedParameter.setAttribute("type", id);
                    ownedOperation.appendChild(ownedParameter);
                } else {
                    Element ownedParameter = getOwnedParameter(arg);
                    Element typeOperation = getTypeOperation(arg);
                    ownedParameter.appendChild(typeOperation);
                    ownedOperation.appendChild(ownedParameter);
                }
            }
        }

        Element ownedParameterReturnType = getOwnedParameterReturnType();
        if (method.getReturnMethod() != null) {
            if (!method.getReturnMethod().equals("")) {
                Element typeOperationReturn = getTypeOperation(method);
                ownedParameterReturnType.appendChild(typeOperationReturn);
                ownedOperation.appendChild(ownedParameterReturnType);
            }
        }

        if (idClass != null) {
            final Node klassToAddMethod = findByID(documentManager.getDocUml(), idClass, "packagedElement");
            if (klassToAddMethod != null) klassToAddMethod.appendChild(ownedOperation);
            writeOnNotationFile(method.getId(), METHOD_ID, METHOD_TYPE, getNodeToAddMethodInNotationFile(idClass, LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE));
        } else {
            klass.appendChild(ownedOperation);
            writeOnNotationFile(method.getId(), METHOD_ID, METHOD_TYPE, null);
        }
    }

    private Element getOwnedOperation(Method method) {
        final Element ownedOperation = documentManager.getDocUml().createElement("ownedOperation");
        ownedOperation.setAttribute("name", method.getName());
        ownedOperation.setAttribute("xmi:id", method.getId());
        ownedOperation.setAttribute("isAbstract", method.isAbstract());
        ownedOperation.setAttribute("visibility", method.getVisibility());
        return ownedOperation;
    }

    private Element getOwnedParameter(Argument arg) {
        Element ownedParameter = documentManager.getDocUml().createElement("ownedParameter");
        ownedParameter.setAttribute("xmi:id", UtilResources.getRandomUUID());
        ownedParameter.setAttribute("name", arg.getName());
        ownedParameter.setAttribute("isUnique", "false");
        return ownedParameter;
    }

    private Element getTypeOperation(Argument arg) {
        Element typeOperation = documentManager.getDocUml().createElement("type");
        typeOperation.setAttribute("xmi:type", "uml:PrimitiveType");
        typeOperation.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#" + arg.getType().getName());
        return typeOperation;
    }

    private Element getOwnedParameterReturnType() {
        Element ownedParameterReturnType = documentManager.getDocUml().createElement("ownedParameter");
        ownedParameterReturnType.setAttribute("xmi:id", UtilResources.getRandomUUID());
        ownedParameterReturnType.setAttribute("name", "");
        ownedParameterReturnType.setAttribute("direction", "return");
        return ownedParameterReturnType;
    }

    private Element getTypeOperation(Method method) {
        Element typeOperationReturn = documentManager.getDocUml().createElement("type");
        typeOperationReturn.setAttribute("xmi:type", "uml:PrimitiveType");
        typeOperationReturn.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#" + method.getReturnMethod());
        return typeOperationReturn;
    }

    public void generateAttribute(Attribute attribute, String idClass) {
        if (idClass != null) {
            this.klass = findByID(documentManager.getDocUml(), idClass, "packagedElement");
            writeAttributeIntoUmlFile(attribute);
            if (attribute.isGenerateVisualAttribute())
                writeOnNotationFile(attribute.getId(), PROPERTY_ID, PROPERTY_TYPE, getNodeToAddMethodInNotationFile(idClass, LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE));
        } else {
            writeAttributeIntoUmlFile(attribute);
            if (attribute.isGenerateVisualAttribute())
                writeOnNotationFile(attribute.getId(), PROPERTY_ID, PROPERTY_TYPE, null);
        }

    }

    private void writeOnNotationFile(String idProperty, String typeId, String typeElement, Element appendTo) {
        notation.createNodeForElementType(idProperty, typeId, typeElement, appendTo);
    }

    private String writeAttributeIntoUmlFile(Attribute attribute) {
        Element ownedAttribute = getOwnedAttribute(attribute);
        klass.appendChild(ownedAttribute);

        if (attribute.getType() != null && Types.isCustomType(attribute.getType()) && !attribute.getType().equals("")) {
            String id = findIdByName(attribute.getType(), a.getElements());
            if ("".equals(id)) LOGGER.warn("Type " + attribute.getType() + " not found");
            ownedAttribute.setAttribute("type", id);
        } else {
            Element typeProperty = getTypeProperty(attribute);
            ownedAttribute.appendChild(typeProperty);
        }

        Element lowerValue = getLowerValue();
        ownedAttribute.appendChild(lowerValue);

        Element upperValue = getUpperValue();
        ownedAttribute.appendChild(upperValue);

        Element defaultValue = getDefaultValue();
        ownedAttribute.appendChild(defaultValue);

        Element value = getTrueValue();
        defaultValue.appendChild(value);
        return attribute.getId();
    }

    private Element getOwnedAttribute(Attribute attribute) {
        Element ownedAttribute = documentManager.getDocUml().createElement("ownedAttribute");
        ownedAttribute.setAttribute("xmi:id", attribute.getId());
        ownedAttribute.setAttribute("name", attribute.getName());
        ownedAttribute.setAttribute("visibility", attribute.getVisibility());
        ownedAttribute.setAttribute("isUnique", "false");
        return ownedAttribute;
    }

    private Element getTypeProperty(Attribute attribute) {
        Element typeProperty = documentManager.getDocUml().createElement("type");
        typeProperty.setAttribute("xmi:type", "uml:PrimitiveType");
        typeProperty.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#" + attribute.getType());
        return typeProperty;
    }

    private Element getLowerValue() {
        Element lowerValue = documentManager.getDocUml().createElement("lowerValue");
        lowerValue.setAttribute("xmi:type", "uml:LiteralInteger");
        lowerValue.setAttribute("xmi:id", UtilResources.getRandomUUID());
        lowerValue.setAttribute("value", "1");
        return lowerValue;
    }

    private Element getUpperValue() {
        Element upperValue = documentManager.getDocUml().createElement("upperValue");
        upperValue.setAttribute("xmi:type", "uml:LiteralUnlimitedNatural");
        upperValue.setAttribute("xmi:id", UtilResources.getRandomUUID());
        upperValue.setAttribute("value", "1");
        return upperValue;
    }

    private Element getDefaultValue() {
        Element defaultValue = documentManager.getDocUml().createElement("defaultValue");
        defaultValue.setAttribute("xmi:type", "uml:LiteralString");
        defaultValue.setAttribute("xmi:id", UtilResources.getRandomUUID());
        return defaultValue;
    }

    private Element getTrueValue() {
        Element value = documentManager.getDocUml().createElement("value");
        value.setAttribute("xmi:nil", "true");
        return value;
    }


    private Element getNodeToAddMethodInNotationFile(final String idClass, String location) {
        Node nodeNotationToAddMethod = findByIDInNotationFile(documentManager.getDocNotation(), idClass);
        if (nodeNotationToAddMethod != null) {
            for (int i = 0; i < nodeNotationToAddMethod.getChildNodes().getLength(); i++) {
                if ("children".equalsIgnoreCase(nodeNotationToAddMethod.getChildNodes().item(i).getNodeName())) {
                    if (isLocationToAddMethodInNotationFile(nodeNotationToAddMethod, i, location))
                        return (Element) nodeNotationToAddMethod.getChildNodes().item(i);
                }
            }
        }
        return null;
    }


    private boolean isLocationToAddMethodInNotationFile(Node nodeNotationToAddMethod, int i, String location) {
        return nodeNotationToAddMethod.getChildNodes().item(i).getAttributes().getNamedItem("type").getNodeValue().equals(location);
    }

    public String createEdgeAssociationOnNotationFile(Document docNotation, String newModelName, String client, String target, String idEdge) {
        String id = UtilResources.getRandomUUID();
        Node node = docNotation.getElementsByTagName("notation:Diagram").item(0);
        Node notationFileClient = findByIDInNotationFile(docNotation, client);
        Node notationFileTarget = findByIDInNotationFile(docNotation, target);
        if (notationFileClient != null && notationFileTarget != null) {
            NamedNodeMap attributesOwner = notationFileClient.getAttributes();
            NamedNodeMap attributesDestination = notationFileTarget.getAttributes();
            String idSource = attributesOwner.getNamedItem("xmi:id").getNodeValue();
            String idTarget = attributesDestination.getNamedItem("xmi:id").getNodeValue();

            Element edges = getEdges(docNotation, idSource, idTarget, id);

            Element childrenDecorationNode = getDecorationNode(docNotation);
            edges.appendChild(childrenDecorationNode);

            Element childrenDecorationNodeName = getChildrenDecorationNodeName(docNotation);
            edges.appendChild(childrenDecorationNodeName);

            Element layoutConstraintName = getConstraint(docNotation);
            childrenDecorationNodeName.appendChild(layoutConstraintName);

            Element layoutConstraint = getConstraint(docNotation);
            childrenDecorationNode.appendChild(layoutConstraint);

            Element childrenDecorationNode2 = getChildrenDecorationNode(docNotation);
            edges.appendChild(childrenDecorationNode2);

            Element layoutConstraint2 = getLayoutConstraint(docNotation);
            childrenDecorationNode2.appendChild(layoutConstraint2);

            Element elementAssociation = getElementAssociation(docNotation, newModelName, idEdge);
            edges.appendChild(elementAssociation);

            Element styles = getStyles(docNotation);
            edges.appendChild(styles);

            Element bendPoints = getBendPoints(docNotation);
            edges.appendChild(bendPoints);

            Element sourceAnchor = getSourceAnchor(docNotation);
            edges.appendChild(sourceAnchor);

            node.appendChild(edges);
        }
        return id;
    }

    private Element getEdges(Document docNotation, String idSource, String idTarget, String id) {
        Element edges = docNotation.createElement("edges");
        edges.setAttribute("xmi:type", "notation:Connector");
        edges.setAttribute("xmi:id", id);
        edges.setAttribute("type", "4001");
        edges.setAttribute("source", idSource);
        edges.setAttribute("target", idTarget);
        edges.setAttribute("routing", "Rectilinear");
        edges.setAttribute("lineColor", "0");
        return edges;
    }

    private Element getDecorationNode(Document docNotation) {
        Element childrenDecorationNode = docNotation.createElement("children");
        childrenDecorationNode.setAttribute("xmi:type", "notation:DecorationNode");
        childrenDecorationNode.setAttribute("xmi:id", UtilResources.getRandomUUID());
        childrenDecorationNode.setAttribute("type", "6033");
        return childrenDecorationNode;
    }

    private Element getChildrenDecorationNodeName(Document docNotation) {
        Element childrenDecorationNodeName = docNotation.createElement("children");
        childrenDecorationNodeName.setAttribute("xmi:type", "notation:DecorationNode");
        childrenDecorationNodeName.setAttribute("xmi:id", UtilResources.getRandomUUID());
        childrenDecorationNodeName.setAttribute("type", "6002");
        return childrenDecorationNodeName;
    }

    private Element getConstraint(Document docNotation) {
        Element layoutConstraint = docNotation.createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Location");
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandomUUID());
        layoutConstraint.setAttribute("y", "20");
        return layoutConstraint;
    }

    private Element getChildrenDecorationNode(Document docNotation) {
        Element childrenDecorationNode2 = docNotation.createElement("children");
        childrenDecorationNode2.setAttribute("xmi:type", "notation:DecorationNode");
        childrenDecorationNode2.setAttribute("xmi:id", UtilResources.getRandomUUID());
        childrenDecorationNode2.setAttribute("type", "6034");
        return childrenDecorationNode2;
    }

    private Element getLayoutConstraint(Document docNotation) {
        Element layoutConstraint2 = docNotation.createElement("layoutConstraint");
        layoutConstraint2.setAttribute("xmi:type", "notation:Location");
        layoutConstraint2.setAttribute("xmi:id", UtilResources.getRandomUUID());
        layoutConstraint2.setAttribute("y", "-20");
        return layoutConstraint2;
    }

    private Element getElementAssociation(Document docNotation, String newModelName, String idEdge) {
        Element elementAssociation = docNotation.createElement("element");
        elementAssociation.setAttribute("xmi:type", "uml:Association");
        elementAssociation.setAttribute("href", newModelName + ".uml#" + idEdge);
        return elementAssociation;
    }

    private Element getStyles(Document docNotation) {
        Element styles = docNotation.createElement("styles");
        styles.setAttribute("xmi:type", "notation:FontStyle");
        styles.setAttribute("xmi:id", UtilResources.getRandomUUID());
        styles.setAttribute("fontName", "Lucida Grande");
        styles.setAttribute("fontHeight", "11");
        return styles;
    }

    private Element getBendPoints(Document docNotation) {
        Element bendPoints = docNotation.createElement("bendpoints");
        bendPoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
        bendPoints.setAttribute("xmi:id", UtilResources.getRandomUUID());
        bendPoints.setAttribute("points", "[0, 0, -200, -20]$[255, -30, -6, -50]");
        return bendPoints;
    }

    private Element getSourceAnchor(Document docNotation) {
        Element sourceAnchor = docNotation.createElement("sourceAnchor");
        sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
        sourceAnchor.setAttribute("xmi:id", UtilResources.getRandomUUID());
        sourceAnchor.setAttribute("id", "(1.0,0.36)");
        return sourceAnchor;
    }

    public void createStereotype(Variant variant, String idClass) {
        addStereotypeToUmlFile(variant, idClass);
        notation.createXmiForStereotype(variant.getVariantType(), idClass, "smarty");
    }

    private void addStereotypeToUmlFile(Variant variant, String idClass) {
        Node nodeXmi = this.documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        Element stereotype = this.documentManager.getDocUml().createElement("smarty:" + variant.getVariantType());
        stereotype.setAttribute("xmi:id", UtilResources.getRandomUUID());
        stereotype.setAttribute("base_Class", idClass); // A classe que tem o estereotype
        stereotype.setAttribute("rootVP", variant.getRootVP());
        stereotype.setAttribute("variabilities", Strings.splitVariabilities(variant.getVariabilities()));
        nodeXmi.getParentNode().appendChild(stereotype);
    }

    public void createStereotypeVariationPoint(String idClass, String variants, String variabilities, String dESIGN_TIME) {
        Node nodeXmi = this.documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        Element stereotype = this.documentManager.getDocUml().createElement("smarty:variationPoint");
        stereotype.setAttribute("xmi:id", UtilResources.getRandomUUID());
        stereotype.setAttribute("base_Class", idClass); // A classe que tem o estereotype
        stereotype.setAttribute("variants", variants);
        stereotype.setAttribute("variabilities", variabilities);
        stereotype.setAttribute("bindingTime", dESIGN_TIME);
        stereotype.setAttribute("numberOfVariants", String.valueOf(variants.split(",").length));
        nodeXmi.getParentNode().appendChild(stereotype);
        notation.createXmiForStereotype("variationPoint", idClass, "smarty");
    }

    public void createStereotypeVariability(String idNote, VariabilityStereotype a) {
        Node nodeXmi = this.documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        Element stereotype = this.documentManager.getDocUml().createElement("smarty:" + a.getStereotypeName());
        stereotype.setAttribute("xmi:id", UtilResources.getRandomUUID());

        String type = findTypeById(idNote, documentManager.getDocUml());

        if ("class".equalsIgnoreCase(type))
            stereotype.setAttribute("base_Class", idNote); // A classe que tem o estereotype
        else if ("comment".equalsIgnoreCase(type))
            stereotype.setAttribute("base_Comment", idNote); // A classe que tem o estereotype
        stereotype.setAttribute("name", a.getName());
        stereotype.setAttribute("minSelection", a.getMinSelection());
        stereotype.setAttribute("maxSelection", a.getMaxSelection());
        stereotype.setAttribute("variants", a.getVariants());
        stereotype.setAttribute("bindingTime", a.getBindingTime());

        nodeXmi.getParentNode().appendChild(stereotype);

        notation.createXmiForStereotype(a.getStereotypeName(), idNote, "smarty");
    }

    private void createConcern(String name, String idClass, String type) {
        Node nodeXmi = this.documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        Element stereotype = this.documentManager.getDocUml().createElement(type + ":" + name);
        stereotype.setAttribute("xmi:id", UtilResources.getRandomUUID());
        stereotype.setAttribute("base_Class", idClass);
        nodeXmi.getParentNode().appendChild(stereotype);
        notation.createXmiForStereotype(name, idClass, type);
    }

    public void interfaceStereotype(String idClass) {
        Node nodeXmi = this.documentManager.getDocUml().getElementsByTagName("uml:Model").item(0);
        Element stereotype = this.documentManager.getDocUml().createElement("smarty:interface");
        stereotype.setAttribute("xmi:id", UtilResources.getRandomUUID());
        stereotype.setAttribute("base_Class", idClass);
        nodeXmi.getParentNode().appendChild(stereotype);
        notation.createXmiForStereotype("interface", idClass, "smarty");
    }

    public void generateConcern(final String concern, final String idElement, final String type) {
        br.otimizes.oplatool.architecture.papyrus.touml.Document.executeTransformation(documentManager,
                () -> createConcern(concern, idElement, type));
    }

    public String createEdgeAssociationClassOnNotationFile(String idChildren, String idEdge) {
        Node node = this.documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
        String id = UtilResources.getRandomUUID();
        Element edges = getEdges(idChildren, idEdge, id);

        Element styles = getStyles(id);
        Element bendPoints = getBendPoints(id);
        edges.appendChild(bendPoints);

        Element sourceAnchor = getSourceAnchor(id);
        edges.appendChild(sourceAnchor);

        edges.appendChild(styles);
        edges.appendChild(sourceAnchor);
        node.appendChild(edges);

        return id;
    }

    private Element getEdges(String idChildren, String idEdge, String id) {
        Element edges = this.documentManager.getDocNotation().createElement("edges");
        edges.setAttribute("xmi:type", "notation:Connector");
        edges.setAttribute("xmi:id", id);
        edges.setAttribute("type", "4016");
        edges.setAttribute("source", idEdge);
        edges.setAttribute("target", idChildren);
        edges.setAttribute("routing", "Rectilinear");
        edges.setAttribute("lineColor", "0");
        return edges;
    }

    private Element getStyles(String id) {
        Element styles = this.documentManager.getDocNotation().createElement("styles");
        styles.setAttribute("xmi:type", "notation:FontStyle");
        styles.setAttribute("xmi:id", id);
        styles.setAttribute("fontName", "Lucida Grande");
        styles.setAttribute("fontHeight", "11");
        return styles;
    }

    private Element getBendPoints(String id) {
        Element bendPoints = this.documentManager.getDocNotation().createElement("bendpoints");
        bendPoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
        bendPoints.setAttribute("xmi:id", id);
        bendPoints.setAttribute("points", "[-50, -50, 0, 0]$[-50, -50, 0, 0]");
        return bendPoints;
    }

    private Element getSourceAnchor(String id) {
        Element sourceAnchor = this.documentManager.getDocNotation().createElement("sourceAnchor");
        sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
        sourceAnchor.setAttribute("xmi:id", id);
        sourceAnchor.setAttribute("id", "(1.0,0.36)");
        return sourceAnchor;
    }
}