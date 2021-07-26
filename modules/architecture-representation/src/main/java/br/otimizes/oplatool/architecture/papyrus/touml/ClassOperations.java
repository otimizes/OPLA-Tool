package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.helpers.Uml2Helper;
import br.otimizes.oplatool.architecture.helpers.Uml2HelperFactory;
import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Variant;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.uml2.uml.Profile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class operations
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ClassOperations extends XmiHelper {

    private static final String WITHOUT_PACKAGE = "";
    static Logger LOGGER = LogManager.getLogger(ClassOperations.class.getName());
    private String idClass;
    private final DocumentManager documentManager;
    private final ElementXmiGenerator elementXmiGenerator;
    private String idsProperties = "";
    private String idsMethods = "";
    private Node klass;
    private boolean isAbstract = false;
    private final Uml2Helper uml2Helper;

    public ClassOperations(DocumentManager documentManager, Architecture a) {
        uml2Helper = Uml2HelperFactory.instance.get();
        this.documentManager = documentManager;
        this.elementXmiGenerator = new ElementXmiGenerator(documentManager, a);
    }

    public ClassOperations createClass(final br.otimizes.oplatool.architecture.representation.Element _klass) {
        klass = elementXmiGenerator.generateClass(_klass, WITHOUT_PACKAGE);
        this.idClass = _klass.getId();
        return this;
    }

    public ClassOperations withAttribute(final List<Attribute> attributes) {
        for (final Attribute attribute : attributes) {
            Document.executeTransformation(documentManager, () -> {
                elementXmiGenerator.generateAttribute(attribute, idClass);
                idsProperties += attribute.getId() + " ";
            });
        }
        return this;
    }

    public ClassOperations withMethods(final Set<Method> methods) {
        for (final Method method : methods) {
            createMethod(method);
        }
        return this;
    }

    public ClassOperations withMethod(final Method method) {
        createMethod(method);
        return this;
    }

    private void createMethod(final Method method) {
        Document.executeTransformation(documentManager, () -> {
            elementXmiGenerator.generateMethod(method, idClass);
            idsMethods += method.getId() + " ";
        });
    }

    public Map<String, String> build() {
        Document.executeTransformation(documentManager, () -> {
            Element e = (Element) klass;
            e.setAttribute("isAbstract", isClassAbstract(isAbstract));
        });

        Map<String, String> createdClassInfos = new HashMap<>();
        createdClassInfos.put("id", this.idClass);
        createdClassInfos.put("idsProperties", this.idsProperties);
        createdClassInfos.put("idsMethods", this.idsMethods);

        return createdClassInfos;
    }


    public void removeClassById(final String id) {
        Document.executeTransformation(documentManager, () -> {
            RemoveNode removeClass = new RemoveNode(documentManager.getDocUml(), documentManager.getDocNotation());
            removeClass.removeClassById(id);
        });
    }


    public void removeAttribute(final String idAttributeToRemove) {
        final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
        Document.executeTransformation(documentManager, () -> removeClass.removeAttributeeById(idAttributeToRemove, idClass));
    }

    public void removeMethod(final String idMethodoToRmove) {
        final RemoveNode removeClass = new RemoveNode(this.documentManager.getDocUml(), this.documentManager.getDocNotation());
        Document.executeTransformation(documentManager, () -> removeClass.removeMethodById(idMethodoToRmove, idClass));
    }

    public ClassOperations addMethodToClass(final String idClass, final Method method) {
        Document.executeTransformation(documentManager, () -> {
            elementXmiGenerator.generateMethod(method, idClass);
            idsMethods += method.getId() + " ";
        });
        return this;
    }

    public void addAttributeToClass(final String idClass, final Attribute attribute) {
        Document.executeTransformation(documentManager, () -> {
            elementXmiGenerator.generateAttribute(attribute, idClass);
            idsProperties += attribute.getId() + " ";
        });
    }

    public ClassOperations isAbstract() {
        this.isAbstract = true;
        return this;
    }

    public ClassOperations withStereotype(final Variant... stereotypeNames) {
        Profile profile = uml2Helper.loadSMartyProfile();
        for (final Variant variant : stereotypeNames) {
            org.eclipse.uml2.uml.Stereotype stereotype = profile.getOwnedStereotype(variant.getVariantName());
            if (stereotype == null)
                LOGGER.warn("Stereotype + " + variant.getVariantName() + " cannot be found at profile.");
            Document.executeTransformation(documentManager, () -> elementXmiGenerator.createStereotype(variant, idClass));
        }
        return this;
    }

    public void addStereotype(final String id, final Variant variant) {
        Document.executeTransformation(documentManager, () -> elementXmiGenerator.createStereotype(variant, id));
    }

    public ClassOperations isVariationPoint(final String variants, final String variabilities, final String bidingTime) {
        if ((!variants.isEmpty()) || (!variabilities.isEmpty())) {
            Document.executeTransformation(documentManager, () -> elementXmiGenerator.createStereotypeVariationPoint(idClass, variants, variabilities, bidingTime));
        }
        return this;
    }

    public ClassOperations linkToNote(final String id) {
        Document.executeTransformation(documentManager, () -> {
            Element comment = (Element) findByID(documentManager.getDocUml(), id, "ownedComment");
            if (comment != null)
                comment.setAttribute("annotatedElement", idClass);

            Element edges = getEdges(id);

            Element styles = getStyles();
            edges.appendChild(styles);

            Element element = getNilElement();
            edges.appendChild(element);

            Element bendPoints = getBendPoints();
            edges.appendChild(bendPoints);

            Element sourceAnchor = getSourceAnchor();
            edges.appendChild(sourceAnchor);

            Node root = documentManager.getDocNotation().getElementsByTagName("notation:Diagram").item(0);
            root.appendChild(edges);
        });
        return this;
    }

    private Element getEdges(String id) {
        Node commentElement = findByIDInNotationFile(documentManager.getDocNotation(), id);
        Node classElement = findByIDInNotationFile(documentManager.getDocNotation(), idClass);
        Element edges = null;
        if (commentElement != null && classElement != null) {
            String idCommentNotation = commentElement.getAttributes().getNamedItem("xmi:id").getNodeValue();
            String idClassNotation = classElement.getAttributes().getNamedItem("xmi:id").getNodeValue();
            edges = documentManager.getDocNotation().createElement("edges");
            edges.setAttribute("xmi:type", "notation:Connector");
            edges.setAttribute("xmi:id", UtilResources.getRandomUUID());
            edges.setAttribute("type", "4013");
            edges.setAttribute("source", idCommentNotation);
            edges.setAttribute("target", idClassNotation);
            edges.setAttribute("lineColor", "0");
        }
        return edges;
    }

    private Element getStyles() {
        Element styles = documentManager.getDocNotation().createElement("styles");
        styles.setAttribute("xmi:id", UtilResources.getRandomUUID());
        styles.setAttribute("fontName", "Lucida Grande");
        styles.setAttribute("xmi:type", "notation:FontStyle");
        styles.setAttribute("fontHeight", "11");
        return styles;
    }

    private Element getNilElement() {
        Element element = documentManager.getDocNotation().createElement("element");
        element.setAttribute("xsi:nil", "true");
        return element;
    }

    private Element getBendPoints() {
        Element bendPoints = documentManager.getDocNotation().createElement("bendpoints");
        bendPoints.setAttribute("xmi:type", "notation:RelativeBendpoints");
        bendPoints.setAttribute("xmi:id", UtilResources.getRandomUUID());
        bendPoints.setAttribute("points", "[-10, 17, 55, -89]$[-63, 156, 2, 50]");
        return bendPoints;
    }

    private Element getSourceAnchor() {
        Element sourceAnchor = documentManager.getDocNotation().createElement("sourceAnchor");
        sourceAnchor.setAttribute("xmi:type", "notation:IdentityAnchor");
        sourceAnchor.setAttribute("xmi:id", UtilResources.getRandomUUID());
        sourceAnchor.setAttribute("id", "(1.0,0.7166666666666667)");
        return sourceAnchor;
    }

    public ClassOperations withId(String ownerClass) {
        this.idClass = ownerClass;
        return this;
    }

    public ClassOperations asInterface() {
        Document.executeTransformation(documentManager, () -> elementXmiGenerator.interfaceStereoptye(idClass));
        return this;
    }
}