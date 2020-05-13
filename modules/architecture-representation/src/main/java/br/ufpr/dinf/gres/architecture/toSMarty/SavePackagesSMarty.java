package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.io.PrintWriter;

/**
 * Save all packages in the file
 * the link of package and subpackage will be saved in other class
 *
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
     * @param printWriter - used to save a string to file
     */
    public void Save(Architecture architecture, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package pkg : architecture.getAllPackages()) {
            printWriter.write("\n" + tab + "<package id=\"" + pkg.getId() + "\" name=\"" + pkg.getName() + "\" mandatory=\"" + pkg.isMandatory() + "\" x=\"" + pkg.getPosX() + "\" y=\"" + pkg.getPosY() + "\" globalX=\"" + pkg.getGlobalPosX() + "\" globalY=\"" + pkg.getGlobalPosY() + "\" height=\"" + pkg.getHeight() + "\" width=\"" + pkg.getWidth() + "\">");
            printWriter.write("\n" + tab + halfTab + "<description>"+pkg.getComments()+"</description>");
            printWriter.write("\n" + tab + "</package>");
            saveSubPackage(pkg, printWriter);
        }
    }

    /**
     * Recursively save all sub packages in the file
     *
     * @param pkgOrigin - parent of subpackage
     * @param printWriter - used to save a string to file
     */
    private void saveSubPackage(Package pkgOrigin, PrintWriter printWriter) {
        String halfTab = "  ";
        String tab = "    ";
        for (Package subPackage : pkgOrigin.getNestedPackages()) {
            printWriter.write("\n" + tab + "<package id=\"" + subPackage.getId() + "\" name=\"" + subPackage.getName() + "\" mandatory=\"" + subPackage.isMandatory() + "\" x=\"" + subPackage.getPosX() + "\" y=\"" + subPackage.getPosY() + "\" globalX=\"" + subPackage.getGlobalPosX() + "\" globalY=\"" + subPackage.getGlobalPosY() + "\" height=\"" + subPackage.getHeight() + "\" width=\"" + subPackage.getWidth() + "\">");
            printWriter.write("\n" + tab + halfTab + "<description>"+subPackage.getComments()+"</description>");
            printWriter.write("\n" + tab + "</package>");
            saveSubPackage(subPackage, printWriter);
        }
    }

}
