package br.otimizes.oplatool.architecture.smarty.relationship;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationEnd;
import br.otimizes.oplatool.architecture.representation.relationship.AssociationRelationship;
import br.otimizes.oplatool.architecture.representation.relationship.Multiplicity;
import br.otimizes.oplatool.architecture.representation.relationship.Relationship;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;
import java.util.Random;

/**
 * This class save Association relationship to file
 */
public class SaveAssociationSMarty {

    public SaveAssociationSMarty() {
    }

    private static final SaveAssociationSMarty INSTANCE = new SaveAssociationSMarty();

    public static SaveAssociationSMarty getInstance() {
        return INSTANCE;
    }

    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int idRelationship = 1;
        for (Relationship relationship : architecture.getRelationshipHolder().getAllRelationships()) {
            if (relationship instanceof AssociationRelationship) {
                AssociationRelationship associationRelationship = (AssociationRelationship) relationship;
                AssociationEnd firstAssociationEnd = associationRelationship.getParticipants().get(0);
                AssociationEnd secondAssociationEnd = associationRelationship.getParticipants().get(1);
                Element firstElement = getFirstElement(architecture, logPath, associationRelationship, firstAssociationEnd, secondAssociationEnd);
                if (firstElement == null) continue;
                Element secondElement = getSecondElement(architecture, logPath, firstAssociationEnd, secondAssociationEnd);
                if (secondElement == null) continue;
                Multiplicity firstMultiplicity = firstAssociationEnd.getMultiplicity();
                Multiplicity secondMultiplicity = secondAssociationEnd.getMultiplicity();
                String category = "normal";
                if (firstAssociationEnd.getAggregation().equals("shared")) {
                    category = "aggregation";
                }
                if (firstAssociationEnd.getAggregation().equals("composite"))
                    category = "composition";
                if (firstMultiplicity == null) {
                    firstMultiplicity = new Multiplicity("1", "1");
                }
                if (secondMultiplicity == null) {
                    secondMultiplicity = new Multiplicity("1", "1");
                }
                Random rnd = new Random();
                int newX = Integer.parseInt(firstAssociationEnd.getCLSClass().getGlobalPosX())
                        + Integer.parseInt(firstAssociationEnd.getCLSClass().getWidth()) / 2;
                int newY = Integer.parseInt(firstAssociationEnd.getCLSClass().getGlobalPosY())
                        + Integer.parseInt(firstAssociationEnd.getCLSClass().getHeight()) / 2;
                firstAssociationEnd.setPosX("" + (newX + rnd.nextInt(10)));
                firstAssociationEnd.setPosY("" + (newY + rnd.nextInt(10)));
                newX = Integer.parseInt(secondAssociationEnd.getCLSClass().getGlobalPosX())
                        + Integer.parseInt(secondAssociationEnd.getCLSClass().getWidth()) / 2;
                newY = Integer.parseInt(secondAssociationEnd.getCLSClass().getGlobalPosY())
                        + Integer.parseInt(secondAssociationEnd.getCLSClass().getHeight()) / 2;
                secondAssociationEnd.setPosX("" + (newX + rnd.nextInt(10)));
                secondAssociationEnd.setPosY("" + (newY + rnd.nextInt(10)));
                if (firstAssociationEnd.getName().length() == 0) {
                    firstAssociationEnd.setName(firstAssociationEnd.getCLSClass().getName());
                }
                if (secondAssociationEnd.getName().length() == 0) {
                    secondAssociationEnd.setName(secondAssociationEnd.getCLSClass().getName());
                }
                if (firstAssociationEnd.getVisibility().length() == 0)
                    firstAssociationEnd.setVisibility("private");
                if (secondAssociationEnd.getVisibility().length() == 0)
                    secondAssociationEnd.setVisibility("private");
                idRelationship = getIdRelationship(architecture, idRelationship, associationRelationship);
                printWriter.write("\n" + tab + "<association id=\"" + associationRelationship.getId()
                        + "\" name=\"" + associationRelationship.getName() + "\" category=\"" + category
                        + "\" direction=\"" + firstAssociationEnd.isNavigable() + "\">");
                printWriter.write("\n" + tab + halfTab + "<source entity=\"" + firstElement.getId()
                        + "\" sourceVisibility=\"" + firstAssociationEnd.getVisibility() + "\" sourceName=\""
                        + firstAssociationEnd.getName() + "\" sourceMin=\"" + firstMultiplicity.getLowerValue()
                        + "\" sourceMax=\"" + firstMultiplicity.getUpperValue()
                        + "\" sourceX=\"" + firstAssociationEnd.getPosX()
                        + "\" sourceY=\"" + firstAssociationEnd.getPosY() + "\"/>");
                printWriter.write("\n" + tab + halfTab + "<target entity=\"" + secondElement.getId()
                        + "\" targetVisibility=\"" + secondAssociationEnd.getVisibility()
                        + "\" targetName=\"" + secondAssociationEnd.getName()
                        + "\" targetMin=\"" + secondMultiplicity.getLowerValue()
                        + "\" targetMax=\"" + secondMultiplicity.getUpperValue()
                        + "\" targetX=\"" + secondAssociationEnd.getPosX()
                        + "\" targetY=\"" + secondAssociationEnd.getPosY() + "\"/>");
                printWriter.write("\n" + tab + "</association>");
            }
        }
    }

    private Element getFirstElement(Architecture architecture, String logPath, AssociationRelationship associationRelationship, AssociationEnd firstAssociationEnd, AssociationEnd secondAssociationEnd) {
        Element firstElement = architecture.findElementByNameInPackageAndSubPackage(firstAssociationEnd.getCLSClass().getName());
        if (firstElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Association " + associationRelationship.getId() + ":");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 1: "
                    + firstAssociationEnd.getCLSClass().getId() + " - " + firstAssociationEnd.getCLSClass().getName() + " not found");
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 2: "
                    + secondAssociationEnd.getCLSClass().getId() + " - " + secondAssociationEnd.getCLSClass().getName());
            return null;
        }
        return firstElement;
    }

    private Element getSecondElement(Architecture architecture, String logPath, AssociationEnd firstAssociationEnd, AssociationEnd secondAssociationEnd) {
        Element secondElement = architecture.findElementByNameInPackageAndSubPackage(secondAssociationEnd.getCLSClass().getName());
        if (secondElement == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 1: "
                    + firstAssociationEnd.getCLSClass().getId() + " - " + firstAssociationEnd.getCLSClass().getName());
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nElement 2: "
                    + secondAssociationEnd.getCLSClass().getId() + " - " + secondAssociationEnd.getCLSClass().getName() + " not found");
            return null;
        }
        return secondElement;
    }

    private int getIdRelationship(Architecture architecture, int idRelationship, AssociationRelationship associationRelationship) {
        if (associationRelationship.getId().length() == 0) {
            boolean ExistId = true;
            while (ExistId) {
                ExistId = false;
                for (Relationship r2 : architecture.getRelationshipHolder().getAllRelationships()) {
                    if (r2.getId().equals("ASSOCIATION#" + idRelationship)) {
                        idRelationship++;
                        ExistId = true;
                        break;
                    }
                }
            }
            associationRelationship.setId("ASSOCIATION#" + idRelationship);
            idRelationship++;
        }
        return idRelationship;
    }
}
