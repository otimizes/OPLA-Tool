package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Realization relationship to file
 */
public class SaveRealizationSMarty {

    public SaveRealizationSMarty() {
    }

    private static final SaveRealizationSMarty INSTANCE = new SaveRealizationSMarty();

    public static SaveRealizationSMarty getInstance() {
        return INSTANCE;
    }

    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof RealizationRelationship) {
                RealizationRelationship realizationRelationship = (RealizationRelationship) relationship;
                Element firstElement = getFirstElement(architecture, logPath, realizationRelationship);
                if (firstElement == null) continue;
                Element secondElement = getSecondElement(architecture, logPath, realizationRelationship);
                if (secondElement == null) continue;
                idRelationship = getIdRelationship(architecture, idRelationship, realizationRelationship);
                printWriter.write("\n" + tab + "<realization id=\"" + realizationRelationship.getId() + "\" class=\"" + secondElement.getId() + "\" interface=\"" + firstElement.getId() + "\">");
                printWriter.write("\n" + tab + "</realization>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, RealizationRelationship realizationRelationship) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(realizationRelationship.getSupplier().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Realization " + realizationRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + realizationRelationship.getClient().getId()
                    + " - " + realizationRelationship.getClient().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + realizationRelationship.getSupplier().getId()
                    + " - " + realizationRelationship.getSupplier().getName() + " not found");
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, RealizationRelationship realizationRelationship) {
        Element secondElement = architecture.findElementByNameInPackageAndSubPackage(realizationRelationship.getClient().getName());
        if (secondElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Realization " + realizationRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + realizationRelationship.getClient().getId()
                    + " - " + realizationRelationship.getClient().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + realizationRelationship.getSupplier().getId()
                    + " - " + realizationRelationship.getSupplier().getName());
            return null;
        }
        return secondElement;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, RealizationRelationship realizationRelationship) {
        if (realizationRelationship.getId().length() == 0) {
            boolean existID = true;
            while (existID) {
                existID = false;
                for (Relationship secondRelationship : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (secondRelationship.getId().equals("REALIZATION#" + idRelationship)) {
                        idRelationship++;
                        existID = true;
                        break;
                    }
                }
            }
            realizationRelationship.setId("REALIZATION#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
