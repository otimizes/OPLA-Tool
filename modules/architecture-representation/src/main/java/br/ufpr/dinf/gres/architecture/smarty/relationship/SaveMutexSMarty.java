package br.ufpr.dinf.gres.architecture.smarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.MutexRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.smarty.util.SaveStringToFile;

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

    /**
     * This class save mutex relationship to file
     *
     * @param architecture - architecture to be decoded
     * @param printWriter - used to save a string in file
     * @param logPath - path to save log if has a error
     */
    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int id_rel = 1;
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof MutexRelationship) {
                MutexRelationship dr = (MutexRelationship) r;
                Element e1 = architecture.findElementByNameInPackageAndSubPackage(dr.getClient().getName());
                if (e1 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Mutex " + dr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(dr.getSupplier().getName());
                if (e2 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Mutex " + dr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName() + " not found");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                    continue;
                }
                if (dr.getId().length() == 0) {
                    boolean existID = true;
                    while (existID) {
                        existID = false;
                        for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                            if(r2.getId().equals("MUTEX#" + id_rel)){
                                id_rel++;
                                existID = true;
                                break;
                            }
                        }
                    }
                    dr.setId("MUTEX#" + id_rel);
                    id_rel++;
                }
                printWriter.write("\n" + tab + "<mutex id=\"" + dr.getId() + "\" source=\"" + e1.getId() + "\" target=\"" + e2.getId() + "\">");
                printWriter.write("\n" + tab + "</mutex>");
            }
        }
    }

}
