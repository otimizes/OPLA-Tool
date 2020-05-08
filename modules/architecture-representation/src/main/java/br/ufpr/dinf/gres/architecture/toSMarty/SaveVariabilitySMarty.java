package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Variability;
import br.ufpr.dinf.gres.architecture.representation.Variant;
import br.ufpr.dinf.gres.architecture.representation.VariationPoint;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SaveVariabilitySMarty {

    public SaveVariabilitySMarty(Architecture architecture, PrintWriter printWriter, String logPath) {
        String halfTab = "  ";
        String tab = "    ";
        int varID = 0;
        String varIDName; // = "VARIABILITY#" + varID;
        for (Variability variability : architecture.getAllVariabilities()) {
            VariationPoint vp = variability.getVariationPoint();
            if (vp == null) {
                appendStrToFile(logPath, "\n\nDiscart Variability " + variability.getId() + " - " + variability.getName() + ":");
                appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                appendStrToFile(logPath, "\nVariation Point NULL");
                appendStrToFile(logPath, "\nVariabilidade do tipo optional será convertido para stereótipo optional");
                appendStrToFile(logPath, "\nVariabilidade do tipo mandatory será convertido para stereótipo mandatory");
                continue;
            }
            if (vp.getVariationPointElement() == null) {
                appendStrToFile(logPath, "\n\nDiscart Variability " + variability.getId() + " - " + variability.getName() + ":");
                appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                appendStrToFile(logPath, "\nVariation Point NULL");
                appendStrToFile(logPath, "\nVariabilidade do tipo optional será convertido para stereótipo optional");
                appendStrToFile(logPath, "\nVariabilidade do tipo mandatory será convertido para stereótipo mandatory");
                continue;
            }
            if (architecture.findElementById(vp.getVariationPointElement().getId()) == null) {

                appendStrToFile(logPath, "\n\nDiscart Variability " + variability.getId() + " - " + variability.getName() + ":");
                appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                appendStrToFile(logPath, "\nVariation Point: " + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName());
                for (Variant v : variability.getVariants()) {
                    appendStrToFile(logPath, "\nVariant: " + v.getVariantElement().getId() + " - " + v.getVariantElement().getName());
                }
                appendStrToFile(logPath, "\nVariation Point " + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName() + " not found");

                continue;
            }
            boolean discart = false;
            for (Variant v : variability.getVariants()) {
                if (architecture.findElementById(v.getVariantElement().getId()) == null) {
                    discart = true;
                    appendStrToFile(logPath, "\n\nDiscart Variability " + variability.getId() + " - " + variability.getName() + ":");
                    appendStrToFile(logPath, "\nType: " + variability.getVariants().get(0).getVariantType() + ", Binding Time: " + variability.getBindingTime() + ", Allow Binding Var:" + variability.allowAddingVar());
                    appendStrToFile(logPath, "\nVariation Point: " + vp.getVariationPointElement().getId() + " - " + vp.getVariationPointElement().getName());
                    for (Variant v2 : variability.getVariants()) {
                        appendStrToFile(logPath, "\nVariant: " + v2.getVariantElement().getId() + " - " + v2.getVariantElement().getName());
                    }
                    appendStrToFile(logPath, "\nVariant " + v.getVariantElement().getId() + " - " + v.getVariantElement().getName() + " not found");
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
                //System.out.println("id null");
                while (variabilityIDExist(architecture, varIDName)) {
                    varID++;
                    varIDName = "VARIABILITY#" + varID;
                }
                variability.setId(varIDName);
            }
            //printWriter.write("\n"+tab+"<variability id=\""+variability.getId()+"\" name=\""+variability.getName()+"\" variationPoint=\""+vp.getVariationPointElement().getId()+"\" constraint=\""+variability.getConstraint()+"\" bindingTime=\""+variability.getBindingTime()+"\" allowsBindingVar=\""+variability.allowAddingVar()+"\" min=\""+variability.getMinSelection()+"\" max=\""+variability.getMaxSelection()+"\">");
            printWriter.write("\n" + tab + "<variability id=\"" + variability.getId() + "\" name=\"" + variability.getName() + "\" variationPoint=\"" + vp.getVariationPointElement().getId() + "\" constraint=\"" + variability.getConstraint() + "\" bindingTime=\"" + variability.getBindingTime() + "\" allowsBindingVar=\"" + variability.allowAddingVar() + "\" min=\"" + variability.getMinSelection() + "\" max=\"" + variability.getMaxSelection() + "\">");
            for (Variant v : variability.getVariants()) {
                //System.out.println("v:"+v.getVariantElement().getId());
                printWriter.write("\n" + tab + halfTab + "<variant id=\"" + v.getVariantElement().getId() + "\"/>");
            }
            printWriter.write("\n" + tab + "</variability>");
        }

    }


    public static void appendStrToFile(String fileName,
                                       String str) {
        try {
            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileName, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

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
