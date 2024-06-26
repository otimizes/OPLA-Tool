package br.otimizes.oplatool.architecture.papyrus.touml;


import br.otimizes.oplatool.architecture.helpers.UtilResources;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.representation.VariationPoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Note node
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class NoteNode extends XmiHelper {

    private final Document docUml;
    private final Document docNotation;
    private Node modelRootUml;
    private Node modelRootNotation;
    private final String newModelName;
    private String id;

    public NoteNode(DocumentManager documentManager) {
        this.newModelName = documentManager.getNewModelName();
        this.docUml = documentManager.getDocUml();
        this.docNotation = documentManager.getDocNotation();
        setRootUml();
        setRootNotation();
    }

    private void setRootNotation() {
        modelRootNotation = this.docNotation.getElementsByTagName("notation:Diagram").item(0);
    }

    private void setRootUml() {
        modelRootUml = this.docUml.getElementsByTagName("uml:Model").item(0);
    }

    public void createNote(String id, VariationPoint variationPointForVariability) {
        createXmiInUmlFile(id);
        Element childrenComment = getShape();
        Element childrenDecoration = getChildrenDecoration(childrenComment);
        Element element = getComment();
        Element layoutConstraint = getLayoutConstraint(variationPointForVariability);
        childrenComment.appendChild(childrenDecoration);
        childrenComment.appendChild(layoutConstraint);
        childrenComment.appendChild(element);
        modelRootNotation.appendChild(childrenComment);
    }

    private Element getShape() {
        Element childrenComment = docNotation.createElement("children");
        childrenComment.setAttribute("xmi:type", "notation:Shape");
        childrenComment.setAttribute("xmi:id", UtilResources.getRandomUUID());
        childrenComment.setAttribute("type", "2012");
        childrenComment.setAttribute("fontName", "Lucida Grande");
        childrenComment.setAttribute("fontHeight", "11");
        childrenComment.setAttribute("lineColor", "0");
        return childrenComment;
    }

    private Element getChildrenDecoration(Element childrenComment) {
        //<children xmi:type="notation:DecorationNode" xmi:id="_77c8RsFKEeKazYM6BmtXSg" type="5038"/>
        Element childrenDecoration = docNotation.createElement("children");
        childrenDecoration.setAttribute("xmi:type", "notation:DecorationNode");
        childrenDecoration.setAttribute("xmi:id", UtilResources.getRandomUUID());
        childrenDecoration.setAttribute("type", "5038");
        childrenComment.appendChild(childrenDecoration);
        return childrenDecoration;
    }

    private Element getLayoutConstraint(VariationPoint variationPointForVariability) {
        //<layoutConstraint xmi:type="notation:Bounds" xmi:id="_77cVMcFKEeKazYM6BmtXSg" x="434" y="63"/>
        Element layoutConstraint = docNotation.createElement("layoutConstraint");
        layoutConstraint.setAttribute("xmi:type", "notation:Bounds");
        layoutConstraint.setAttribute("xmi:id", UtilResources.getRandomUUID());
        Node elementByAttr = getElementByAttr(new String[]{"xmi:type", "additionalInfo"}, new String[]{"notation:Bounds",
                variationPointForVariability.getVariationPointElement().getNamespace() + ":" + variationPointForVariability.getVariationPointElement().getName()});
        assert elementByAttr != null;
        int x = Integer.parseInt(elementByAttr.getAttributes().getNamedItem("x").getNodeValue()) + 100;
        layoutConstraint.setAttribute("x", elementByAttr.getAttributes().getNamedItem("x").getNodeValue());
        layoutConstraint.setAttribute("y", elementByAttr.getAttributes().getNamedItem("y").getNodeValue());
        return layoutConstraint;
    }

    private Element getComment() {
        //<element xmi:type="uml:Comment" href="modelComment.uml#_74vmgMFKEeKazYM6BmtXSg"/>
        Element element = docNotation.createElement("element");
        element.setAttribute("xmi:type", "uml:Comment");
        element.setAttribute("href", this.newModelName + ".uml#" + this.id);
        return element;
    }

    private Node getElementByAttr(String[] attrs, String[] ids) {
        NodeList elements = modelRootNotation.getChildNodes();
        int length = elements.getLength();
        for (int i = 0; i < length; i++) {
            Node item = elements.item(i);
            if (item.getAttributes() != null) {
                boolean equals = true;
                for (int j = 0; j < attrs.length; j++) {
                    System.out.println("---------------------------------------");
                    System.out.println(item.getAttributes().getNamedItem(attrs[j]) != null
                            ? item.getAttributes().getNamedItem(attrs[j]).getNodeValue() : "null" + " = " + ids[j]);
                    System.out.println("---------------------------------------");
                    if (item.getAttributes().getNamedItem(attrs[j]) == null || (item.getAttributes().getNamedItem(attrs[j]) != null
                            && !item.getAttributes().getNamedItem(attrs[j]).getNodeValue().equals(ids[j]))) {
                        equals = false;
                    }
                }
                if (equals) {
                    return item;
                }
            }
        }
        return null;
    }

    private void createXmiInUmlFile(String id) {
        this.id = id;
        Element comment = docUml.createElement("ownedComment");
        comment.setAttribute("xmi:id", this.id);
        Element commentBody = docUml.createElement("body");
        comment.appendChild(commentBody);
        this.modelRootUml.appendChild(comment);
    }
}