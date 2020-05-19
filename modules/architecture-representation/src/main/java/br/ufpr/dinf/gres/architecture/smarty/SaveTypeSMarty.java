package br.ufpr.dinf.gres.architecture.smarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.TypeSmarty;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Save the list of types in the file
 * If not has a list of types, create a new list
 */
public class SaveTypeSMarty {

    public SaveTypeSMarty() {
    }

    private static final SaveTypeSMarty INSTANCE = new SaveTypeSMarty();

    public static SaveTypeSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * Save the list of types in the file
     *
     * @param architecture - the architecture to be decoded
     * @param printWriter - used to save the type to file
     */
    public void Save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        printWriter.write("\n" + halfTab + "<types>");
        if (architecture.getTypes().size() == 0) {
            GenerateTypeSMarty.getInstance().generate(architecture);
        }
        ArrayList<TypeSmarty> adicionedType = new ArrayList<>();
        for (TypeSmarty ts : architecture.getTypes()) {
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

    /**
     * Save list type from sub package to file
     *
     * @param pkg1 - package that has subpackage
     * @param addedType - type that has added to file
     * @param printWriter - used to save file
     */
    private void saveTypeSubPackage(Package pkg1, ArrayList<TypeSmarty> addedType, PrintWriter printWriter) {
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                if (!typeSmartyExist(addedType, clazz.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            for (Interface clazz : pkg.getAllInterfaces()) {
                if (!typeSmartyExist(addedType, clazz.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + clazz.getId() + "\" path=\"\" name=\"" + clazz.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            saveTypeSubPackage(pkg, addedType, printWriter);
        }
    }

    /**
     * Verify if the type has already added to file
     *
     * @param addedType - type that has already added to file
     * @param id - id of the new type to be compared
     * @return -boolean. true if find the type, else false
     */
    private boolean typeSmartyExist(ArrayList<TypeSmarty> addedType, String id) {
        for (TypeSmarty ts : addedType) {
            if (ts.getId().equals(id))
                return true;
        }
        return false;
    }
}
