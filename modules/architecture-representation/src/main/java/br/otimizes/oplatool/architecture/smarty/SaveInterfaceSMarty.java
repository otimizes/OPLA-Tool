package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Method;
import br.otimizes.oplatool.architecture.representation.Package;

import java.io.PrintWriter;

/**
 * Save all interfaces from architecture to file
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
     * @param printWriter  - used to save the type to file
     */
    public void Save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Interface inter : architecture.getInterfaces()) {
            printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName() + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY() + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight() + "\" width=\"" + inter.getWidth() + "\" parent=\"\">");
            printWriter.write("\n" + tab + halfTab + "<description>" + inter.getStringComments() + "</description>");
            for (Method m : inter.getMethods()) {
                SaveElementSMarty.addAbstractMethod(architecture, printWriter, halfTab, tab, m);
            }
            printWriter.write("\n" + tab + "</interface>");
        }
        for (br.otimizes.oplatool.architecture.representation.Package pkg : architecture.getAllPackages()) {
            for (Interface inter : pkg.getAllInterfaces()) {
                addInterface(printWriter, halfTab, tab, pkg, inter);
                for (Method m : inter.getMethods()) {
                    SaveElementSMarty.addAbstractMethod(architecture, printWriter, halfTab, tab, m);
                }
                printWriter.write("\n" + tab + "</interface>");
            }
            saveInterfaceInSubPackage(pkg, printWriter, architecture);
        }
    }

    /**
     * Recursively save all interfaces that is in the subpackage
     *
     * @param pkg1         - the package that has a subpackage
     * @param printWriter  - used to save the type to file
     * @param architecture - the architecture to be decoded
     */
    private void saveInterfaceInSubPackage(br.otimizes.oplatool.architecture.representation.Package pkg1, PrintWriter printWriter, Architecture architecture) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : pkg1.getNestedPackages()) {
            for (Interface inter : pkg.getAllInterfaces()) {
                addInterface(printWriter, halfTab, tab, pkg, inter);
                for (Method m : inter.getMethods()) {
                    SaveElementSMarty.addStaticMethod(printWriter, architecture, halfTab, tab, m);
                }
                printWriter.write("\n" + tab + "</interface>");
            }
            saveInterfaceInSubPackage(pkg, printWriter, architecture);
        }
    }

    private void addInterface(PrintWriter printWriter, String halfTab, String tab, Package pkg, Interface inter) {
        printWriter.write("\n" + tab + "<interface id=\"" + inter.getId() + "\" name=\"" + inter.getName()
                + "\" mandatory=\"" + inter.isMandatory() + "\" x=\"" + inter.getPosX() + "\" y=\"" + inter.getPosY()
                + "\" globalX=\"" + inter.getGlobalPosX() + "\" globalY=\"" + inter.getGlobalPosY() + "\" height=\"" + inter.getHeight()
                + "\" width=\"" + inter.getWidth() + "\" parent=\"" + pkg.getId() + "\">");
        printWriter.write("\n" + tab + halfTab + "<description>" + inter.getStringComments() + "</description>");
    }
}
