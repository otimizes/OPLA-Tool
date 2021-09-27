package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Variability;
import br.otimizes.oplatool.architecture.representation.Variant;
import br.otimizes.oplatool.architecture.representation.VariationPoint;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save all variability to file
 */
public class SaveVariabilitySMarty {
    public SaveVariabilitySMarty() {
    }

    private static final SaveVariabilitySMarty INSTANCE = new SaveVariabilitySMarty();

    public static SaveVariabilitySMarty getInstance() {
        return INSTANCE;
    }

    /**
     * This class save variability to file
     *
     * @param architecture - architecture to be decoded
     * @param printWriter  - used to save a string in file
     * @param logPath      - path to save log if has a error
     */
    public void save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int varID = 0;
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint vp = variability.getVariationPoint();
            if (isVariationPointEmpty(architecture, logPath, variability, vp)) continue;
            if (variability.constraint == null) {
                for (Variant variant : variability.getVariants()) {
                    if (doesAlternativeAndTypeEqualsTo(variability, variant, "alternative_OR", "Inclusive")) break;
                    if (doesAlternativeAndTypeEqualsTo(variability, variant, "alternative_XOR", "Exclusive")) break;
                }
            }
            if (variability.getId() == null) {
                varID = getNextIdFromVariability(architecture, varID, variability);
            }
            printWriter.write("\n" + tab + "<variability id=\"" + variability.getId() + "\" name=\"" + variability.getName()
                    + "\" variationPoint=\"" + vp.getVariationPointElement().getId() + "\" constraint=\"" + variability.getConstraint()
                    + "\" bindingTime=\"" + variability.getBindingTime() + "\" allowsBindingVar=\"" + variability.allowAddingVar()
                    + "\" min=\"" + variability.getMinSelection() + "\" max=\"" + variability.getMaxSelection() + "\">");
            for (Variant v : variability.getVariants()) {
                printWriter.write("\n" + tab + halfTab + "<variant id=\"" + v.getVariantElement().getId() + "\"/>");
            }
            printWriter.write("\n" + tab + "</variability>");
        }
    }

    private boolean isVariationPointEmpty(Architecture architecture, String logPath, Variability variability, VariationPoint variationPoint) {
        if (variationPoint == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Variability "
                    + variability.getId() + " - " + variability.getName() + ":");
            logType(logPath, variability);
        } else {
            if (isVariationPointElementEmpty(logPath, variability, variationPoint)) return true;
            if (isVariationPointElementEmpty(architecture, logPath, variability, variationPoint)) return true;
            return mustBeDiscarded(architecture, logPath, variability, variationPoint);
        }
        return false;
    }

    private boolean isVariationPointElementEmpty(String logPath, Variability variability, VariationPoint vp) {
        if (vp.getVariationPointElement() == null) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Variability "
                    + variability.getId() + " - " + variability.getName() + ":");
            logType(logPath, variability);
            return true;
        }
        return false;
    }

    private boolean isVariationPointElementEmpty(Architecture architecture, String logPath, Variability variability, VariationPoint vp) {
        if (architecture.findElementById(vp.getVariationPointElement().getId()) == null) {
            logDiscarded(logPath, variability, vp);
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point "
                    + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName() + " not found");
            return true;
        }
        return false;
    }

    private boolean mustBeDiscarded(Architecture architecture, String logPath, Variability variability, VariationPoint vp) {
        boolean discard = false;
        for (Variant variant : variability.getVariants()) {
            if (architecture.findElementById(variant.getVariantElement().getId()) == null) {
                discard = true;
                logDiscarded(logPath, variability, vp);
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariant " + variant.getVariantElement().getId()
                        + " - " + variant.getVariantElement().getName() + " not found");
            }
        }
        return discard;
    }

    private boolean doesAlternativeAndTypeEqualsTo(Variability variability, Variant variant, String alternative, String type) {
        if (variant.getVariantType().equals(alternative)) {
            variability.setConstraint(type);
            return true;
        }
        return false;
    }

    private int getNextIdFromVariability(Architecture architecture, int varID, Variability variability) {
        String varIDName;
        varID++;
        varIDName = "VARIABILITY#" + varID;
        while (variabilityIDExist(architecture, varIDName)) {
            varID++;
            varIDName = "VARIABILITY#" + varID;
        }
        variability.setId(varIDName);
        return varID;
    }

    private void logType(String logPath, Variability variability) {
        SaveStringToFile.getInstance().appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType()
                + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
        SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point is NULL");
        SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariability optional will be converted to concern optional");
        SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariability mandatory will be converted to concern mandatory");
    }

    private void logDiscarded(String logPath, Variability variability, VariationPoint vp) {
        SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Variability " + variability.getId()
                + " - " + variability.getName() + ":");
        SaveStringToFile.getInstance().appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType()
                + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
        SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point: " + vp.getVariationPointElement().getId()
                + " - " + vp.getVariationPointElement().getName());
        for (Variant variant : variability.getVariants()) {
            SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariant: " + variant.getVariantElement().getId() + " - "
                    + variant.getVariantElement().getName());
        }
    }

    /**
     * Verify if exist variability with the specific id
     *
     * @param architecture - architecture to be decoded
     * @param id           - new id to be compared
     * @return boolean true if exist, else false
     */
    private boolean variabilityIDExist(Architecture architecture, String id) {
        for (Variability variability : architecture.getAllVariabilities()) {
            if (variability.getId() != null) {
                if (variability.getId().equals(id))
                    return true;
            }
        }
        return false;
    }
}
