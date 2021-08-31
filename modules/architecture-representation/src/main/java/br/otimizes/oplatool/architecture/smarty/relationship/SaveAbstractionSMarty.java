package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.AbstractionRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Association relationship to file
 * The Abstraction will be saved as Dependency until SMarty Modeling has this relationship
 * Then replace lines:
 * 65 :    if (r2.getId().equals("DEPENDENCY#" + id_rel)) {       to       if(r2.getId().equals("ABSTRACTION#" + id_rel)){
 * 72 :    dr.setId("DEPENDENCY#" + id_rel);                      to       dr.setId("ABSTRACTION#" + id_rel);
 * 75 :    printWriter.write("\n" + tab + "<dependency id=\"" + dr.getId() + "\" source=\"" + e1.getId() + "\" target=\"" + e2.getId() + "\">");                      to       printWriter.write("\n"+tab+"<abstraction id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
 * 76 :    printWriter.write("\n" + tab + "</dependency>");        to       printWriter.write("\n"+tab+"</abstraction>");
 * and remove lines 77-79
 */
public class SaveAbstractionSMarty {

    public SaveAbstractionSMarty() {
    }

    private static final SaveAbstractionSMarty INSTANCE = new SaveAbstractionSMarty();

    public static SaveAbstractionSMarty getInstance() {
        return INSTANCE;
    }

    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof AbstractionRelationship) {
                AbstractionRelationship abstractionRelationship = (AbstractionRelationship) relationship;
                Element firstElement = getFirstElement(architecture, logPath, abstractionRelationship);
                if (firstElement == null) continue;
                Element secondElement = getSecondElement(architecture, logPath, abstractionRelationship);
                if (secondElement == null) continue;
                idRelationship = getIdRelationship(architecture, idRelationship, abstractionRelationship);
                printWriter.write("\n" + tab + "<abstraction id=\"" + abstractionRelationship.getId()
                        + "\" source=\"" + firstElement.getId() + "\" target=\"" + secondElement.getId() + "\">");
                printWriter.write("\n" + tab + "</abstraction>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, AbstractionRelationship abstractionRelationship) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(abstractionRelationship.getClient().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Abstraction " + abstractionRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + abstractionRelationship.getSupplier().getId()
                    + " - " + abstractionRelationship.getSupplier().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + abstractionRelationship.getClient().getId()
                    + " - " + abstractionRelationship.getClient().getName() + " not found");
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, AbstractionRelationship abstractionRelationship) {
        Element secondElement = architecture.findElementByNameInPackageAndSubPackage(abstractionRelationship.getSupplier().getName());
        if (secondElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Abstraction " + abstractionRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + abstractionRelationship.getSupplier().getId()
                    + " - " + abstractionRelationship.getSupplier().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + abstractionRelationship.getClient().getId()
                    + " - " + abstractionRelationship.getClient().getName());
            return null;
        }
        return secondElement;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, AbstractionRelationship abstractionRelationship) {
        if (abstractionRelationship.getId().length() == 0) {
            boolean existId = true;
            while (existId) {
                existId = false;
                for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (r2.getId().equals("ABSTRACTION#" + idRelationship)) {
                        idRelationship++;
                        existId = true;
                        break;
                    }
                }
            }
            abstractionRelationship.setId("ABSTRACTION#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
