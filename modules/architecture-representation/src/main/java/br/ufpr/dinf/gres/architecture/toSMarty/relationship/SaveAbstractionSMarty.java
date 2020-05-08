package br.ufpr.dinf.gres.architecture.toSMarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.AbstractionRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;

import java.io.PrintWriter;

public class SaveAbstractionSMarty {

    public SaveAbstractionSMarty(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int id_rel = 1;
        ///// AbstractionRelationship salvo como Dependency (SMarty Modeling não tem abstraction)
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof AbstractionRelationship) {
                // <dependency source="CLASS#12" target="INTERFACE#10"/>
                AbstractionRelationship dr = (AbstractionRelationship) r;

                Element e1 = architecture.findElementByNameInPackageAndSubPackage(dr.getClient().getName());
                if (e1 == null) {
                    System.out.println("Discart Abstraction 1:" + dr.getClient().getId());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Abstraction " + dr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(dr.getSupplier().getName());
                if (e2 == null) {
                    System.out.println("Discart Abstraction 2:" + dr.getSupplier().getId());
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
                            // if(r2.getId().equals("ABSTRACTION#" + id_rel)){
                            if (r2.getId().equals("DEPENDENCY#" + id_rel)) {
                                id_rel++;
                                existID = true;
                                break;
                            }
                        }
                    }
                    //dr.setId("ABSTRACTION#" + id_rel);
                    dr.setId("DEPENDENCY#" + id_rel);
                    id_rel++;
                }
                printWriter.write("\n" + tab + "<dependency id=\"" + dr.getId() + "\" source=\"" + e1.getId() + "\" target=\"" + e2.getId() + "\">");
                printWriter.write("\n" + tab + "</dependency>");

                    /*
                    printWriter.write("\n"+tab+"<abstraction id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                    printWriter.write("\n"+tab+"</abstraction>");

                     */


                SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nAbstraction " + dr.getId() + " salvo como Dependency");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                continue;

            }
        }
    }

}
