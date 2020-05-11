package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.io.PrintWriter;

/**
 * Save all interfaces from architecture to file
 *
 */
public class SaveInterfaceSMarty {

    public SaveInterfaceSMarty() {
    }

    private static final SaveInterfaceSMarty INSTANCE = new SaveInterfaceSMarty();

    public static SaveInterfaceSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * Save all interfaces from architecture to file
     *
     * @param architecture - the architecture to be decoded
     * @param printWriter - used to save the type to file
     */
    public void Save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Interface inter : architecture.getInterfaces()) {
            printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName() + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY() + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight() + "\" width=\"" + inter.getWidth() + "\" parent=\"\">");
            for (Method m : inter.getMethods()) {
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
            printWriter.write("\n" + tab + "</interface>");
        }
        for (Package pkg : architecture.getAllPackages()) {
            for (Interface inter : pkg.getAllInterfaces()) {
                printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName() + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY() + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight() + "\" width=\"" + inter.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
                for (Method m : inter.getMethods()) {
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
                printWriter.write("\n" + tab + "</interface>");
            }
            saveInterfaceInSubPackage(pkg, printWriter, architecture);
        }
    }

    /**
     * Recursively save all interfaces that is in the subpackage
     *
     * @param pkg1 - the package that has a subpackage
     * @param printWriter - used to save the type to file
     * @param architecture - the architecture to be decoded
     */
    private void saveInterfaceInSubPackage(Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            for (Interface inter : pkg.getAllInterfaces()) {
                printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName() + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY() + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight() + "\" width=\"" + inter.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
                for (Method m : inter.getMethods()) {
                    TypeSmarty typeS = architecture.findTypeSMartyByName(m.getReturnType());
                    printWriter.write("\n" + tab + halfTab + "<method id=\"" + m.getId() + "\" name=\"" + m.getName() + "\" return=\"" + typeS.getId() + "\" visibility=\"" + m.getVisibility() + "\" constructor=\"" + m.isConstructor() + "\" static=\"" + m.isStatic() + "\" final=\"" + m.isFinal() + "\" abstract=\"" + m.isAbstract() + "\">");
                    for (ParameterMethod pm : m.getParameters()) {
                        typeS = architecture.findTypeSMartyByName(pm.getType());
                        printWriter.write("\n" + tab + tab + "<parameter type=\"" + typeS.getId() + "\" name=\"" + pm.getName() + "\"/>");
                    }
                    printWriter.write("\n" + tab + halfTab + "</method>");
                }
                printWriter.write("\n" + tab + "</interface>");
            }
            saveInterfaceInSubPackage(pkg, printWriter, architecture);
        }
    }
}
