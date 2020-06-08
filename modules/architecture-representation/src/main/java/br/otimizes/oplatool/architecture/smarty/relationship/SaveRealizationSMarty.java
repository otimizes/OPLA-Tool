package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.RealizationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Realization relationship to file
 *
 */
public class SaveRealizationSMarty {

    public SaveRealizationSMarty() {
    }

    private static final SaveRealizationSMarty INSTANCE = new SaveRealizationSMarty();

    public static SaveRealizationSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * This class save Realization relationship to file
     *
     * @param architecture - architecture to be decoded
     * @param printWriter - used to save a string in file
     * @param logPath - path to save log if has a error
     */
    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int id_rel = 1;
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof RealizationRelationship) {
                RealizationRelationship rr = (RealizationRelationship) r;

                Element e1 = architecture.findElementByNameInPackageAndSubPackage(rr.getSupplier().getName());
                if (e1 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Realization " + rr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + rr.getClient().getId() + " - " + rr.getClient().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + rr.getSupplier().getId() + " - " + rr.getSupplier().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(rr.getClient().getName());
                if (e2 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Realization " + rr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClint: " + rr.getClient().getId() + " - " + rr.getClient().getName() + " not found");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + rr.getSupplier().getId() + " - " + rr.getSupplier().getName());
                    continue;
                }
                if (rr.getId().length() == 0) {
                    boolean existID = true;
                    while (existID) {
                        existID = false;
                        for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                            if (r2.getId().equals("REALIZATION#" + id_rel)) {
                                id_rel++;
                                existID = true;
                                break;
                            }
                        }
                    }
                    rr.setId("REALIZATION#" + id_rel);
                    id_rel++;
                }
                printWriter.write("\n" + tab + "<realization id=\"" + rr.getId() + "\" class=\"" + e2.getId() + "\" interface=\"" + e1.getId() + "\">");
                printWriter.write("\n" + tab + "</realization>");
            }
        }
    }

}
