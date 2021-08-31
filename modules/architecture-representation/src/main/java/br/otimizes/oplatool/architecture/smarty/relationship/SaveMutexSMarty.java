package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.MutexRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Mutex relationship to file
 */
public class SaveMutexSMarty {

    public SaveMutexSMarty() {
    }

    private static final SaveMutexSMarty INSTANCE = new SaveMutexSMarty();

    public static SaveMutexSMarty getInstance() {
        return INSTANCE;
    }

    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof MutexRelationship) {
                MutexRelationship mutexRelationship = (MutexRelationship) relationship;
                Element firstElement = getFirstElement(architecture, logPath, mutexRelationship);
                if (firstElement == null) continue;
                Element second = getSecondElement(architecture, logPath, mutexRelationship);
                if (second == null) continue;
                idRelationship = getIdRelationship(architecture, idRelationship, mutexRelationship);
                printWriter.write("\n" + tab + "<mutex id=\"" + mutexRelationship.getId() + "\" source=\""
                        + firstElement.getId() + "\" target=\"" + second.getId() + "\">");
                printWriter.write("\n" + tab + "</mutex>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, MutexRelationship mutexRelationship) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(mutexRelationship.getClient().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Mutex " + mutexRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + mutexRelationship.getSupplier().getId()
                    + " - " + mutexRelationship.getSupplier().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + mutexRelationship.getClient().getId()
                    + " - " + mutexRelationship.getClient().getName() + " not found");
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, MutexRelationship mutexRelationship) {
        Element second = architecture.findElementByNameInPackageAndSubPackage(mutexRelationship.getSupplier().getName());
        if (second == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Mutex " + mutexRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + mutexRelationship.getSupplier().getId()
                    + " - " + mutexRelationship.getSupplier().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + mutexRelationship.getClient().getId()
                    + " - " + mutexRelationship.getClient().getName());
            return null;
        }
        return second;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, MutexRelationship mutexRelationship) {
        if (mutexRelationship.getId().length() == 0) {
            boolean existID = true;
            while (existID) {
                existID = false;
                for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (r2.getId().equals("MUTEX#" + idRelationship)) {
                        idRelationship++;
                        existID = true;
                        break;
                    }
                }
            }
            mutexRelationship.setId("MUTEX#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
