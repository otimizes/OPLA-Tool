package br.ufpr.dinf.gres.architecture.main;


import br.ufpr.dinf.gres.architecture.io.OPLAConfigThreadScopeReader;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.toSMarty.*;
import br.ufpr.dinf.gres.architecture.toSMarty.relationship.SaveRelationshipSMarty;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;
import br.ufpr.dinf.gres.architecture.touml.ArchitectureBase;

import java.io.*;
import java.util.ArrayList;

/**
 * This class generate an .smty file from an architecture
 */
public class GenerateArchitectureSMarty extends ArchitectureBase {

    /**
     * create an .smty output file from an architecture
     * @param architecture - reference the architecture to be decoded
     * @param name - name of pla to be created (the path is get using ReaderConfig)
     */
    public void generate(Architecture architecture, String name) {
        if (name.contains("/")) {
            String directory = OPLAConfigThreadScopeReader.getDirectoryToExportModels() + "/" + name.split("/")[0];
            File file = new File(directory);
            file.mkdir();
        }
        String path = OPLAConfigThreadScopeReader.getDirectoryToExportModels() + "/" + name + ".smty";
        SaveStringToFile.getInstance().createLogDir();
        String logPath = OPLAConfigThreadScopeReader.getDirectoryToExportModels() + "/Logs/log_" + name + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String halfTab = "  ";
            printWriter.print("<project id=\"" + architecture.getProjectID() + "\" name=\"" + architecture.getProjectName() + "\" version=\""+architecture.getProjectVersion()+"\">");
            VerifyMandatoryOptionalVariability.getInstance().Verify(architecture);
            SaveTypeSMarty.getInstance().Save(architecture, printWriter);
            SaveConcernListSMarty.getInstance().Save(architecture, printWriter);
            printWriter.write("\n" + halfTab + "<profile mandatory=\"STEREOTYPE#1\" optional=\"STEREOTYPE#2\" variationPoint=\"STEREOTYPE#3\" inclusive=\"STEREOTYPE#4\" exclusive=\"STEREOTYPE#5\" requires=\"STEREOTYPE#6\" mutex=\"STEREOTYPE#7\"/>");
            printWriter.write("\n" + halfTab + "<diagram id=\"" + architecture.getDiagramID() + "\" name=\"" + architecture.getDiagramName() + "\" type=\"Class\">");
            ResizeAndReorderArchitectureSMarty.getInstance().Execute(architecture);
            ArrayList<Interface> duplicatedInterfaces = architecture.getDuplicateInterface();
            if (duplicatedInterfaces.size() > 0) {
                for (Interface inter : duplicatedInterfaces) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nInterface Duplicated: " + inter.getId() + " - " + inter.getName());
                }
            }
            SavePackagesSMarty.getInstance().Save(architecture, printWriter);
            SaveClassSMarty.getInstance().Save(architecture, printWriter);
            SaveInterfaceSMarty.getInstance().Save(architecture, printWriter);
            SaveRelationshipSMarty.getInstance().Save(architecture, printWriter, logPath);
            for (Package pkg : architecture.getAllPackages())
                saveReference(pkg, printWriter);
            SaveVariabilitySMarty.getInstance().Save(architecture, printWriter, logPath);
            printWriter.write("\n" + halfTab + "</diagram>");
            SaveConcernLinkSMart.getInstance().Save(architecture,printWriter);
            printWriter.write("\n" + halfTab + "<products>");
            printWriter.write("\n" + halfTab + "</products>");
            printWriter.write("\n</project>");
            printWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    /**
     * Save the list of references of subpackage with parent package
     *
     * @param pkg - package that is parent to subpackage
     * @param printWriter - used to save a string to file
     */
    private void saveReference(Package pkg, PrintWriter printWriter) {
        String tab = "    ";
        for (Package subPkg : pkg.getNestedPackages()) {
            printWriter.write("\n" + tab + "<reference package=\"" + subPkg.getId() + "\" parent=\"" + pkg.getId() + "\" />");
            saveReference(subPkg, printWriter);
        }
    }




}
