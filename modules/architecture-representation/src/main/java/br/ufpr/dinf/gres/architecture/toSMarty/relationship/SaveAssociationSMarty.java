package br.ufpr.dinf.gres.architecture.toSMarty.relationship;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationEnd;
import br.ufpr.dinf.gres.architecture.representation.relationship.AssociationRelationship;
import br.ufpr.dinf.gres.architecture.representation.relationship.Multiplicity;
import br.ufpr.dinf.gres.architecture.representation.relationship.Relationship;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;

import java.io.PrintWriter;
import java.util.Random;

public class SaveAssociationSMarty {

    public SaveAssociationSMarty(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int id_rel = 1;
        for (Relationship r : architecture.getRelationshipHolder().getAllRelationships()) {
            if (r instanceof AssociationRelationship) {
                //<association name="usermgr_user_1" category="aggregation" direction="true" source="CLASS#1" sourceName="UserMgr" sourceMin="1" sourceMax="2147483647" sourceX="140" sourceY="210" target="CLASS#2" targetName="User" targetMin="1" targetMax="2147483647" targetX="224" targetY="458"/>
                AssociationRelationship ar = (AssociationRelationship) r;
                AssociationEnd ae1 = ar.getParticipants().get(0);
                AssociationEnd ae2 = ar.getParticipants().get(1);

                Element e1 = architecture.findElementByNameInPackageAndSubPackage(ae1.getCLSClass().getName());
                if (e1 == null) {
                    System.out.println("Discart Association 1:" + ae1.getCLSClass().getId());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Association " + ar.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 1: " + ae1.getCLSClass().getId() + " - " + ae1.getCLSClass().getName() + " not found");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 2: " + ae2.getCLSClass().getId() + " - " + ae2.getCLSClass().getName());
                    continue;
                }
                Element e2 = architecture.findElementByNameInPackageAndSubPackage(ae2.getCLSClass().getName());
                if (e2 == null) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Association " + ar.getId() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 1: " + ae1.getCLSClass().getId() + " - " + ae1.getCLSClass().getName());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 2: " + ae2.getCLSClass().getId() + " - " + ae2.getCLSClass().getName() + " not found");
                    //System.out.println("Discart Association 2:"+ae2.getCLSClass().getId());
                    continue;
                }

                Multiplicity mult1 = ae1.getMultiplicity();
                Multiplicity mult2 = ae2.getMultiplicity();
                String category = "normal";
                if (ae1.getAggregation().equals("shared")) {
                    category = "aggregation";
                }
                if (ae1.getAggregation().equals("composite"))
                    category = "composition";
                if (mult1 == null) {
                    //System.out.println("\n\nMult1 == null");
                    mult1 = new Multiplicity("1", "1");
                }
                if (mult2 == null) {
                    //System.out.println("\n\nMult2 == null");
                    mult2 = new Multiplicity("1", "1");
                }

                Random rnd = new Random();

                int new_x = Integer.parseInt(ae1.getCLSClass().getGlobalPosX()) + Integer.parseInt(ae1.getCLSClass().getWidth()) / 2;
                int new_y = Integer.parseInt(ae1.getCLSClass().getGlobalPosY()) + Integer.parseInt(ae1.getCLSClass().getHeight()) / 2;
                ae1.setPosX("" + (new_x + rnd.nextInt(10)));
                ae1.setPosY("" + (new_y + rnd.nextInt(10)));
                new_x = Integer.parseInt(ae2.getCLSClass().getGlobalPosX()) + Integer.parseInt(ae2.getCLSClass().getWidth()) / 2;
                new_y = Integer.parseInt(ae2.getCLSClass().getGlobalPosY()) + Integer.parseInt(ae2.getCLSClass().getHeight()) / 2;
                ae2.setPosX("" + (new_x + rnd.nextInt(10)));
                ae2.setPosY("" + (new_y + rnd.nextInt(10)));


                if (ae1.getName().length() == 0) {
                    ae1.setName(ae1.getCLSClass().getName());
                }
                if (ae2.getName().length() == 0) {
                    ae2.setName(ae2.getCLSClass().getName());
                }


                if (ae1.getVisibility().length() == 0)
                    ae1.setVisibility("private");
                if (ae2.getVisibility().length() == 0)
                    ae2.setVisibility("private");
                if (ar.getId().length() == 0) {
                    boolean existID = true;
                    while (existID) {
                        existID = false;
                        for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                            if (r2.getId().equals("ASSOCIATION#" + id_rel)) {
                                id_rel++;
                                existID = true;
                                break;
                            }
                        }
                    }
                    ar.setId("ASSOCIATION#" + id_rel);
                    id_rel++;
                }
                //printWriter.write("\n"+tab+"<association name=\""+ar.getName()+"\" category=\""+category+"\" direction=\""+ae1.isNavigable()+"\" source=\""+e1.getId()+"\" sourceName=\""+ae1.getName()+"\" sourceMin=\""+mult1.getLowerValue()+"\" sourceMax=\""+mult1.getUpperValue()+"\" sourceX=\""+ae1.getPosX()+"\" sourceY=\""+ae1.getPosY()+"\" target=\""+e2.getId()+"\" targetName=\""+ae2.getName()+"\" targetMin=\""+mult2.getLowerValue()+"\" targetMax=\""+mult2.getUpperValue()+"\" targetX=\""+ae2.getPosX()+"\" targetY=\""+ae2.getPosY()+"\"/>");
                printWriter.write("\n" + tab + "<association id=\"" + ar.getId() + "\" name=\"" + ar.getName() + "\" category=\"" + category + "\" direction=\"" + ae1.isNavigable() + "\">");
                printWriter.write("\n" + tab + halfTab + "<source entity=\"" + e1.getId() + "\" sourceVisibility=\"" + ae1.getVisibility() + "\" sourceName=\"" + ae1.getName() + "\" sourceMin=\"" + mult1.getLowerValue() + "\" sourceMax=\"" + mult1.getUpperValue() + "\" sourceX=\"" + ae1.getPosX() + "\" sourceY=\"" + ae1.getPosY() + "\"/>");
                printWriter.write("\n" + tab + halfTab + "<target entity=\"" + e2.getId() + "\" targetVisibility=\"" + ae2.getVisibility() + "\" targetName=\"" + ae2.getName() + "\" targetMin=\"" + mult2.getLowerValue() + "\" targetMax=\"" + mult2.getUpperValue() + "\" targetX=\"" + ae2.getPosX() + "\" targetY=\"" + ae2.getPosY() + "\"/>");
                printWriter.write("\n" + tab + "</association>");

            }
        }
    }


}
