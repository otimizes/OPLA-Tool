package br.ufpr.dinf.gres.architecture.toSMarty;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.io.PrintWriter;

public class SavePackagesSMarty {

    public SavePackagesSMarty(Architecture architecture, PrintWriter printWriter) {
        String tab = "    ";
        for (Package pkg : architecture.getAllPackages()) {
            // <package id="PACKAGE#2" name="AlbumMgr" mandatory="true" x="300" y="2090" height="700" width="667"/>
            printWriter.write("\n" + tab + "<package id=\"" + pkg.getId() + "\" name=\"" + pkg.getName() + "\" mandatory=\"" + pkg.isMandatory() + "\" x=\"" + pkg.getPosX() + "\" y=\"" + pkg.getPosY() + "\" globalX=\"" + pkg.getGlobalPosX() + "\" globalY=\"" + pkg.getGlobalPosY() + "\" height=\"" + pkg.getHeight() + "\" width=\"" + pkg.getWidth() + "\"/>");

            saveSubPackage(pkg, printWriter);
        }
    }

    private void saveSubPackage(Package pkgOrigin, PrintWriter printWriter) {
        String tab = "    ";
        for (Package subPackage : pkgOrigin.getNestedPackages()) {
            // <package id="PACKAGE#2" name="AlbumMgr" mandatory="true" x="300" y="2090" height="700" width="667"/>

            printWriter.write("\n" + tab + "<package id=\"" + subPackage.getId() + "\" name=\"" + subPackage.getName() + "\" mandatory=\"" + subPackage.isMandatory() + "\" x=\"" + subPackage.getPosX() + "\" y=\"" + subPackage.getPosY() + "\" globalX=\"" + subPackage.getGlobalPosX() + "\" globalY=\"" + subPackage.getGlobalPosY() + "\" height=\"" + subPackage.getHeight() + "\" width=\"" + subPackage.getWidth() + "\"/>");
            saveSubPackage(subPackage, printWriter);
        }
    }

}
