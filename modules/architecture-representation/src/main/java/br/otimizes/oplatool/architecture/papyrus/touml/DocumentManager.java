package br.otimizes.oplatool.architecture.papyrus.touml;

import br.otimizes.oplatool.architecture.exceptions.ModelIncompleteException;
import br.otimizes.oplatool.architecture.exceptions.ModelNotFoundException;
import br.otimizes.oplatool.architecture.helpers.XmiHelper;
import br.otimizes.oplatool.architecture.io.SaveAndMove;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileUtils;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Document manager
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class DocumentManager extends XmiHelper {

    static Logger LOGGER = Logger.getLogger(DocumentManager.class);
    private final String BASE_DOCUMENT = "simples";
    private org.w3c.dom.Document docUml;
    private org.w3c.dom.Document docNotation;
    private org.w3c.dom.Document docDi;
    private String outputModelName;

    public DocumentManager(String outputModelName) throws ModelNotFoundException, ModelIncompleteException {
        this.outputModelName = outputModelName;
        makeACopy(BASE_DOCUMENT);
        createXMIDocument();
        updateProfilesRefs();
        copyProfilesToDestination();
        this.saveAndCopy(outputModelName);
    }

    private void copyProfilesToDestination() {
        try {
            createResourcesDirectoryIfNotExist();

            if (ApplicationFileConfigThreadScope.hasSmartyProfile()) {
                String pathSmarty = ApplicationFileConfigThreadScope.getPathToProfile();
                final File sourceFileSmarty = new File(pathSmarty);
                final File destFileSmarty = new File(
                        ApplicationFileConfigThreadScope.getDirectoryToExportModels() + "/resources/smarty.profile.uml");
                Files.copy(sourceFileSmarty, destFileSmarty);
            } else {
                XmiHelper.removeNode(docUml, "profileApplication", "_2RlssY9OEeO5xq3Ur4qgFw");
            }

            if (ApplicationFileConfigThreadScope.hasConcernsProfile()) {
                String pathConcern = ApplicationFileConfigThreadScope.getPathToProfileConcern();
                final File sourceFileConcern = new File(pathConcern);
                final File destFileConcern = new File(
                        ApplicationFileConfigThreadScope.getDirectoryToExportModels() + "/resources/concerns.profile.uml");
                Files.copy(sourceFileConcern, destFileConcern);
            } else {
                XmiHelper.removeNode(docUml, "profileApplication", "_2Q2s4I9OEeO5xq3Ur4qgFw");
            }

            if (ApplicationFileConfigThreadScope.hasRelationsShipProfile()) {
                String pathToProfileRelationships = ApplicationFileConfigThreadScope.getPathToProfileRelationships();
                final File sourceFileRelationships = new File(pathToProfileRelationships);
                final File destFileRelationship = new File(
                        ApplicationFileConfigThreadScope.getDirectoryToExportModels() + "/resources/relationships.profile.uml");
                Files.copy(sourceFileRelationships, destFileRelationship);
            } else {
                XmiHelper.removeNode(docUml, "profileApplication", "_2RXDMI9OEeO5xq3Ur4qgFw");
            }

            if (ApplicationFileConfigThreadScope.hasPatternsProfile()) {
                final File destFileRelationship = new File(
                        ApplicationFileConfigThreadScope.getDirectoryToExportModels() + "/resources/br.otimizes.oplatool.patterns.profile.uml");
                Files.copy(new File(ApplicationFileConfigThreadScope.getPathToProfilePatterns()), destFileRelationship);
            } else {
                XmiHelper.removeNode(docUml, "profileApplication", "_cyBBIJJmEeOENZsdUoZvrw");
            }

        } catch (IOException e) {
            LOGGER.warn("I cannot copy resources to destination. " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void createResourcesDirectoryIfNotExist() {
        File resourcesDir = new File(ApplicationFileConfigThreadScope.getDirectoryToExportModels() + "/resources/");
        if (!resourcesDir.exists())
            resourcesDir.mkdir();
    }

    private void createXMIDocument() {
        LOGGER.info("createXMIDocument()");
        DocumentBuilderFactory docBuilderFactoryNotation = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilderNotation;
        DocumentBuilder docBuilderUml;

        try {
            docBuilderNotation = docBuilderFactoryNotation.newDocumentBuilder();
            DocumentBuilderFactory docBuilderFactoryUml = DocumentBuilderFactory.newInstance();
            docBuilderUml = docBuilderFactoryUml.newDocumentBuilder();

        } catch (ParserConfigurationException e) {
            LOGGER.error(e);
            throw new RuntimeException();
        }

        try {
            LOGGER.info("docNotation");
            this.docNotation = docBuilderNotation.parse(ApplicationFileConfigThreadScope.getDirectoryToSaveModels() + BASE_DOCUMENT + ".notation");
            LOGGER.info("docUml");
            this.docUml = docBuilderUml.parse(ApplicationFileConfigThreadScope.getDirectoryToSaveModels() + BASE_DOCUMENT + ".uml");
            LOGGER.info("docDi");
            this.docDi = docBuilderUml.parse(ApplicationFileConfigThreadScope.getDirectoryToSaveModels() + BASE_DOCUMENT + ".di");
        } catch (SAXException e) {
            LOGGER.error(e);
            throw new RuntimeException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void makeACopy(String modelName) throws ModelNotFoundException, ModelIncompleteException {
        LOGGER.info("makeACopy(String modelName) - Enter");
        File temp = new File(ApplicationFileConfigThreadScope.getDirectoryToSaveModels());
        if (!temp.exists())
            temp.mkdirs();

        String notationCopy = ApplicationFileConfigThreadScope.getDirectoryToSaveModels() + BASE_DOCUMENT + ".notation";
        String umlCopy = ApplicationFileConfigThreadScope.getDirectoryToSaveModels() + BASE_DOCUMENT + ".uml";
        String diCopy = ApplicationFileConfigThreadScope.getDirectoryToSaveModels() + BASE_DOCUMENT + ".di";

        URL n = null;
        URL u = null;
        URL d = null;
        try {
            URL baseUrl = new URL("file:" + ApplicationFileConfigThreadScope.getPathToTemplateModelsDirectory());
            n = new URL(baseUrl, modelName + ".notation");
            u = new URL(baseUrl, modelName + ".uml");
            d = new URL(baseUrl, modelName + ".di");
        } catch (MalformedURLException e) {
            LOGGER.error("makeACopy(String modelName) - Could not find template files directory: "
                    + ApplicationFileConfigThreadScope.getPathToTemplateModelsDirectory());
        }

        if (n != null) FileUtils.copyFile(new File(n.getPath()), new File(notationCopy));
        if (u != null) FileUtils.copyFile(new File(u.getPath()), new File(umlCopy));
        if (d != null) FileUtils.copyFile(new File(d.getPath()), new File(diCopy));

        LOGGER.info("makeACopy(String modelName) - Exit");

    }

    /**
     * @return the docUml
     */
    public org.w3c.dom.Document getDocUml() {
        return docUml;
    }

    /**
     * @return the docNotation
     */
    public org.w3c.dom.Document getDocNotation() {
        return docNotation;
    }

    public void saveAndCopy(String newModelName) {
        this.outputModelName = newModelName;

        try {
            SaveAndMove.saveAndMove(docNotation, docUml, docDi, BASE_DOCUMENT, newModelName);
        } catch (TransformerException | IOException e) {
            LOGGER.error(e);
            throw new RuntimeException();
        }
    }

    public String getModelName() {
        return BASE_DOCUMENT;
    }

    public String getNewModelName() {
        return this.outputModelName;
    }

    public void updateProfilesRefs() {
        LOGGER.info("updateProfilesRefs()");
        String pathToProfileConcern = ApplicationFileConfigThreadScope.getPathToProfileConcern();

        DocumentBuilderFactory factoryConcern = DocumentBuilderFactory.newInstance();
        DocumentBuilder profileConcern = null;

        try {

            LOGGER.info("hasConcernsProfile");
            if (ApplicationFileConfigThreadScope.hasConcernsProfile()) {
                LOGGER.info("profileConcern");
                profileConcern = factoryConcern.newDocumentBuilder();
                LOGGER.info("docConcern");
                final Document docConcern = profileConcern.parse(pathToProfileConcern);

                updateHrefAtt(getIdOnNode(docConcern, "contents", "xmi:id"),
                        false);
                updateHrefAtt(getIdOnNode(docConcern, "uml:Profile", "xmi:id"),
                        true);

                final String nsUriProfileConcern = getIdOnNode(docConcern, "contents", "nsURI");
                br.otimizes.oplatool.architecture.papyrus.touml.Document.executeTransformation(this, () -> {
                    Node xmlSnsConcern = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes()
                            .getNamedItem("xmlns:concerns");
                    xmlSnsConcern.setNodeValue(nsUriProfileConcern);
                    String concernLocaltionSchema = nsUriProfileConcern + " " + "resources/concerns.profile.uml#"
                            + getIdOnNode(docConcern, "contents", "xmi:id");

                    Node nodeSchemaLocation = docUml.getElementsByTagName("xmi:XMI").item(0).getAttributes()
                            .getNamedItem("xsi:schemaLocation");
                    nodeSchemaLocation
                            .setNodeValue(nodeSchemaLocation.getNodeValue() + " " + concernLocaltionSchema + " ");
                });
            }
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void updateHrefAtt(final String idApplied,
                               final boolean updateReference) {
        LOGGER.info("updateHrefAtt()");
        br.otimizes.oplatool.architecture.papyrus.touml.Document.executeTransformation(this, () -> {
            Node node;
            if (updateReference) {
                node = getAppliedHrefProfile();
            } else {
                node = getReference();
            }
            if (node != null) {
                Node nodeAttr = node.getAttributes().getNamedItem("href");
                String oldValueAttr = nodeAttr.getNodeValue().substring(0, nodeAttr.getNodeValue().indexOf("#"));
                nodeAttr.setNodeValue(oldValueAttr + "#" + idApplied);
            }
        });
    }

    private Node getAppliedHrefProfile() {
        NodeList elements = docUml.getElementsByTagName("profileApplication");
        for (int i = 0; i < elements.getLength(); i++) {
            NodeList children = (elements.item(i).getChildNodes());
            for (int j = 0; j < children.getLength(); j++)
                if (children.item(j).getNodeName().equals("appliedProfile")
                        && (children.item(j).getAttributes().getNamedItem("href").getNodeValue().contains("concerns")))
                    return children.item(j);
        }
        return null;
    }

    private Node getReference() {
        NodeList elements = this.docUml.getElementsByTagName("profileApplication");
        for (int i = 0; i < elements.getLength(); i++) {
            NodeList children = (elements.item(i).getChildNodes());
            for (int j = 0; j < children.getLength(); j++) {
                if (children.item(j).getNodeName().equalsIgnoreCase("eAnnotations")) {
                    for (int k = 0; k < children.item(j).getChildNodes().getLength(); k++) {
                        if (children.item(j).getChildNodes().item(k).getNodeName().equalsIgnoreCase("references")) {
                            NodeList eAnnotationsChildren = children.item(j).getChildNodes();
                            for (int l = 0; l < eAnnotationsChildren.getLength(); l++)
                                if (isProfileNode(eAnnotationsChildren, l))
                                    return eAnnotationsChildren.item(l);
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isProfileNode(NodeList eAnnotationsChildren, int l) {
        return (eAnnotationsChildren.item(l).getNodeName().equalsIgnoreCase("references") && (eAnnotationsChildren.item(l)
                .getAttributes().getNamedItem("href").getNodeValue().contains("concerns")));
    }

    private String getIdOnNode(Document document, String tagName, String attrName) {
        return document.getElementsByTagName(tagName).item(0).getAttributes().getNamedItem(attrName).getNodeValue();
    }
}