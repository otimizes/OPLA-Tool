package br.ufpr.dinf.gres.architecture.smarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.AbstractionRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.smarty.util.SaveStringToFile;

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
 *
 */
public class SaveAbstractionSMarty {

    public SaveAbstractionSMarty() {
    }

    private static final SaveAbstractionSMarty INSTANCE = new SaveAbstractionSMarty();

    public static SaveAbstractionSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * This class save Abstraction relationship to file (temporary save as Dependency)
     *
     * @param architecture - architecture to be decoded
     * @param printWriter - used to save a string in file
     * @param logPath - path to save log if has a error
     */
    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int id_rel = 1;
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof AbstractionRelationship) {
                AbstractionRelationship dr = (AbstractionRelationship) r;
                Element e1 = architecture.findElementByNameInPackageAndSubPackage(dr.getClient().getName());
                if (e1 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Abstraction " + dr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(dr.getSupplier().getName());
                if (e2 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Abstraction " + dr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName() + " not found");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                    continue;
                }
                if (dr.getId().length() == 0) {
                    boolean existID = true;
                    while (existID) {
                        existID = false;
                        for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                            if(r2.getId().equals("ABSTRACTION#" + id_rel)){
                                id_rel++;
                                existID = true;
                                break;
                            }
                        }
                    }
                    dr.setId("ABSTRACTION#" + id_rel);
                    id_rel++;
                }
                printWriter.write("\n" + tab + "<abstraction id=\"" + dr.getId() + "\" source=\"" + e1.getId() + "\" target=\"" + e2.getId() + "\">");
                printWriter.write("\n" + tab + "</abstraction>");
            }
        }
    }

}
