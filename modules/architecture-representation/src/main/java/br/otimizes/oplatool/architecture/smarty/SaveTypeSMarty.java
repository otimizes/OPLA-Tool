package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

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
     * @param printWriter  - used to save the type to file
     */
    public void save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        printWriter.write("\n" + halfTab + "<types>");
        if (architecture.getTypes().size() == 0) {
            GenerateTypeSMarty.getInstance().generate(architecture);
        }
        ArrayList<TypeSmarty> addedType = new ArrayList<>();
        for (TypeSmarty typeSmarty : architecture.getTypes()) {
            if (typeSmarty.isStandard()) {
                addTypeSmarty(printWriter, tab, addedType, typeSmarty);
            } else {
                if (architecture.findElementById(typeSmarty.getId()) != null) {
                    addTypeSmarty(printWriter, tab, addedType, typeSmarty);
                }
            }
        }
        addClass(printWriter, tab, addedType, architecture.getClasses());
        addInterface(printWriter, tab, addedType, architecture.getInterfaces());
        for (br.otimizes.oplatool.architecture.representation.Package aPackage : architecture.getAllPackages()) {
            addClass(printWriter, tab, addedType, aPackage.getAllClasses());
            addInterface(printWriter, tab, addedType, aPackage.getAllInterfaces());
            saveTypeSubPackage(aPackage, addedType, printWriter);
        }
        printWriter.write("\n" + halfTab + "</types>");
    }

    private void addTypeSmarty(PrintWriter printWriter, String tab, ArrayList<TypeSmarty> addedType, TypeSmarty typeSmarty) {
        printWriter.write("\n" + tab + "<type id=\"" + typeSmarty.getId() + "\" path=\"" + typeSmarty.getPath()
                + "\" name=\"" + typeSmarty.getName() + "\" value=\"" + typeSmarty.getValue() + "\" primitive=\"" + typeSmarty.isPrimitive()
                + "\" standard=\"" + typeSmarty.isStandard() + "\"/>");
        addedType.add(typeSmarty);
    }

    private void addClass(PrintWriter printWriter, String tab, ArrayList<TypeSmarty> addedType, Set<Class> classes) {
        for (Class aClass : classes) {
            if (!doesTypeSmartyExist(addedType, aClass.getId())) {
                printWriter.write("\n" + tab + "<type id=\"" + aClass.getId() + "\" path=\"" + aClass.getName()
                        + "\" name=\"" + aClass.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
            }
        }
    }

    private void addInterface(PrintWriter printWriter, String tab, ArrayList<TypeSmarty> addedType, Set<Interface> interfaces) {
        for (Interface anInterface : interfaces) {
            if (!doesTypeSmartyExist(addedType, anInterface.getId())) {
                printWriter.write("\n" + tab + "<type id=\"" + anInterface.getId() + "\" path=\"" + anInterface.getName()
                        + "\" name=\"" + anInterface.getName() + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
            }
        }
    }

    /**
     * Save list type from sub package to file
     *
     * @param pkg         - package that has subpackage
     * @param addedType   - type that has added to file
     * @param printWriter - used to save file
     */
    private void saveTypeSubPackage(br.otimizes.oplatool.architecture.representation.Package pkg,
                                    ArrayList<TypeSmarty> addedType, PrintWriter printWriter) {
        String tab = "    ";
        for (Package aPackage : pkg.getNestedPackages()) {
            for (Class aClass : aPackage.getAllClasses()) {
                if (!doesTypeSmartyExist(addedType, aClass.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + aClass.getId() + "\" path=\"\" name=\"" + aClass.getName()
                            + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            for (Interface anInterface : aPackage.getAllInterfaces()) {
                if (!doesTypeSmartyExist(addedType, anInterface.getId())) {
                    printWriter.write("\n" + tab + "<type id=\"" + anInterface.getId() + "\" path=\"\" name=\"" + anInterface.getName()
                            + "\" value=\"null\" primitive=\"false\" standard=\"false\"/>");
                }
            }
            saveTypeSubPackage(aPackage, addedType, printWriter);
        }
    }

    /**
     * Verify if the type has already added to file
     *
     * @param addedType - type that has already added to file
     * @param id        - id of the new type to be compared
     * @return -boolean. true if find the type, else false
     */
    private boolean doesTypeSmartyExist(ArrayList<TypeSmarty> addedType, String id) {
        for (TypeSmarty typeSmarty : addedType) {
            if (typeSmarty.getId().equals(id))
                return true;
        }
        return false;
    }
}
