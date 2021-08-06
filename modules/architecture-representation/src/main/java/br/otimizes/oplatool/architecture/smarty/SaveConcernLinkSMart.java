package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class save the relation with concern and element to file
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
     *
     * @param architecture - architecture to be decoded
     * @param printWriter  - used to save a string in file
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
        for (br.otimizes.oplatool.architecture.representation.Class clazz : architecture.getClasses()) {
            linkClass(printWriter, architecture, variantStereotype, tab, clazz);
        }
        for (Interface clazz : architecture.getInterfaces()) {
            linkInterface(printWriter, architecture, tab, clazz);
        }
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            for (Concern c : pkg.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                printWriter.write("\n" + tab + "<link element=\"" + pkg.getId() + "\" stereotype=\"" + c.getId() + "\"/>");
            }
            for (br.otimizes.oplatool.architecture.representation.Class clazz : pkg.getAllClasses()) {
                linkClass(printWriter, architecture, variantStereotype, tab, clazz);
            }
            addInterfaces(printWriter, architecture, variantStereotype, tab, pkg);
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

    public ArrayList<String> getDiscardedVariability(Architecture architecture) {
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

    private void saveSubPackageLink(br.otimizes.oplatool.architecture.representation.Package pkg, PrintWriter printWriter, Architecture architecture, ArrayList<String> variantStereotype) {
        String tab = "    ";
        String concernID = "";
        for (Package subPkg : pkg.getNestedPackages()) {
            for (Concern c : subPkg.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "t<link element=\"" + subPkg.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
            for (Class clazz : subPkg.getAllClasses()) {
                linkClass(printWriter, architecture, variantStereotype, tab, clazz);
            }
            addInterfaces(printWriter, architecture, variantStereotype, tab, subPkg);
        }
    }

    private void linkClass(PrintWriter printWriter, Architecture architecture, ArrayList<String> variantStereotype, String tab, Class clazz) {
        String concernID;
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

    private void addInterfaces(PrintWriter printWriter, Architecture architecture, ArrayList<String> variantStereotype, String tab, Package subPkg) {
        for (Interface inter : subPkg.getAllInterfaces()) {
            linkInterface(printWriter, architecture, tab, inter);
        }
        saveSubPackageLink(subPkg, printWriter, architecture, variantStereotype);
    }

    private void linkInterface(PrintWriter printWriter, Architecture architecture, String tab, Interface inter) {
        String concernID;
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
}
