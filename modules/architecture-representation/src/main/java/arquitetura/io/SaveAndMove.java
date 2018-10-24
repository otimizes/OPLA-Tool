package arquitetura.io;

import arquitetura.helpers.XmiHelper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class SaveAndMove extends XmiHelper {
	
	private static final Logger LOGGER = Logger.getLogger(SaveAndMove.class);


    public static void saveAndMove(Document docNotation, Document docUml, Document docDi, String originalModelName, String newModelName) throws TransformerException, IOException {
    	LOGGER.info("saveAndMove()");
        String targetDir = ReaderConfig.getDirTarget();
        String targetDirExport = ReaderConfig.getDirExportTarget();

        String notationCopy = targetDir + originalModelName + ".notation";
        String umlCopy = targetDir + originalModelName + ".uml";
        String diCopy = targetDir + originalModelName + ".di";

        LOGGER.info("transformerFactory");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        LOGGER.info("transformer");
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");


        //Necessario atualizar referencias pois nome do modelo pode ter mudado
        LOGGER.info("Atualizando referencias");
        NodeList elements = docNotation.getElementsByTagName("element");
        for (int i = 0; i < elements.getLength(); i++) {
            String idXmi = getOnlyIdOfXmiAttribute(elements, i);
            if (idXmi != null)
                elements.item(i).getAttributes().getNamedItem("href").setNodeValue(newModelName + ".uml#" + idXmi);
        }

        LOGGER.info("elementsUml");
        NodeList elementsUml = docDi.getElementsByTagName("emfPageIdentifier");
        for (int i = 0; i < elementsUml.getLength(); i++) {
            String idXmi = getOnlyIdOfXmiAttribute(elementsUml, i);
            elementsUml.item(i).getAttributes().getNamedItem("href").setNodeValue(newModelName + ".notation#" + idXmi);
        }

        LOGGER.info("Doc Notation");
        DOMSource source = new DOMSource(docNotation);
        StreamResult result = new StreamResult(new File(notationCopy));
        transformer.transform(source, result);

        LOGGER.info("Doc UML");
        DOMSource sourceUml = new DOMSource(docUml);
        StreamResult resultUml = new StreamResult(new File(umlCopy));
        transformer.transform(sourceUml, resultUml);

        LOGGER.info("Doc DI");
        DOMSource sourceDi = new DOMSource(docDi);
        StreamResult resultDi = new StreamResult(new File(diCopy));
        transformer.transform(sourceDi, resultDi);

        LOGGER.info("Moving Files");
        FileUtils.moveFiles(notationCopy, targetDirExport + newModelName + ".notation");
        FileUtils.moveFiles(umlCopy, targetDirExport + newModelName + ".uml");
        FileUtils.moveFiles(diCopy, targetDirExport + newModelName + ".di");

    }


}
