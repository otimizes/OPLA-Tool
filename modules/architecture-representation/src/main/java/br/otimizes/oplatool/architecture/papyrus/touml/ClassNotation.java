package br.otimizes.oplatool.architecture.papyrus.touml;


import br.otimizes.oplatool.architecture.exceptions.NullReferenceFoundException;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class notation
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ClassNotation extends XmiHelper {
    private static final String SHOW_PROPERTY_TYPE = "7066";
    private static final String LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE = "7017";
    private static final String LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE = "7018";
    private static final String TYPE_CLASS = "2008";
    private static final String TYPE_PACKAGE = "2007";
    private final String fontName = "Lucida Grande";
    private final String fontHeight = "11";
    private final String lineColor = "0";
    private final String xmiType = "notation:Shape";
    private Node notationChildren;
    private Element notationBasicProperty;
    private final DocumentManager documentManager;
    public static int xElement = 1;
    public static int yElement = 1;
    public static int xPackage = 1;
    public static int yPackage = 10;
    public static String lastNamespace = "";

    public static void clearConfigurations() {
        xElement = 1;
        yElement = 1;
        xPackage = 1;
        yPackage = 10;
        lastNamespace = "";
    }

    public ClassNotation(DocumentManager documentManager, Node notationChildren) {
        this.documentManager = documentManager;
        this.notationChildren = notationChildren;
        this.notationChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
    }

    public void createNodeForElementType(String idProperty, String type, String typeElement, Element appendTo) {
        Element node = getNode(type);
        Element eAnnotations = getEAnnotations();
        Element details = getDetails();
        eAnnotations.appendChild(details);
        node.appendChild(eAnnotations);

        Element element = getElement(idProperty, typeElement);
        node.appendChild(element);

        Element layoutConstraint = documentManager.getDocNotation().createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Location");
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandomUUID());
        node.appendChild(layoutConstraint);

        if (appendTo != null)
            appendTo.appendChild(node);
        else
            notationBasicProperty.appendChild(node);
    }

    private Element getNode(String type) {
        Element node = documentManager.getDocNotation().createElement("children");
        node.setAttribute("xmi:type", this.xmiType);
        node.setAttribute("xmi:id", UtilResources.getRandomUUID());
        node.setAttribute("type", type);
        node.setAttribute("fontName", this.fontName);
        node.setAttribute("fontHeight", this.fontHeight);
        node.setAttribute("lineColor", this.lineColor);
        return node;
    }

    private Element getEAnnotations() {
        Element eAnnotations = documentManager.getDocNotation().createElement("eAnnotations");
        eAnnotations.setAttribute("xmi:type", "ecore:EAnnotation");
        eAnnotations.setAttribute("source", "CustomAppearance_Annotation");
        eAnnotations.setAttribute("xmi:id", UtilResources.getRandomUUID());
        return eAnnotations;
    }

    private Element getDetails() {
        Element details = documentManager.getDocNotation().createElement("details");
        details.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details.setAttribute("xmi:id", UtilResources.getRandomUUID());
        details.setAttribute("key", "CustomAppearance_MaskValue");
        details.setAttribute("value", SHOW_PROPERTY_TYPE);
        return details;
    }

    private Element getElement(String idProperty, String typeElement) {
        Element element = documentManager.getDocNotation().createElement("element");
        element.setAttribute("xmi:type", typeElement);
        element.setAttribute("href", documentManager.getModelName() + "#" + idProperty);
        return element;
    }

    public String createXmiForClassInNotationFile(String id, String idPackage, String type, br.otimizes.oplatool.architecture.representation.Element aClass) throws NullReferenceFoundException {
        String idChildren = UtilResources.getRandomUUID();
        Element childrenNode = getChildrenNode(idChildren);

        Element notationDecorationNode = getNotationDecorationNode();
        childrenNode.appendChild(notationDecorationNode);

        Element klass = getClass(id, type);

        this.notationBasicProperty = createChildrenComportment(documentManager.getDocNotation(), childrenNode, LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE, aClass);
        createChildrenComportment(documentManager.getDocNotation(), childrenNode, LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE, aClass);
        childrenNode.appendChild(klass);

        if ((idPackage != null) && !(idPackage.isEmpty())) {
            Node nodeToAppend = findByIDInNotationFile(documentManager.getDocNotation(), idPackage);
            if (nodeToAppend != null)
                nodeToAppend.appendChild(childrenNode);
        } else {
            notationChildren.appendChild(childrenNode);
        }
        return idChildren;
    }

    private Element getChildrenNode(String idChildren) {
        Element node = documentManager.getDocNotation().createElement("children");
        node.setAttribute("xmi:type", this.xmiType);
        node.setAttribute("xmi:id", idChildren);
        node.setAttribute("type", TYPE_CLASS);
        node.setAttribute("fontName", this.fontName);
        node.setAttribute("fontHeight", this.fontHeight);
        node.setAttribute("lineColor", this.lineColor);
        return node;
    }

    private Element getNotationDecorationNode() {
        Element notationDecorationNode = documentManager.getDocNotation().createElement("children");
        notationDecorationNode.setAttribute("xmi:type", "notation:DecorationNode");
        notationDecorationNode.setAttribute("xmi:id", UtilResources.getRandomUUID());
        notationDecorationNode.setAttribute("type", "5029");
        return notationDecorationNode;
    }

    private Element getClass(String id, String type) throws NullReferenceFoundException {
        Element klass = documentManager.getDocNotation().createElement("element");
        if (id == null) {
            throw new NullReferenceFoundException("A null reference found when try access attribute id. Executation will be interrupted.");
        }
        klass.setAttribute("href", documentManager.getModelName() + ".uml#" + id);
        if ("associationClass".equalsIgnoreCase(type))
            klass.setAttribute("xmi:type", "uml:AssociationClass");
        else {
            klass.setAttribute("xmi:type", "uml:Class");
        }
        return klass;
    }


    private Element createChildrenComportment(Document doc, Element node, String type, br.otimizes.oplatool.architecture.representation.Element aClass) {
        Element childrenElement = getChildrenElement(doc, type);
        node.appendChild(childrenElement);

        Element notationTitleStyle = getNotationTitleStyle(doc);
        childrenElement.appendChild(notationTitleStyle);

        Element notationSortingStyle = getNotationSortingStyle(doc);
        childrenElement.appendChild(notationSortingStyle);

        Element notationFilteringStyle = getNotationFilteringStyle(doc);
        childrenElement.appendChild(notationFilteringStyle);

        Element layoutConstraint = getLayoutConstraint(doc, aClass);

        if (!lastNamespace.equals(aClass.getNamespace())) {
            lastNamespace = aClass.getNamespace();
            if (aClass instanceof Class) {
                yElement = 1;
            } else {
                yElement = 810;
            }
            xElement = 1;
        } else {
            yElement += getHeightClass(aClass) * 2;
            if (aClass instanceof Class && yElement >= 800) {
                xElement += 600;
                yElement = 1;
            }
            if (aClass instanceof Interface && yElement >= 1500) {
                xElement += 600;
                yElement = 810;
            }
        }
        node.appendChild(layoutConstraint);

        return childrenElement;
    }

    private Element getChildrenElement(Document doc, String type) {
        Element element = doc.createElement("children");
        element.setAttribute("xmi:type", "notation:BasicCompartment");
        element.setAttribute("xmi:id", UtilResources.getRandomUUID());
        element.setAttribute("type", type);
        return element;
    }

    private Element getNotationTitleStyle(Document doc) {
        Element notationTitleStyle = doc.createElement("styles");
        notationTitleStyle.setAttribute("xmi:type", "notation:TitleStyle");
        notationTitleStyle.setAttribute("xmi:id", UtilResources.getRandomUUID());
        return notationTitleStyle;
    }

    private Element getNotationSortingStyle(Document doc) {
        Element notationSortingStyle = doc.createElement("styles");
        notationSortingStyle.setAttribute("xmi:type", "notation:SortingStyle");
        notationSortingStyle.setAttribute("xmi:id", UtilResources.getRandomUUID());
        return notationSortingStyle;
    }

    private Element getNotationFilteringStyle(Document doc) {
        Element notationFilteringStyle = doc.createElement("styles");
        notationFilteringStyle.setAttribute("xmi:type", "notation:FilteringStyle");
        notationFilteringStyle.setAttribute("xmi:id", UtilResources.getRandomUUID());
        return notationFilteringStyle;
    }

    private Element getLayoutConstraint(Document doc, br.otimizes.oplatool.architecture.representation.Element aClass) {
        Element layoutConstraint = doc.createElement("layoutConstraint");
        layoutConstraint.setAttribute("x", String.valueOf(xElement));
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandomUUID());
        layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
        layoutConstraint.setAttribute("y", String.valueOf(yElement));
        layoutConstraint.setAttribute("additionalInfo", aClass.getNamespace() + ":" + aClass.getName());
        return layoutConstraint;
    }

    private int getHeightClass(br.otimizes.oplatool.architecture.representation.Element aClass) {
        int YClass = 100;
        if (aClass instanceof Class) {
            for (br.otimizes.oplatool.architecture.representation.Attribute ignored : ((Class) aClass).getAllAttributes()) {
                YClass += 10;
            }
            for (br.otimizes.oplatool.architecture.representation.Method ignored : ((Class) aClass).getAllMethods()) {
                YClass += 10;
            }
        } else {
            for (br.otimizes.oplatool.architecture.representation.Method ignored : ((Interface) aClass).getMethods()) {
                YClass += 10;
            }
        }
        return YClass;
    }

    private int getWidthClass(br.otimizes.oplatool.architecture.representation.Element aClass) {
        int XClass = 1;
        int i = 0;
        int[] XClasss = new int[aClass instanceof Class ? ((Class) aClass).getAllAttributes().size()
                + ((Class) aClass).getAllMethods().size() : ((Interface) aClass).getMethods().size()];
        if (aClass instanceof Class) {
            for (br.otimizes.oplatool.architecture.representation.Attribute allAttribute : ((Class) aClass).getAllAttributes()) {
                XClasss[i] += (allAttribute.getType().length() + allAttribute.getAllConcerns().toString().length()
                        + allAttribute.getVisibility().length() + allAttribute.getName().length());
            }
            for (br.otimizes.oplatool.architecture.representation.Method method : ((Class) aClass).getAllMethods()) {
                XClasss[i] += (method.getParameters().toString().length() + method.getAllConcerns().toString().length()
                        + method.getReturnType().length() + method.getName().length());
            }
        } else {
            for (br.otimizes.oplatool.architecture.representation.Method operation : ((Interface) aClass).getMethods()) {
                XClasss[i] += (operation.toString().length() + operation.getName().length() + operation.getAllConcerns()
                        .toString().length() + operation.getTypeElement().length());
            }
        }

        for (int classes : XClasss) {
            if (classes > XClass) XClass = classes;
        }
        return XClass;
    }

    public void createXmiForPackageInNotationFile(String id, Package original) {
        Element nodeChildren = getNode(TYPE_PACKAGE);

        Element childrenDecorationNode5026 = createChildrenDecorationnode("5026");
        Element childrenDecorationNode7016 = createChildrenDecorationnode("7016");

        Element layoutConstraint = documentManager.getDocNotation().createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandomUUID());

        Element styles = getNotationTitleStyle(documentManager.getDocNotation());
        childrenDecorationNode7016.appendChild(styles);
        childrenDecorationNode7016.appendChild(layoutConstraint);

        nodeChildren.appendChild(childrenDecorationNode5026);
        nodeChildren.appendChild(childrenDecorationNode7016);

        Element elementPackage = getElement(id, "uml:Package");
        nodeChildren.appendChild(elementPackage);

        Element layoutConstraint2 = getLayoutConstraint(original);
        xPackage += original.getElements().size() * 300 + 50;
        int height = 1650;
        layoutConstraint2.setAttribute("height", String.valueOf(height));
        if (xPackage >= 6000) {
            xPackage = 1;
            yPackage += height + 50;
        }
        nodeChildren.appendChild(layoutConstraint2);
        notationChildren.appendChild(nodeChildren);

    }

    private Element getLayoutConstraint(Package original) {
        Element layoutConstraint2 = documentManager.getDocNotation().createElement("layoutConstraint");
        layoutConstraint2.setAttribute("x", String.valueOf(xPackage));
        layoutConstraint2.setAttribute("xmi:id", UtilResources.getRandomUUID());
        layoutConstraint2.setAttribute("xmi:type", "notation:Bounds");
        layoutConstraint2.setAttribute("y", String.valueOf(yPackage));
        layoutConstraint2.setAttribute("width", String.valueOf(original.getElements().size() * 300));
        return layoutConstraint2;
    }

    private Element createChildrenDecorationnode(String type) {
        Element children = documentManager.getDocNotation().createElement("children");
        children.setAttribute("xmi:type", "notation:DecorationNode");
        children.setAttribute("xmi:id", UtilResources.getRandomUUID());
        children.setAttribute("type", type);
        return children;
    }

    public void createXmiForStereotype(String name, String idClass, String perfilType) {
        Node node = getXmiToAppendStereotype(idClass);
        if (node != null) {
            Node valueAttr = node.getAttributes().getNamedItem("value");
            String oldValue = valueAttr.getNodeValue().trim();
            if ("smarty".equalsIgnoreCase(perfilType))
                valueAttr.setNodeValue(oldValue + ",smarty::" + name);
            else if ("concerns".equalsIgnoreCase(perfilType))
                valueAttr.setNodeValue(oldValue + ",concerns::" + name);
            else if ("br.otimizes.oplatool.patterns".equalsIgnoreCase(perfilType))
                valueAttr.setNodeValue(oldValue + ",br.otimizes.oplatool.patterns::" + name);
        } else {
            ste(name, idClass, false, perfilType);
            ste(name, idClass, true, perfilType);
        }
    }

    private Node getXmiToAppendStereotype(String idClass) {
        Node node = findByIDInNotationFile(documentManager.getDocNotation(), idClass);
        if (node != null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if ("eAnnotations".equalsIgnoreCase(children.item(i).getNodeName())) {
                    String nodeValue = children.item(i).getAttributes().getNamedItem("source").getNodeValue();
                    if ("Stereotype_Annotation".equalsIgnoreCase(nodeValue)) {
                        NodeList eAnnotationsChildreen = children.item(i).getChildNodes();
                        for (int j = 0; j < eAnnotationsChildreen.getLength(); j++) {
                            String keyValue = eAnnotationsChildreen.item(j).getAttributes().getNamedItem("key").getNodeValue();
                            if ("StereotypeList".equalsIgnoreCase(keyValue)) {
                                return eAnnotationsChildreen.item(j);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void ste(String name, String idClass, boolean addEcorePrefix, String profileType) {
        Node classToAddSte = findByIDInNotationFile(documentManager.getDocNotation(), idClass);

        Element eAnnotations = documentManager.getDocNotation().createElement("eAnnotations");
        eAnnotations.setAttribute("xmi:type", "ecore:EAnnotation");
        eAnnotations.setAttribute("xmi:id", UtilResources.getRandomUUID());

        if (addEcorePrefix)
            eAnnotations.setAttribute("source", "ecore:Stereotype_Annotation");
        else
            eAnnotations.setAttribute("source", "Stereotype_Annotation");

        Element details = getDetailsForStereotypeWithQualifiedNameList();
        eAnnotations.appendChild(details);

        Element details2 = getDetailsWithProfiles(name, profileType);
        eAnnotations.appendChild(details2);

        Element details3 = getDetailsForHorizontalStereo();
        eAnnotations.appendChild(details3);

        Element details4 = getDetailsForPropStereoDisplay();
        eAnnotations.appendChild(details4);

        Element details5 = getDetailsForStereotypePropertyLocation();
        eAnnotations.appendChild(details5);

        if (classToAddSte != null)
            classToAddSte.appendChild(eAnnotations);
    }

    private Element getDetailsForStereotypeWithQualifiedNameList() {
        Element details = documentManager.getDocNotation().createElement("details");
        details.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details.setAttribute("xmi:id", UtilResources.getRandomUUID());
        details.setAttribute("key", "StereotypeWithQualifiedNameList");
        details.setAttribute("value", "");
        return details;
    }

    private Element getDetailsWithProfiles(String name, String profileType) {
        Element details2 = documentManager.getDocNotation().createElement("details");
        details2.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details2.setAttribute("xmi:id", UtilResources.getRandomUUID());
        details2.setAttribute("key", "StereotypeList");
        if ("smarty".equalsIgnoreCase(profileType))
            details2.setAttribute("value", "smarty::" + name);
        else if ("concerns".equalsIgnoreCase(profileType))
            details2.setAttribute("value", "concerns::" + name);
        else if ("br.otimizes.oplatool.patterns".equalsIgnoreCase(profileType))
            details2.setAttribute("value", "br.otimizes.oplatool.patterns::" + name);
        return details2;
    }

    private Element getDetailsForHorizontalStereo() {
        Element details3 = documentManager.getDocNotation().createElement("details");
        details3.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details3.setAttribute("xmi:id", UtilResources.getRandomUUID());
        details3.setAttribute("key", "Stereotype_Presentation_Kind");
        details3.setAttribute("value", "HorizontalStereo");
        return details3;
    }

    private Element getDetailsForPropStereoDisplay() {
        Element details4 = documentManager.getDocNotation().createElement("details");
        details4.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details4.setAttribute("xmi:id", UtilResources.getRandomUUID());
        details4.setAttribute("key", "PropStereoDisplay");
        details4.setAttribute("value", "");
        return details4;
    }

    private Element getDetailsForStereotypePropertyLocation() {
        Element details5 = documentManager.getDocNotation().createElement("details");
        details5.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details5.setAttribute("xmi:id", UtilResources.getRandomUUID());
        details5.setAttribute("key", "StereotypePropertyLocation");
        details5.setAttribute("value", "Compartment");
        return details5;
    }

}