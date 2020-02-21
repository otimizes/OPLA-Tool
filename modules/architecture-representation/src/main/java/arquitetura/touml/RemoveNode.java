package arquitetura.touml;

import arquitetura.helpers.XmiHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class RemoveNode extends XmiHelper {

    static Logger LOGGER = LogManager.getLogger(RemoveNode.class.getName());

    private Document docUml;
    private Document docNotation;


    public RemoveNode(Document docUml, Document docNotation) {
        this.docUml = docUml;
        this.docNotation = docNotation;
    }

    /**
     * Remove node de um dado documento com o id informado.
     *
     * @param document
     * @param id
     */
    public void removeClassById(String id) {
        Node umlNOde = this.docUml.getElementsByTagName("uml:Model").item(0);
        try {
            umlNOde.removeChild(findByID(this.docUml, id, "packagedElement"));
            removeNodeFromNotationFile(id);

            LOGGER.info("Class with id: " + id + " removed from UML file.");
        } catch (Exception e) {
            LOGGER.info("Cannot reemove Class with id: " + id + ".");
        }
    }

    public void removeAttributeeById(String id, String idClass) {
        try {
            removeNodeFromUmlFile(id, idClass, "ownedAttribute");
            removeNodeFromNotationFile(id);
            LOGGER.info("Attribute with id: " + id + " removed.");
        } catch (Exception e) {
            LOGGER.info("Cannot reemove Attribute with id: " + id + ".");
        }
    }


    public void removeMethodById(String idMethodToRemove, String idClass) {
        try {
            removeNodeFromUmlFile(idMethodToRemove, idClass, "ownedOperation");
            removeNodeFromNotationFile(idMethodToRemove);
            LOGGER.info("Method with id: " + idMethodToRemove + " removed.");
        } catch (Exception e) {
            LOGGER.info("Cannot reemove method with id: " + idMethodToRemove + ".");
        }
    }

    private void removeNodeFromUmlFile(String idMethodToRemove, String idClass, String typeElement) {
        NodeList umlNOde = this.docUml.getElementsByTagName("packagedElement");
        Node element = findByID(this.docUml, idMethodToRemove, typeElement);
        for (int i = 0; i < umlNOde.getLength(); i++) {
            NamedNodeMap attributes = umlNOde.item(i).getAttributes();
            for (int j = 0; j < attributes.getLength(); j++) {
                String valueAttribute = attributes.item(j).getNodeValue();
                if (idClass.equalsIgnoreCase(valueAttribute))
                    umlNOde.item(i).removeChild(element);
            }
        }
    }

    private void removeNodeFromNotationFile(String id) {
        try {
            Node notationNode = findByIDInNotationFile(this.docNotation, id);
            notationNode.getParentNode().removeChild(notationNode);
            LOGGER.info("Class with id: " + id + " removed from NOTATION file.");
        } catch (Exception e) {
            LOGGER.info("Problem when trying remove node with id: " + id + " from notation file. " + e.getMessage());
        }

    }

}
