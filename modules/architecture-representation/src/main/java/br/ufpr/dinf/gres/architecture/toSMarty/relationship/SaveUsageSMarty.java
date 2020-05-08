package br.ufpr.dinf.gres.architecture.toSMarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.UsageRelationship;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;

import java.io.PrintWriter;

public class SaveUsageSMarty {

    public SaveUsageSMarty(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int id_rel = 1;
        // usage relationship salvo como dependency (SMarty Modeling não tem usage)
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof UsageRelationship) {
                // <dependency source="CLASS#12" target="INTERFACE#10"/>
                UsageRelationship dr = (UsageRelationship) r;

                Element e1 = architecture.findElementByNameInPackageAndSubPackage(dr.getClient().getName());
                if (e1 == null) {
                    System.out.println("Discart Usage 1:" + dr.getClient().getId());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Usage " + dr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName() + " not found");
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(dr.getSupplier().getName());
                if (e2 == null) {
                    System.out.println("Discart USAGE 2:" + dr.getSupplier().getId());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Usage " + dr.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName() + " not found");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                    continue;
                }
                if (dr.getId().length() == 0) {
                    boolean existID = true;
                    while (existID) {
                        existID = false;
                        for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                            // change to this when SMarty Modeling suport USAGE
                            // if(r2.getId().equals("USAGE#" + id_rel)){
                            if (r2.getId().equals("DEPENDENCY#" + id_rel)) {
                                id_rel++;
                                existID = true;
                                break;
                            }
                        }
                    }
                    // change to this when SMarty Modeling suport USAGE
                    //dr.setId("USAGE#" + id_rel);
                    dr.setId("DEPENDENCY#" + id_rel);
                    id_rel++;
                }
                printWriter.write("\n" + tab + "<dependency id=\"" + dr.getId() + "\" source=\"" + e1.getId() + "\" target=\"" + e2.getId() + "\">");
                printWriter.write("\n" + tab + "</dependency>");

                // change to this when SMarty Modeling suport USAGE
                /*
                   printWriter.write("\n"+tab+"<usage id=\""+dr.getId()+"\" source=\""+e1.getId()+"\" target=\""+e2.getId()+"\">");
                   printWriter.write("\n"+tab+"</usage>");

                */

                SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nUsage " + dr.getId() + " salvo como Dependency");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nSupplier: " + dr.getSupplier().getId() + " - " + dr.getSupplier().getName());
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nClient: " + dr.getClient().getId() + " - " + dr.getClient().getName());
                continue;

            }
        }
    }

}
