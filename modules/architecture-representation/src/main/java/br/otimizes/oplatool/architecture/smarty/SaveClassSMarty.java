package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.representation.*;

import java.io.PrintWriter;

/**
 * Save all classes from architecture to file
 */
public class SaveClassSMarty {

    public SaveClassSMarty() {
    }

    private static final SaveClassSMarty INSTANCE = new SaveClassSMarty();

    public static SaveClassSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * Save all classes from architecture to file
     *
     * @param architecture - the architecture to be decoded
     * @param printWriter  - used to save the type to file
     */
    public void save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Class classFromArchitecture : architecture.getClasses()) {
            printWriter.write("\n" + tab + "<class id=\"" + classFromArchitecture.getId() + "\" name=\""
                    + classFromArchitecture.getName() + "\" mandatory=\"" + classFromArchitecture.isMandatory()
                    + "\" x=\"" + classFromArchitecture.getPosX() + "\" y=\"" + classFromArchitecture.getPosY()
                    + "\" globalX=\"" + classFromArchitecture.getGlobalPosX() + "\" globalY=\""
                    + classFromArchitecture.getGlobalPosY() + "\"  abstract=\"" + classFromArchitecture.isAbstract()
                    + "\" final=\"" + classFromArchitecture.isAbstract() + "\" height=\"" + classFromArchitecture.getHeight()
                    + "\" width=\"" + classFromArchitecture.getWidth() + "\" parent=\"\">");
            printWriter.write("\n" + tab + halfTab + "<description>" + classFromArchitecture.getStringComments() + "</description>");

            addAttributes(architecture, printWriter, halfTab, tab, classFromArchitecture);
        }
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                addClass(printWriter, halfTab, tab, pkg, clazz);
                addAttributes(architecture, printWriter, halfTab, tab, clazz);
            }
            saveClassInSubpackage(pkg, printWriter, architecture);
        }
    }

    private void addAttributes(Architecture architecture, PrintWriter printWriter, String halfTab, String tab, Class clazz) {
        for (Attribute attribute : clazz.getAllAttributes()) {
            TypeSmarty typeS = architecture.findTypeSMartyByName(attribute.getType());
            if (attribute.getName().length() == 0) {
                attribute.setName(typeS.getName());
            }
            printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + attribute.getId() + "\" name=\""
                    + attribute.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + attribute.getVisibility()
                    + "\" static=\"" + attribute.isStatic() + "\" final=\"" + attribute.isFinal() + "\"/>");
        }
        for (Method method : clazz.getAllMethods()) {
            SaveElementSMarty.addAbstractMethod(architecture, printWriter, halfTab, tab, method);
        }
        printWriter.write("\n" + tab + "</class>");
    }

    /**
     * Recursively save all class that is in the subpackage
     *
     * @param pkg          - the package that has a subpackage
     * @param printWriter  - used to save the type to file
     * @param architecture - the architecture to be decoded
     */
    private void saveClassInSubpackage(br.otimizes.oplatool.architecture.representation.Package pkg, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package nestedPackage : pkg.getNestedPackages()) {
            for (Class classFromNestedPackage : nestedPackage.getAllClasses()) {
                addClass(printWriter, halfTab, tab, nestedPackage, classFromNestedPackage);
                for (Attribute attribute : classFromNestedPackage.getAllAttributes()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(attribute.getType());
                    printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + attribute.getId() + "\" name=\""
                            + attribute.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + attribute.getVisibility()
                            + "\" static=\"" + attribute.isStatic() + "\" final=\"" + attribute.isFinal() + "\"/>");
                }
                for (Method method : classFromNestedPackage.getAllMethods()) {
                    SaveElementSMarty.addStaticMethod(printWriter, architecture, halfTab, tab, method);
                }
                printWriter.write("\n" + tab + "</class>");
            }
            saveClassInSubpackage(nestedPackage, printWriter, architecture);
        }
    }

    private void addClass(PrintWriter printWriter, String halfTab, String tab, Package pkg, Class clazz) {
        printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\""
                + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\""
                + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\" abstract=\"" + clazz.isAbstract()
                + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth()
                + "\" parent=\"" + pkg.getId() + "\">");
        printWriter.write("\n" + tab + halfTab + "<description>" + clazz.getStringComments() + "</description>");
    }
}
