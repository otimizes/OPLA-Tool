package br.ufpr.dinf.gres.architecture.smarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Variability;
import br.ufpr.dinf.gres.architecture.representation.Variant;
import br.ufpr.dinf.gres.architecture.representation.VariationPoint;
import br.ufpr.dinf.gres.architecture.smarty.util.SaveStringToFile;

import java.io.PrintWriter;

/**
 * This class save all variability to file
 *
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
     * @param printWriter - used to save a string in file
     * @param logPath - path to save log if has a error
     */
    public void Save(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int varID = 0;
        String varIDName;
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint vp = variability.getVariationPoint();
            if (vp == null) {
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscard Variability " + variability.getId() + " - " + variability.getName() + ":");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point is NULL");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariability optional will be converted to concern optional");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariability mandatory will be converted to concern mandatory");
                continue;
            }
            if (vp.getVariationPointElement() == null) {
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Variability " + variability.getId() + " - " + variability.getName() + ":");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point is NULL");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariability optional will be converted to concern optional");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariability mandatory will be converted to concern mandatory");
                continue;
            }
            if (architecture.findElementById(vp.getVariationPointElement().getId()) == null) {

                SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Variability " + variability.getId() + " - " + variability.getName() + ":");
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point: " + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName());
                for (Variant v : variability.getVariants()) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariant: " + v.getVariantElement().getId() + " - " + v.getVariantElement().getName());
                }
                SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point " + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName() + " not found");

                continue;
            }
            boolean discart = false;
            for (Variant v : variability.getVariants()) {
                if (architecture.findElementById(v.getVariantElement().getId()) == null) {
                    discart = true;
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\n\nDiscart Variability " + variability.getId() + " - " + variability.getName() + ":");
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariation Point: " + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName());
                    for (Variant v2 : variability.getVariants()) {
                        SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariant: " + v2.getVariantElement().getId() + " - " + v2.getVariantElement().getName());
                    }
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nVariant " + v.getVariantElement().getId() + " - " + v.getVariantElement().getName() + " not found");
                }
            }
            if (discart) {
                continue;
            }
            if (variability.constraint == null) {
                for (Variant variant : variability.getVariants()) {
                    if (variant.getVariantType().equals("alternative_OR")) {
                        variability.setConstraint("Inclusive");
                        break;
                    }
                    if (variant.getVariantType().equals("alternative_XOR")) {
                        variability.setConstraint("Exclusive");
                        break;
                    }
                }
            }
            if (variability.getId() == null) {
                varID++;
                varIDName = "VARIABILITY#" + varID;
                while (variabilityIDExist(architecture, varIDName)) {
                    varID++;
                    varIDName = "VARIABILITY#" + varID;
                }
                variability.setId(varIDName);
            }
            printWriter.write("\n" + tab + "<variability id=\"" + variability.getId() + "\" name=\"" + variability.getName() + "\" variationPoint=\"" + vp.getVariationPointElement().getId() + "\" constraint=\"" + variability.getConstraint() + "\" bindingTime=\"" + variability.getBindingTime() + "\" allowsBindingVar=\"" + variability.allowAddingVar() + "\" min=\"" + variability.getMinSelection() + "\" max=\"" + variability.getMaxSelection() + "\">");
            for (Variant v : variability.getVariants()) {
                printWriter.write("\n" + tab + halfTab + "<variant id=\"" + v.getVariantElement().getId() + "\"/>");
            }
            printWriter.write("\n" + tab + "</variability>");
        }
    }

    /**
     * Verify if exist variability with the specific id
     *
     * @param architecture - architecture to be decoded
     * @param id - new id to be compared
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
