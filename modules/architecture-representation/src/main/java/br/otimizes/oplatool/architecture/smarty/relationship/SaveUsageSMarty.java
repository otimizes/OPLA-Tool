package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.representation.relationship.UsageRelationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Usage relationship to file
 */
public class SaveUsageSMarty {

    public SaveUsageSMarty() {
    }

    private static final SaveUsageSMarty INSTANCE = new SaveUsageSMarty();

    public static SaveUsageSMarty getInstance() {
        return INSTANCE;
    }

    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof UsageRelationship) {
                UsageRelationship usageRelationship = (UsageRelationship) relationship;
                Element firstElement = getFirstElement(architecture, logPath, usageRelationship);
                if (firstElement == null) continue;
                Element secondElement = getSecondElement(architecture, logPath, usageRelationship);
                if (secondElement == null) continue;
                idRelationship = getIdRelationship(architecture, idRelationship, usageRelationship);
                printWriter.write("\n" + tab + "<usage id=\"" + usageRelationship.getId() + "\" source=\"" + firstElement.getId() + "\" target=\"" + secondElement.getId() + "\">");
                printWriter.write("\n" + tab + "</usage>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, UsageRelationship usageRelationship) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(usageRelationship.getClient().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Usage " + usageRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + usageRelationship.getSupplier().getId() + " - " + usageRelationship.getSupplier().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + usageRelationship.getClient().getId() + " - " + usageRelationship.getClient().getName() + " not found");
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, UsageRelationship usageRelationship) {
        Element secondElement = architecture.findElementByNameInPackageAndSubPackage(usageRelationship.getSupplier().getName());
        if (secondElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Usage " + usageRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + usageRelationship.getSupplier().getId() + " - " + usageRelationship.getSupplier().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + usageRelationship.getClient().getId() + " - " + usageRelationship.getClient().getName());
            return null;
        }
        return secondElement;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, UsageRelationship usageRelationship) {
        if (usageRelationship.getId().length() == 0) {
            boolean existID = true;
            while (existID) {
                existID = false;
                for (Relationship secondRelationship : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (secondRelationship.getId().equals("USAGE#" + idRelationship)) {
                        idRelationship++;
                        existID = true;
                        break;
                    }
                }
            }
            usageRelationship.setId("USAGE#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
