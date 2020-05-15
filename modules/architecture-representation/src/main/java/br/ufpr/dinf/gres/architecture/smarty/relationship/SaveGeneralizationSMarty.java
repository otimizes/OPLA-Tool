package br.ufpr.dinf.gres.architecture.smarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.GeneralizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save Generalization relationship to file
 *
 */
public class SaveGeneralizationSMarty {
    public SaveGeneralizationSMarty() {
    }

    private static final SaveGeneralizationSMarty INSTANCE = new SaveGeneralizationSMarty();

    public static SaveGeneralizationSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * This class save Generalization relationship to file
     *
     * @param architecture - architecture to be decoded
     * @param printWriter - used to save a string in file
     * @param logPath - path to save log if has a error
     */
    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String tab = "    ";
        int id_rel = 1;
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof GeneralizationRelationship) {
                GeneralizationRelationship gr = (GeneralizationRelationship) r;
                Element e1 = architecture.findElementByNameInPackageAndSubPackage(gr.getChild().getName());
                if (e1 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Generealization " + gr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nParent: " + gr.getParent().getId() + " - " + gr.getParent().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nChild: " + gr.getChild().getId() + " - " + gr.getChild().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(gr.getParent().getName());
                if (e2 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Genelarization " + gr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nParent: " + gr.getParent().getId() + " - " + gr.getParent().getName() + " not found");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nChild: " + gr.getChild().getId() + " - " + gr.getChild().getName());
                    continue;
                }
                if (gr.getId().length() == 0) {
                    boolean existID = true;
                    while (existID) {
                        existID = false;
                        for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                            if (r2.getId().equals("GENERALIZATION#" + id_rel)) {
                                id_rel++;
                                existID = true;
                                break;
                            }
                        }
                    }
                    gr.setId("GENERALIZATION#" + id_rel);
                    id_rel++;
                }
                printWriter.write("\n" + tab + "<generalization id=\"" + gr.getId() + "\" source=\"" + e1.getId() + "\" target=\"" + e2.getId() + "\">");
                printWriter.write("\n" + tab + "</generalization>");
            }
        }
    }

}
