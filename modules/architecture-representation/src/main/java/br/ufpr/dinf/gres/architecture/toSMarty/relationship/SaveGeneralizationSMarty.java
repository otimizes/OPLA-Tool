package br.ufpr.dinf.gres.architecture.toSMarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.GeneralizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;

import java.io.PrintWriter;

public class SaveGeneralizationSMarty {

    public SaveGeneralizationSMarty(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int id_rel = 1;
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof GeneralizationRelationship) {
                // <generalization source="CLASS#7" target="CLASS#3"/>
                GeneralizationRelationship gr = (GeneralizationRelationship) r;


                Element e1 = architecture.findElementByNameInPackageAndSubPackage(gr.getChild().getName());
                if (e1 == null) {
                    System.out.println("Discart Gen 1:" + gr.getChild().getId());

                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Generealization " + gr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nParent: " + gr.getParent().getId() + " - " + gr.getParent().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nChild: " + gr.getChild().getId() + " - " + gr.getChild().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(gr.getParent().getName());
                if (e2 == null) {
                    System.out.println("Discart Gen 2:" + gr.getParent().getId());
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
