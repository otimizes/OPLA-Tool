package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.GeneralizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Generalization relationship to file
 */
public class SaveGeneralizationSMarty {
    public SaveGeneralizationSMarty() {
    }

    private static final SaveGeneralizationSMarty INSTANCE = new SaveGeneralizationSMarty();

    public static SaveGeneralizationSMarty getInstance() {
        return INSTANCE;
    }

    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof GeneralizationRelationship) {
                GeneralizationRelationship generalizationRelationship = (GeneralizationRelationship) relationship;
                Element firstElement = getFirstElement(architecture, logPath, generalizationRelationship);
                if (firstElement == null) continue;
                Element secondElement = getSecondElement(architecture, logPath, generalizationRelationship);
                if (secondElement == null) continue;
                idRelationship = getIdRelationship(architecture, idRelationship, generalizationRelationship);
                printWriter.write("\n" + tab + "<generalization id=\"" + generalizationRelationship.getId()
                        + "\" source=\"" + firstElement.getId() + "\" target=\"" + secondElement.getId() + "\">");
                printWriter.write("\n" + tab + "</generalization>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, GeneralizationRelationship generalizationRelationship) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(generalizationRelationship.getChild().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Generalization " + generalizationRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nParent: "
                    + generalizationRelationship.getParent().getId() + " - " + generalizationRelationship.getParent().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nChild: " + generalizationRelationship.getChild().getId()
                    + " - " + generalizationRelationship.getChild().getName() + " not found");
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, GeneralizationRelationship generalizationRelationship) {
        Element secondElement = architecture.findElementByNameInPackageAndSubPackage(generalizationRelationship.getParent().getName());
        if (secondElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Generalization " + generalizationRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nParent: " + generalizationRelationship.getParent().getId() + " - "
                    + generalizationRelationship.getParent().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nChild: " + generalizationRelationship.getChild().getId() + " - "
                    + generalizationRelationship.getChild().getName());
            return null;
        }
        return secondElement;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, GeneralizationRelationship generalizationRelationship) {
        if (generalizationRelationship.getId().length() == 0) {
            boolean existID = true;
            while (existID) {
                existID = false;
                for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (r2.getId().equals("GENERALIZATION#" + idRelationship)) {
                        idRelationship++;
                        existID = true;
                        break;
                    }
                }
            }
            generalizationRelationship.setId("GENERALIZATION#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
