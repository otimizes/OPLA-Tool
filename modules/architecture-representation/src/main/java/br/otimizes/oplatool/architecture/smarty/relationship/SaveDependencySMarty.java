package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.DependencyRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Dependency relationship to file
 */
public class SaveDependencySMarty {

    public SaveDependencySMarty() {
    }

    private static final SaveDependencySMarty INSTANCE = new SaveDependencySMarty();

    public static SaveDependencySMarty getInstance() {
        return INSTANCE;
    }

    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof DependencyRelationship) {
                DependencyRelationship dependencyRelationship = (DependencyRelationship) relationship;
                Element firstElement = getFirstElement(architecture, logPath, dependencyRelationship);
                if (firstElement == null) continue;
                Element secondElement = getSecondElement(architecture, logPath, dependencyRelationship);
                if (secondElement == null) continue;
                idRelationship = getIdRelationship(architecture, idRelationship, dependencyRelationship);
                printWriter.write("\n" + tab + "<dependency id=\"" + dependencyRelationship.getId() + "\" source=\""
                        + firstElement.getId() + "\" target=\"" + secondElement.getId() + "\">");
                printWriter.write("\n" + tab + "</dependency>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, DependencyRelationship dependencyRelationship) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(dependencyRelationship.getClient().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Dependency " + dependencyRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dependencyRelationship.getSupplier().getId()
                    + " - " + dependencyRelationship.getSupplier().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dependencyRelationship.getClient().getId()
                    + " - " + dependencyRelationship.getClient().getName() + " not found");
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, DependencyRelationship dependencyRelationship) {
        Element secondElement = architecture.findElementByNameInPackageAndSubPackage(dependencyRelationship.getSupplier().getName());
        if (secondElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Dependency " + dependencyRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dependencyRelationship.getSupplier().getId()
                    + " - " + dependencyRelationship.getSupplier().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dependencyRelationship.getClient().getId()
                    + " - " + dependencyRelationship.getClient().getName());
            return null;
        }
        return secondElement;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, DependencyRelationship dependencyRelationship) {
        if (dependencyRelationship.getId().length() == 0) {
            boolean existId = true;
            while (existId) {
                existId = false;
                for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (r2.getId().equals("DEPENDENCY#" + idRelationship)) {
                        idRelationship++;
                        existId = true;
                        break;
                    }
                }
            }
            dependencyRelationship.setId("DEPENDENCY#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
