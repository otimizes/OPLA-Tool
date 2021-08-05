package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.representation.relationship.RequiresRelationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Requires relationship to file
 */
public class SaveRequiresSMarty {

    public SaveRequiresSMarty() {
    }

    private static final SaveRequiresSMarty INSTANCE = new SaveRequiresSMarty();

    public static SaveRequiresSMarty getInstance() {
        return INSTANCE;
    }

    public void save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof RequiresRelationship) {
                RequiresRelationship requiresRelationship = (RequiresRelationship) relationship;
                Element firstElement = getFirstElement(architecture, logPath, requiresRelationship);
                if (firstElement == null) continue;
                Element secondElement = getSecondElement(architecture, logPath, requiresRelationship);
                if (secondElement == null) continue;
                idRelationship = getIdRelationship(architecture, idRelationship, requiresRelationship);
                printWriter.write("\n" + tab + "<requires id=\"" + requiresRelationship.getId() + "\" source=\""
                        + firstElement.getId() + "\" target=\"" + secondElement.getId() + "\">");
                printWriter.write("\n" + tab + "</requires>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, RequiresRelationship requiresRelationship) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(requiresRelationship.getClient().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Requires " + requiresRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + requiresRelationship.getSupplier().getId()
                    + " - " + requiresRelationship.getSupplier().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + requiresRelationship.getClient().getId()
                    + " - " + requiresRelationship.getClient().getName() + " not found");
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, RequiresRelationship requiresRelationship) {
        Element secondElement = architecture.findElementByNameInPackageAndSubPackage(requiresRelationship.getSupplier().getName());
        if (secondElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Requires " + requiresRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + requiresRelationship.getSupplier().getId()
                    + " - " + requiresRelationship.getSupplier().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + requiresRelationship.getClient().getId()
                    + " - " + requiresRelationship.getClient().getName());
            return null;
        }
        return secondElement;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, RequiresRelationship requiresRelationship) {
        if (requiresRelationship.getId().length() == 0) {
            boolean existID = true;
            while (existID) {
                existID = false;
                for (Relationship secondRelationship : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (secondRelationship.getId().equals("REQUIRES#" + idRelationship)) {
                        idRelationship++;
                        existID = true;
                        break;
                    }
                }
            }
            requiresRelationship.setId("REQUIRES#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
