package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.TypeSmarty;

import java.io.PrintWriter;
import java.util.ArrayList;

public class SaveTypeSMarty {

    public SaveTypeSMarty(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        printWriter.write("\n" + halfTab + "<types>");
        // add types
        if (architecture.getLstTypes().size() == 0) {
            new GenerateTypeSMarty().generate(architecture);
        }
        ArrayList<TypeSmarty> adicionedType = new ArrayList<>();

        for (TypeSmarty ts : architecture.getLstTypes()) {
            if (ts.isStandard()) {
                printWriter.write("\n" + tab + "<type id=\"" + ts.getId() + "\" path=\"" + ts.getPath() + "\" name=\"" + ts.getName() + "\" value=\"" + ts.getValue() + "\" primitive=\"" + ts.isPrimitive() + "\" standard=\"" + ts.isStandard() + "\"/>");
                adicionedType.add(ts);
            } else {
                if (architecture.findElementById(ts.getId()) != null) {
                    printWriter.write("\n" + tab + "<type id=\"" + ts.getId() + "\" path=\"" + ts.getPath() + "\" name=\"" + ts.getName() + "\" value=\"" + ts.getValue() + "\" primitive=\"" + ts.isPrimitive() + "\" standard=\"" + ts.isStandard() + "\"/>");
                    adicionedType.add(ts);
                }
            }
        }


        for (Class clazz : architecture.getClasses()) {
            if (!typeSmartyExist(adicionedType, clazz.getId())) {
                printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"" + clazz.getName() + "\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
            }
        }
        for (Interface clazz : architecture.getInterfaces()) {
            if (!typeSmartyExist(adicionedType, clazz.getId())) {
                printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"" + clazz.getName() + "\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
            }
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                if (!typeSmartyExist(adicionedType, clazz.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"" + clazz.getName() + "\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (!typeSmartyExist(adicionedType, clazz.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"" + clazz.getName() + "\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            saveTypeSubPackage(pkg, adicionedType, printWriter);
        }
        printWriter.write("\n" + halfTab + "</types>");
    }

    private void saveTypeSubPackage(Package pkg1, ArrayList<TypeSmarty> adicionedType, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                if (!typeSmartyExist(adicionedType, clazz.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (!typeSmartyExist(adicionedType, clazz.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            saveTypeSubPackage(pkg, adicionedType, printWriter);
        }
    }

    private boolean typeSmartyExist(ArrayList<TypeSmarty> lst, String id) {
        for (TypeSmarty ts : lst) {
            if (ts.getId().equals(id))
                return true;
        }
        return false;
    }
}
