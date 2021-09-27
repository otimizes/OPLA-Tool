package br.otimizes.oplatool.architecture.smarty;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Package;

import java.io.PrintWriter;

/**
 * Save all packages in the file
 * the link of package and subpackage will be saved in other class
 */
public class SavePackagesSMarty {

    public SavePackagesSMarty() {
    }

    private static final SavePackagesSMarty INSTANCE = new SavePackagesSMarty();

    public static SavePackagesSMarty getInstance() {
        return INSTANCE;
    }

    /**
     * Save all packages in the file
     * the link of package and subpackage will be saved in other class
     *
     * @param architecture - input architecture
     * @param printWriter  - used to save a string to file
     */
    public void save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : architecture.getAllPackages()) {
            addPackage(printWriter, halfTab, tab, pkg);
        }
    }

    /**
     * Recursively save all sub packages in the file
     *
     * @param originPackage   - parent of subpackage
     * @param printWriter - used to save a string to file
     */
    private void saveSubPackage(Package originPackage, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package subPackage : originPackage.getNestedPackages()) {
            addPackage(printWriter, halfTab, tab, subPackage);
        }
    }

    private void addPackage(PrintWriter printWriter, String halfTab, String tab, Package subPackage) {
        printWriter.write("\n" + tab + "<package id=\"" + subPackage.getId() + "\" name=\"" + subPackage.getName()
                + "\" mandatory=\"" + subPackage.isMandatory() + "\" x=\"" + subPackage.getPosX() + "\" y=\""
                + subPackage.getPosY() + "\" globalX=\"" + subPackage.getGlobalPosX() + "\" globalY=\"" + subPackage.getGlobalPosY()
                + "\" height=\"" + subPackage.getHeight() + "\" width=\"" + subPackage.getWidth() + "\">");
        printWriter.write("\n" + tab + halfTab + "<description>" + subPackage.getStringComments() + "</description>");
        printWriter.write("\n" + tab + "</package>");
        saveSubPackage(subPackage, printWriter);
    }
}
