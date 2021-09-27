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
    public void save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        printWriter.write("\n" + halfTab + "<links>");
        ArrayList<String> variantStereotype = new ArrayList<>();
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint variationPoint = variability.getVariationPoint();
            if (variationPoint == null)
                continue;
            if (variationPoint.getVariationPointElement() == null)
                continue;
            if (!variantStereotype.contains(variationPoint.getVariationPointElement().getId()))
                variantStereotype.add(variationPoint.getVariationPointElement().getId());
            for (Variant variant : variationPoint.getVariants()) {
                if (!variantStereotype.contains(variant.getVariantElement().getId()))
                    variantStereotype.add(variant.getVariantElement().getId());
            }
        }
        for (br.otimizes.oplatool.architecture.representation.Class aClass : architecture.getClasses()) {
            linkClass(printWriter, architecture, variantStereotype, tab, aClass);
        }
        for (Interface anInterface : architecture.getInterfaces()) {
            linkInterface(printWriter, architecture, tab, anInterface);
        }
        for (br.otimizes.oplatool.architecture.representation.Package aPackage : architecture.getAllPackages()) {
            for (Concern concern : aPackage.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                printWriter.write("\n" + tab + "<link element=\"" + aPackage.getId() + "\" stereotype=\"" + concern.getId() + "\"/>");
            }
            for (br.otimizes.oplatool.architecture.representation.Class clazz : aPackage.getAllClasses()) {
                linkClass(printWriter, architecture, variantStereotype, tab, clazz);
            }
            addInterfaces(printWriter, architecture, variantStereotype, tab, aPackage);
        }
        ArrayList<String> variationPointStereotype = new ArrayList<>();
        variantStereotype = new ArrayList<>();
        ArrayList<String> discardedVariability = getDiscardedVariability(architecture);
        setVariability(architecture, printWriter, tab, variantStereotype, variationPointStereotype, discardedVariability);
        setStereotypes(architecture, printWriter, tab, variantStereotype, variationPointStereotype);
        printWriter.write("\n" + halfTab + "</links>");

    }

    private void setVariability(Architecture architecture, PrintWriter printWriter, String tab, ArrayList<String> variantStereotype,
                                ArrayList<String> variationPointStereotype, ArrayList<String> discardedVariability) {
        for (Variability variability : architecture.getAllVariabilities()) {
            if (discardedVariability.contains(variability.getId())) {
                continue;
            }
            VariationPoint variationPoint = variability.getVariationPoint();
            if (variationPoint == null)
                continue;
            if (variationPoint.getVariationPointElement() == null)
                continue;
            if (!variationPointStereotype.contains(variationPoint.getVariationPointElement().getId())) {
                printWriter.write("\n" + tab + "<link element=\"" + variationPoint.getVariationPointElement().getId()
                        + "\" stereotype=\"STEREOTYPE#3\"/>");
                variationPointStereotype.add(variationPoint.getVariationPointElement().getId());
            }
            for (Variant v : variationPoint.getVariants()) {
                if (!variantStereotype.contains(v.getVariantElement().getId())) {
                    printWriter.write("\n" + tab + "<link element=\"" + v.getVariantElement().getId() + "\" stereotype=\""
                            + architecture.findConcernByName(v.getVariantType()).getId() + "\"/>");
                    variantStereotype.add(v.getVariantElement().getId());
                }
            }
        }
    }

    private void setStereotypes(Architecture architecture, PrintWriter printWriter, String tab, ArrayList<String> variantStereotype,
                                ArrayList<String> variationPointStereotype) {
        for (String vpID : variationPointStereotype) {
            if (!variantStereotype.contains(vpID)) {
                Element clazz = architecture.findElementById(vpID);
                if (clazz.isMandatory())
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
                else
                    printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");
            }
        }
    }

    public ArrayList<String> getDiscardedVariability(Architecture architecture) {
        ArrayList<String> discardedVariability = new ArrayList<>();
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint variationPoint = variability.getVariationPoint();
            if (variationPoint == null) {
                continue;
            }
            if (variationPoint.getVariationPointElement() == null) {
                continue;
            }
            if (architecture.findElementById(variationPoint.getVariationPointElement().getId()) == null) {
                discardedVariability.add(variability.getId());
                continue;
            }
            for (Variant variant : variability.getVariants()) {
                if (architecture.findElementById(variant.getVariantElement().getId()) == null) {
                    discardedVariability.add(variability.getId());
                    break;
                }
            }
        }
        return discardedVariability;
    }

    private void saveSubPackageLink(br.otimizes.oplatool.architecture.representation.Package pkg, PrintWriter printWriter,
                                    Architecture architecture, ArrayList<String> variantStereotype) {
        String tab = "    ";
        String concernID = "";
        for (Package subPkg : pkg.getNestedPackages()) {
            for (Concern concern : subPkg.getConcernsOnlyFromElementWithoutMethodOrAttribute()) {
                concernID = architecture.findConcernByName(concern.getName()).getId();
                printWriter.write("\n" + tab + "t<link element=\"" + subPkg.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
            for (Class aClass : subPkg.getAllClasses()) {
                linkClass(printWriter, architecture, variantStereotype, tab, aClass);
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
        for (Concern concern : clazz.getOwnConcerns()) {
            concernID = architecture.findConcernByName(concern.getName()).getId();
            printWriter.write("\n" + tab + "<link element=\"" + clazz.getId() + "\" stereotype=\"" + concernID + "\"/>");
        }
        for (Method method : clazz.getAllMethods()) {
            for (Concern c : method.getOwnConcerns()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "<link element=\"" + method.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
        }
        for (Attribute attribute : clazz.getAllAttributes()) {
            for (Concern c : attribute.getOwnConcerns()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "<link element=\"" + attribute.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
        }
    }

    private void addInterfaces(PrintWriter printWriter, Architecture architecture, ArrayList<String> variantStereotype, String tab, Package subPkg) {
        for (Interface interfaceFromSubPackage : subPkg.getAllInterfaces()) {
            linkInterface(printWriter, architecture, tab, interfaceFromSubPackage);
        }
        saveSubPackageLink(subPkg, printWriter, architecture, variantStereotype);
    }

    private void linkInterface(PrintWriter printWriter, Architecture architecture, String tab, Interface interfaceToLink) {
        String concernID;
        if (interfaceToLink.isMandatory())
            printWriter.write("\n" + tab + "<link element=\"" + interfaceToLink.getId() + "\" stereotype=\"STEREOTYPE#1\"/>");
        else
            printWriter.write("\n" + tab + "<link element=\"" + interfaceToLink.getId() + "\" stereotype=\"STEREOTYPE#2\"/>");

        for (Concern concern : interfaceToLink.getOwnConcerns()) {
            concernID = architecture.findConcernByName(concern.getName()).getId();
            printWriter.write("\n" + tab + "<link element=\"" + interfaceToLink.getId() + "\" stereotype=\"" + concernID + "\"/>");
        }
        for (Method method : interfaceToLink.getMethods()) {
            for (Concern c : method.getOwnConcerns()) {
                concernID = architecture.findConcernByName(c.getName()).getId();
                printWriter.write("\n" + tab + "<link element=\"" + method.getId() + "\" stereotype=\"" + concernID + "\"/>");
            }
        }
    }
}
