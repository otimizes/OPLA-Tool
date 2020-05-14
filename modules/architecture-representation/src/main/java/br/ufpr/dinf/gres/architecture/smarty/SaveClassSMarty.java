package br.ufpr.dinf.gres.architecture.smarty;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;

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
            printWriter.write("\n" + tab + halfTab + "<description>"+clazz.getComments()+"</description>");

            for (Attribute att : clazz.getAllAttributes()) {
                TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                if (att.getName().length() == 0) {
                    att.setName(typeS.getName());
                }
                printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
            }
            for (Method m : clazz.getAllMethods()) {
                TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());
                printWriter.write("\n" + tab + halfTab + "<method id=\"" + m.getId() + "\" name=\"" + m.getName() + "\" return=\"" + typeS.getId() + "\" visibility=\"" + m.getVisibility() + "\" constructor=\"" + m.isConstructor() + "\" static=\"" + m.isStatic() + "\" final=\"" + m.isFinal() + "\" abstract=\"" + m.isAbstract() + "\">");
                for (ParameterMethod pm : m.getParameters()) {
                    if (pm.getName().length() == 0)
                        continue;
                    typeS = architecture.findTypeSMartyByName(pm.getType());
                    printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + pm.getName() + "\"/>");
                }
                printWriter.write("\n" + tab + halfTab + "</method>");
            }
            printWriter.write("\n" + tab + "</class>");
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\"" + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\"" + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\" abstract=\"" + clazz.isAbstract() + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
                printWriter.write("\n" + tab + halfTab + "<description>"+clazz.getComments()+"</description>");
                for (Attribute att : clazz.getAllAttributes()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                    if (att.getName().length() == 0) {
                        att.setName(typeS.getName());
                    }
                    printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
                }
                for (Method m : clazz.getAllMethods()) {
                    TypeSmarty typeS = architecture.findReturnTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n" + tab + halfTab + "<method id=\"" + m.getId() + "\" name=\"" + m.getName() + "\" return=\"" + typeS.getId() + "\" visibility=\"" + m.getVisibility() + "\" constructor=\"" + m.isConstructor() + "\" static=\"" + m.isStatic() + "\" final=\"" + m.isFinal() + "\" abstract=\"" + m.isAbstract() + "\">");
                    for (ParameterMethod pm : m.getParameters()) {
                        if (pm.getName().length() == 0)
                            continue;
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + pm.getName() + "\"/>");
                    }
                    printWriter.write("\n" + tab + halfTab + "</method>");
                }
                printWriter.write("\n" + tab + "</class>");
            }
            saveClassInSubpackage(pkg, printWriter, architecture);
        }
    }

    /**
     * Recursively save all class that is in the subpackage
     *
     * @param pkg1 - the package that has a subpackage
     * @param printWriter - used to save the type to file
     * @param architecture - the architecture to be decoded
     */
    private void saveClassInSubpackage(Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            for (Class clazz : pkg.getAllClasses()) {
                printWriter.write("\n" + tab + "<class id=\"" + clazz.getId() + "\" name=\"" + clazz.getName() + "\" mandatory=\"" + clazz.isMandatory() + "\" x=\"" + clazz.getPosX() + "\" y=\"" + clazz.getPosY() + "\" globalX=\"" + clazz.getGlobalPosX() + "\" globalY=\"" + clazz.getGlobalPosY() + "\" abstract=\"" + clazz.isAbstract() + "\" final=\"" + clazz.isAbstract() + "\" height=\"" + clazz.getHeight() + "\" width=\"" + clazz.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
                printWriter.write("\n" + tab + halfTab + "<description>"+clazz.getComments()+"</description>");
                for (Attribute att : clazz.getAllAttributes()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(att.getType());
                    printWriter.write("\n" + tab + halfTab + "<attribute id=\"" + att.getId() + "\" name=\"" + att.getName() + "\" type=\"" + typeS.getId() + "\" visibility=\"" + att.getVisibility() + "\" static=\"" + att.isStatic() + "\" final=\"" + att.isFinal() + "\"/>");
                }
                for (Method m : clazz.getAllMethods()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n" + tab + halfTab + "<method id=\"" + m.getId() + "\" name=\"" + m.getName() + "\" return=\"" + typeS.getId() + "\" visibility=\"" + m.getVisibility() + "\" constructor=\"" + m.isConstructor() + "\" static=\"" + m.isStatic() + "\" final=\"" + m.isFinal() + "\" abstract=\"" + m.isAbstract() + "\">");
                    for (ParameterMethod pm : m.getParameters()) {
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + pm.getName() + "\"/>");
                    }
                    printWriter.write("\n" + tab + halfTab + "</method>");
                }
                printWriter.write("\n" + tab + "</class>");
            }
            saveClassInSubpackage(pkg, printWriter, architecture);
        }
    }

}
