package br.ufpr.dinf.gres.architecture.papyrus.touml;


import br.ufpr.dinf.gres.architecture.exceptions.NodeNotFound;
import br.ufpr.dinf.gres.architecture.exceptions.NullReferenceFoundException;
import br.ufpr.dinf.gres.architecture.helpers.UtilResources;
import br.ufpr.dinf.gres.architecture.helpers.XmiHelper;
import br.ufpr.dinf.gres.architecture.representation.Attribute;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
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
    private String xmitype = "notation:Shape";
    private Node notatioChildren;
    private Element notationBasicProperty;
    private DocumentManager documentManager;
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


    public ClassNotation(DocumentManager documentManager, Node notatioChildren) {
        this.documentManager = documentManager;
        this.notatioChildren = notatioChildren;
        this.notatioChildren = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
    }

    public void createNodeForElementType(String idProperty, String type, String typeElement, Element appendTo) {
        Element node = documentManager.getDocNotation().createElement("children");

        node.setAttribute("xmi:type", this.xmitype);
        node.setAttribute("xmi:id", UtilResources.getRandonUUID());
        node.setAttribute("type", type);
        node.setAttribute("fontName", this.fontName);
        node.setAttribute("fontHeight", this.fontHeight);
        node.setAttribute("lineColor", this.lineColor);


        Element eAnnotations = documentManager.getDocNotation().createElement("eAnnotations");
        eAnnotations.setAttribute("xmi:type", "ecore:EAnnotation");
        eAnnotations.setAttribute("source", "CustomAppearance_Annotation");
        eAnnotations.setAttribute("xmi:id", UtilResources.getRandonUUID());


        Element details = documentManager.getDocNotation().createElement("details");
        details.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details.setAttribute("xmi:id", UtilResources.getRandonUUID());
        details.setAttribute("key", "CustomAppearance_MaskValue");
        details.setAttribute("value", SHOW_PROPERTY_TYPE);
        eAnnotations.appendChild(details);

        node.appendChild(eAnnotations);


        Element element = documentManager.getDocNotation().createElement("element");
        element.setAttribute("xmi:type", typeElement);
        element.setAttribute("href", documentManager.getModelName() + "#" + idProperty);
        node.appendChild(element);

        Element layoutConstraint = documentManager.getDocNotation().createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Location");
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
        node.appendChild(layoutConstraint);

        if (appendTo != null)
            appendTo.appendChild(node);
        else
            notationBasicProperty.appendChild(node);
    }

    /**
     * @param id        - ID da class ou associationClass no arquivo uml
     * @param idPackage - ID do pacote se tiver
     * @param type      - "associationClass" se for para associationClass qualquer outra coisa para clas
     * @throws NullReferenceFoundException
     */
    public String createXmiForClassInNotationFile(String id, String idPackage, String type, br.ufpr.dinf.gres.architecture.representation.Element aClass) throws NullReferenceFoundException {

        Element node = documentManager.getDocNotation().createElement("children");
        node.setAttribute("xmi:type", this.xmitype);
        String idChildren = UtilResources.getRandonUUID();
        node.setAttribute("xmi:id", idChildren);
        node.setAttribute("type", TYPE_CLASS);
        node.setAttribute("fontName", this.fontName);
        node.setAttribute("fontHeight", this.fontHeight);
        node.setAttribute("lineColor", this.lineColor);


        Element notationDecoratioNode = documentManager.getDocNotation().createElement("children");
        notationDecoratioNode.setAttribute("xmi:type", "notation:DecorationNode");
        notationDecoratioNode.setAttribute("xmi:id", UtilResources.getRandonUUID());
        notationDecoratioNode.setAttribute("type", "5029");
        node.appendChild(notationDecoratioNode);

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

        this.notationBasicProperty = createChildrenComportament(documentManager.getDocNotation(), node, LOCATION_TO_ADD_ATTR_IN_NOTATION_FILE, aClass); //onde vai as props
        createChildrenComportament(documentManager.getDocNotation(), node, LOCATION_TO_ADD_METHOD_IN_NOTATION_FILE, aClass); //onde vai os methods
        node.appendChild(klass);

        if ((idPackage != null) && !(idPackage.isEmpty())) {
            Node nodeToAppend = findByIDInNotationFile(documentManager.getDocNotation(), idPackage);
            nodeToAppend.appendChild(node);
        } else {
            notatioChildren.appendChild(node);
        }

        return idChildren;

    }


    private Element createChildrenComportament(Document doc, Element node, String type, br.ufpr.dinf.gres.architecture.representation.Element aClass) {
        Element element = doc.createElement("children");
        element.setAttribute("xmi:type", "notation:BasicCompartment");
        element.setAttribute("xmi:id", UtilResources.getRandonUUID());
        element.setAttribute("type", type);
        node.appendChild(element);

        Element notationTitleStyle = doc.createElement("styles");
        notationTitleStyle.setAttribute("xmi:type", "notation:TitleStyle");
        notationTitleStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
        element.appendChild(notationTitleStyle);

        Element notationSortingStyle = doc.createElement("styles");
        notationSortingStyle.setAttribute("xmi:type", "notation:SortingStyle");
        notationSortingStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
        element.appendChild(notationSortingStyle);

        Element notationFilteringStyle = doc.createElement("styles");
        notationFilteringStyle.setAttribute("xmi:type", "notation:FilteringStyle");
        notationFilteringStyle.setAttribute("xmi:id", UtilResources.getRandonUUID());
        element.appendChild(notationFilteringStyle);

//        Element notationBounds = doc.createElement("layoutConstraint");
//        notationBounds.setAttribute("xmi:type", "notation:Bounds");
//        notationBounds.setAttribute("xmi:id", UtilResources.getRandonUUID());
//        element.appendChild(notationBounds);

        Element layoutConstraint = doc.createElement("layoutConstraint");
        layoutConstraint.setAttribute("x", String.valueOf(xElement));
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());
        layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
        layoutConstraint.setAttribute("y", String.valueOf(yElement));
        layoutConstraint.setAttribute("additionalInfo", aClass.getNamespace() + ":" + aClass.getName());

        if (!lastNamespace.equals(aClass.getNamespace())) {
            lastNamespace = aClass.getNamespace();
            if (aClass instanceof Class) {
                yElement = 1;
                xElement = 1;
            } else {
                yElement = 810;
                xElement = 1;
            }
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

        return element;
    }

    private int getHeightClass(br.ufpr.dinf.gres.architecture.representation.Element aClass) {
        int YClass = 100;
        if (aClass instanceof Class) {
            for (Attribute ignored : ((Class) aClass).getAllAttributes()) {
                YClass += 10;
            }
            for (Method ignored : ((Class) aClass).getAllMethods()) {
                YClass += 10;
            }
        } else {
            for (Method ignored : ((Interface) aClass).getMethods()) {
                YClass += 10;
            }
        }
        return YClass;
    }

    private int getWidthClass(br.ufpr.dinf.gres.architecture.representation.Element aClass) {
        int XClass = 1;
        int i = 0;
        int[] XClasss = new int[aClass instanceof Class ? ((Class) aClass).getAllAttributes().size() + ((Class) aClass).getAllMethods().size() : ((Interface) aClass).getMethods().size()];
        if (aClass instanceof Class) {
            for (Attribute allAttribute : ((Class) aClass).getAllAttributes()) {
                XClasss[i] += (allAttribute.getType().length() + allAttribute.getAllConcerns().toString().length() + allAttribute.getVisibility().length() + allAttribute.getName().length());
            }
            for (Method method : ((Class) aClass).getAllMethods()) {
                XClasss[i] += (method.getParameters().toString().length() + method.getAllConcerns().toString().length() + method.getReturnType().length() + method.getName().length());
            }
        } else {
            for (Method operation : ((Interface) aClass).getMethods()) {
                XClasss[i] += (operation.toString().length() + operation.getName().length() + operation.getAllConcerns().toString().length() + operation.getTypeElement().length());
            }
        }

        for (int classs : XClasss) {
            if (classs > XClass) XClass = classs;
        }
        return XClass;
    }

    //TODO MOVER PAR PACKAGEOPERATIONS
    public void createXmiForPackageInNotationFile(String id, Package original) {

        Element nodeChildren = documentManager.getDocNotation().createElement("children");
        nodeChildren.setAttribute("xmi:type", this.xmitype);
        nodeChildren.setAttribute("xmi:id", UtilResources.getRandonUUID());
        nodeChildren.setAttribute("type", TYPE_PACKAGE);
        nodeChildren.setAttribute("fontName", this.fontName);
        nodeChildren.setAttribute("fontHeight", this.fontHeight);
        nodeChildren.setAttribute("lineColor", this.lineColor);


        Element childrenDecorationnode5026 = createChildrenDecorationnode("5026");
        Element childrenDecorationnode7016 = createChildrenDecorationnode("7016");

        Element layoutConstraint = documentManager.getDocNotation().createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandonUUID());

        Element styles = documentManager.getDocNotation().createElement("styles");
        styles.setAttribute("xmi:type", "notation:TitleStyle");
        styles.setAttribute("xmi:id", UtilResources.getRandonUUID());
        childrenDecorationnode7016.appendChild(styles);
        childrenDecorationnode7016.appendChild(layoutConstraint);

        nodeChildren.appendChild(childrenDecorationnode5026);
        nodeChildren.appendChild(childrenDecorationnode7016);


        Element elementPackage = documentManager.getDocNotation().createElement("element");
        elementPackage.setAttribute("xmi:type", "uml:Package");
        elementPackage.setAttribute("href", documentManager.getModelName() + "#" + id);
        nodeChildren.appendChild(elementPackage);

        //TODO mover comum
        Element layoutConstraint2 = documentManager.getDocNotation().createElement("layoutConstraint");
        layoutConstraint2.setAttribute("x", String.valueOf(xPackage));
        layoutConstraint2.setAttribute("xmi:id", UtilResources.getRandonUUID());
        layoutConstraint2.setAttribute("xmi:type", "notation:Bounds");
        layoutConstraint2.setAttribute("y", String.valueOf(yPackage));
        layoutConstraint2.setAttribute("width", String.valueOf(original.getElements().size() * 300)); //TODO ver uma maneira de criar conforme necessidade
        xPackage += original.getElements().size() * 300 + 50;

        int height = 1650;
        layoutConstraint2.setAttribute("height", String.valueOf(height));

        if (xPackage >= 6000) {
            xPackage = 1;
            yPackage += height + 50;
        }

        nodeChildren.appendChild(layoutConstraint2);

        notatioChildren.appendChild(nodeChildren);

    }

    private Element createChildrenDecorationnode(String type) {
        Element childrenDecorationnode = documentManager.getDocNotation().createElement("children");
        childrenDecorationnode.setAttribute("xmi:type", "notation:DecorationNode");
        childrenDecorationnode.setAttribute("xmi:id", UtilResources.getRandonUUID());
        childrenDecorationnode.setAttribute("type", type);
        return childrenDecorationnode;
    }

    /**
     * @param name       - o nome do esterótipo
     * @param idClass    - id da classe que se deseja aplicar o estereótipo
     * @param perfilType - de qual perfil vem o estereótipo sendo aplicado
     * @throws NodeNotFound
     */
    public void createXmiForStereotype(String name, String idClass, String perfilType) {

        Node node = getXmiToAppendStereotype(idClass);
        if (node != null) {
            Node valueAttr = node.getAttributes().getNamedItem("value");
            String oldValue = valueAttr.getNodeValue().trim();
            if ("smarty".equalsIgnoreCase(perfilType))
                valueAttr.setNodeValue(oldValue + ",smarty::" + name);
            else if ("concerns".equalsIgnoreCase(perfilType))
                valueAttr.setNodeValue(oldValue + ",concerns::" + name);
            else if ("br.ufpr.dinf.gres.patterns".equalsIgnoreCase(perfilType))
                valueAttr.setNodeValue(oldValue + ",br.ufpr.dinf.gres.patterns::" + name);
        } else {
            ste(name, idClass, false, perfilType);
            ste(name, idClass, true, perfilType);
        }
    }

    /**
     * Verifica se o arquivo xmi já tem os nodes resposnveis por apresentar os estereotipos.
     * Se tiver retorna o node para que se possa adicinar o estereotipo.
     *
     * @param idClass
     * @return
     * @throws NodeNotFound
     */
    private Node getXmiToAppendStereotype(String idClass) {
        //Verifica se já existe no notation o xmi para estereotipos
        Node node = findByIDInNotationFile(documentManager.getDocNotation(), idClass);

        NodeList chidreen = node.getChildNodes();
        for (int i = 0; i < chidreen.getLength(); i++) {
            if ("eAnnotations".equalsIgnoreCase(chidreen.item(i).getNodeName())) {
                String nodeValue = chidreen.item(i).getAttributes().getNamedItem("source").getNodeValue();
                if ("Stereotype_Annotation".equalsIgnoreCase(nodeValue)) {
                    NodeList eAnnotationsChildreen = chidreen.item(i).getChildNodes();
                    for (int j = 0; j < eAnnotationsChildreen.getLength(); j++) {
                        String keyValue = eAnnotationsChildreen.item(j).getAttributes().getNamedItem("key").getNodeValue();
                        if ("StereotypeList".equalsIgnoreCase(keyValue)) {
                            return eAnnotationsChildreen.item(j);
                        }
                    }
                }
            }
        }
        return null;
    }

    private void ste(String name, String idClass, boolean addEcorePrefix, String perfilType) {
        Node classToAddSte = findByIDInNotationFile(documentManager.getDocNotation(), idClass);

        Element eAnnotations = documentManager.getDocNotation().createElement("eAnnotations");
        eAnnotations.setAttribute("xmi:type", "ecore:EAnnotation");
        eAnnotations.setAttribute("xmi:id", UtilResources.getRandonUUID());

        if (addEcorePrefix)
            eAnnotations.setAttribute("source", "ecore:Stereotype_Annotation");
        else
            eAnnotations.setAttribute("source", "Stereotype_Annotation");

        Element details = documentManager.getDocNotation().createElement("details");
        details.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details.setAttribute("xmi:id", UtilResources.getRandonUUID());
        details.setAttribute("key", "StereotypeWithQualifiedNameList");
        details.setAttribute("value", "");
        eAnnotations.appendChild(details);

        Element details2 = documentManager.getDocNotation().createElement("details");
        details2.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details2.setAttribute("xmi:id", UtilResources.getRandonUUID());
        details2.setAttribute("key", "StereotypeList");
        if ("smarty".equalsIgnoreCase(perfilType))
            details2.setAttribute("value", "smarty::" + name);
        else if ("concerns".equalsIgnoreCase(perfilType))
            details2.setAttribute("value", "concerns::" + name);
        else if ("br.ufpr.dinf.gres.patterns".equalsIgnoreCase(perfilType))
            details2.setAttribute("value", "br.ufpr.dinf.gres.patterns::" + name);
        eAnnotations.appendChild(details2);

        Element details3 = documentManager.getDocNotation().createElement("details");
        details3.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details3.setAttribute("xmi:id", UtilResources.getRandonUUID());
        details3.setAttribute("key", "Stereotype_Presentation_Kind");
        details3.setAttribute("value", "HorizontalStereo");
        eAnnotations.appendChild(details3);

        Element details4 = documentManager.getDocNotation().createElement("details");
        details4.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details4.setAttribute("xmi:id", UtilResources.getRandonUUID());
        details4.setAttribute("key", "PropStereoDisplay");
        details4.setAttribute("value", "");
        eAnnotations.appendChild(details4);

        Element details5 = documentManager.getDocNotation().createElement("details");
        details5.setAttribute("xmi:type", "ecore:EStringToStringMapEntry");
        details5.setAttribute("xmi:id", UtilResources.getRandonUUID());
        details5.setAttribute("key", "StereotypePropertyLocation");
        details5.setAttribute("value", "Compartment");
        eAnnotations.appendChild(details5);

        classToAddSte.appendChild(eAnnotations);
    }

}