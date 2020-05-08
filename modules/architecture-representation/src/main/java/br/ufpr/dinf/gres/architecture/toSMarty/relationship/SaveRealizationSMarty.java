package br.ufpr.dinf.gres.architecture.toSMarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.RealizationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;

import java.io.PrintWriter;

public class SaveRealizationSMarty {

    public SaveRealizationSMarty(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int id_rel = 1;
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof RealizationRelationship) {
                //<realization class="CLASS#6" interface="INTERFACE#5"/>
                RealizationRelationship rr = (RealizationRelationship) r;

                Element e1 = architecture.findElementByNameInPackageAndSubPackage(rr.getSupplier().getName());
                if (e1 == null) {
                    System.out.println("Discart Real 1:" + rr.getSupplier().getId());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Realization " + rr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + rr.getClient().getId() + " - " + rr.getClient().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + rr.getSupplier().getId() + " - " + rr.getSupplier().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(rr.getClient().getName());
                if (e2 == null) {
                    System.out.println("Discart Real 2:" + rr.getClient().getId());
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
