package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;

import java.io.PrintWriter;

/**
 * Save all classes from architecture to file
 *
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
     * @param printWriter - used to save the type to file
     */
    public void Save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Class clazz : architecture.getClasses()) {
            printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\"" + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\"" + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\"  abstract=\"" + clazz.isAbstract() + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth() + "\" parent=\"\">");
            printWriter.write("\n" + tab + halfTab + "<description>"+clazz.getStringComments()+"</description>");

            addAttributes(architecture, printWriter, halfTab, tab, clazz);
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
        for (Attribute att : clazz.getAllAttributes()) {
            TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
            if (att.getName().length() == 0) {
                att.setName(typeS.getName());
            }
            printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
        }
        for (Method m : clazz.getAllMethods()) {
            SaveElementSMarty.addAbstractMethod(architecture, printWriter, halfTab, tab, m);
        }
        printWriter.write("\n" + tab + "</class>");
    }

    /**
     * Recursively save all class that is in the subpackage
     *
     * @param pkg1 - the package that has a subpackage
     * @param printWriter - used to save the type to file
     * @param architecture - the architecture to be decoded
     */
    private void saveClassInSubpackage(br.otimizes.oplatool.architecture.representation.Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                addClass(printWriter, halfTab, tab, pkg, clazz);
                for (Attribute att : clazz.getAllAttributes()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                    printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
                }
                for (Method m : clazz.getAllMethods()) {
                    SaveElementSMarty.addStaticMethod(printWriter, architecture, halfTab, tab, m);
                }
                printWriter.write("\n" + tab + "</class>");
            }
            saveClassInSubpackage(pkg, printWriter, architecture);
        }
    }

    private void addClass(PrintWriter printWriter, String halfTab, String tab, Package pkg, Class clazz) {
        printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\"" + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\"" + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\" abstract=\"" + clazz.isAbstract() + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
        printWriter.write("\n" + tab + halfTab + "<description>"+clazz.getStringComments()+"</description>");
    }
}
