package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.toSMarty.relationship.SaveAssociationSMarty;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class save the relation with concern and element to file
 *
 */
public class SaveConcernLinkSMart {

    public SaveConcernLinkSMart() {
    }

    private static final SaveConcernLinkSMart INSTANCE = new SaveConcernLinkSMart();

    public static SaveConcernLinkSMart getInstance() {
        return INSTANCE;
    }

    /**
     * This class save the relation with concern and element of architecture
     * This function verify stereotypes of variability and normal stereotypes of element
     * @param architecture - architecture to be decoded
     * @param printWriter - used to save a string in file
     */
    public void Save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        printWriter.write("\n" + halfTab + "<links>");
        ArrayList<String> variantStereotype = new ArrayList<>();
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint vp = variability.getVariationPoint();
            if (vp == null)
                continue;
            if (vp.getVariationPointElement() == null)
                continue;
            if (!variantStereotype.contains(vp.getVariationPointElement().getId()))
                variantStereotype.add(vp.getVariationPointElement().getId());
            for (Variant v : vp.getVariants()) {
                if (!variantStereotype.contains(v.getVariantElement().getId()))
                    variantStereotype.add(v.getVariantElement().getId());
            }
        }
        for (Class clazz : architecture.getClasses()) {
            String concernID = "";
            if (!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                if (clazz.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
            }
            for (Concern c : clazz.getOwnConcerns()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
            for (Method m : clazz.getAllMethods()) {
                for (Concern c : m.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
            }
            for (Attribute m : clazz.getAllAttributes()) {
                for (Concern c : m.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
            }
        }
        for (Interface clazz : architecture.getInterfaces()) {
            String concernID = "";
            if (clazz.isMandatory())
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
            else
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
            for (Concern c : clazz.getOwnConcerns()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
            for (Method m : clazz.getMethods()) {
                for (Concern c : m.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
            }
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Concern c : pkg.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                printWriter.write("\n" + tab + "<link element=\"" + pkg.getId() + "\" stereotype=\"" + c.getId() + "\"/>");
            }
            String concernID = "";
            for (Class clazz : pkg.getAllClasses()) {
                if (!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                    if (clazz.isMandatory())
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                }
                for (Concern c : clazz.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : clazz.getAllMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
                for (Attribute m : clazz.getAllAttributes()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (clazz.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");

                for (Concern c : clazz.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : clazz.getMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }
            saveSubPackageLink(pkg, printWriter, architecture, variantStereotype);
        }
        ArrayList<String> variationPointStereotype = new ArrayList<>();
        variantStereotype = new ArrayList<>();
        ArrayList<String> discardedVariability = getDiscardedVariability(architecture);
        for (Variability variability : architecture.getAllVariabilities()) {
            if (discardedVariability.contains(variability.getId())) {
                continue;
            }
            VariationPoint vp = variability.getVariationPoint();
            if (vp == null)
                continue;
            if (vp.getVariationPointElement() == null)
                continue;
            if (!variationPointStereotype.contains(vp.getVariationPointElement().getId())) {
                printWriter.write("\n" + tab + "<link element=\"" + vp.getVariationPointElement().getId() + "\" stereotype=\"STEREOTYPE#3\"/>");
                variationPointStereotype.add(vp.getVariationPointElement().getId());
            }
            for (Variant v : vp.getVariants()) {
                if (!variantStereotype.contains(v.getVariantElement().getId())) {
                    printWriter.write("\n" + tab + "<link element=\"" + v.getVariantElement().getId() + "\" stereotype=\"" + architecture.findConcernByName(v.getVariantType()).getId() + "\"/>");
                    variantStereotype.add(v.getVariantElement().getId());
                }
            }
        }
        for (String vpID : variationPointStereotype) {
            if (!variantStereotype.contains(vpID)) {
                Element clazz = architecture.findElementById(vpID);
                if (clazz.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
            }
        }
        printWriter.write("\n" + halfTab + "</links>");

    }

    /**
     * this function get the list of discarded variability to not be used to save concern
     * discarded variability will be ignored
     *
     * @param architecture
     * @return discardedVariability - ArrayList that contain the id of discarded variability
     */
    public ArrayList<String> getDiscardedVariability(Architecture architecture){
        ArrayList<String> discardedVariability = new ArrayList<>();
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint vp = variability.getVariationPoint();
            if (vp == null) {
                continue;
            }
            if (vp.getVariationPointElement() == null) {
                continue;
            }
            if (architecture.findElementById(vp.getVariationPointElement().getId()) == null) {
                discardedVariability.add(variability.getId());
                continue;
            }
            for (Variant v : variability.getVariants()) {
                if (architecture.findElementById(v.getVariantElement().getId()) == null) {
                    discardedVariability.add(variability.getId());
                    break;
                }
            }
        }
        return discardedVariability;
    }

    /**
     * this method will save recursively all concern of elements in subpackages
     * @param pkg
     * @param printWriter
     * @param architecture
     * @param variantStereotype
     */
    private void saveSubPackageLink(Package pkg, PrintWriter printWriter, Architecture architecture, ArrayList<String> variantStereotype) {
        String tab = "    ";
        String concernID = "";
        for (Package subPkg : pkg.getNestedPackages()) {
            for (Concern c : subPkg.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "t<link element=\"" + subPkg.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
            for (Class clazz : subPkg.getAllClasses()) {
                if (!variantStereotype.contains(clazz.getId()) && !variantStereotype.contains(clazz.getId())) {
                    if (clazz.isMandatory())
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                    else
                        printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
                }
                for (Concern c : clazz.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : clazz.getAllMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
                for (Attribute m : clazz.getAllAttributes()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }
            for (Interface inter : subPkg.getAllInterfaces()) {
                if (inter.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + inter.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + inter.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");

                for (Concern c : inter.getOwnConcerns()) {
                    concernID = architecture.findConcernByName(c.getName()).getId();
                    printWriter.write("\n" + tab + "<link element=\"" + inter.getId() + "\" stereotype=\"" + concernID + "\"/>");
                }
                for (Method m : inter.getMethods()) {
                    for (Concern c : m.getOwnConcerns()) {
                        concernID = architecture.findConcernByName(c.getName()).getId();
                        printWriter.write("\n" + tab + "<link element=\"" + m.getId() + "\" stereotype=\"" + concernID + "\"/>");
                    }
                }
            }
            saveSubPackageLink(subPkg, printWriter, architecture, variantStereotype);
        }
    }


}
