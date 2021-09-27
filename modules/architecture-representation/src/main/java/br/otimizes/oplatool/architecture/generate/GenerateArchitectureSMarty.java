package br.otimizes.oplatool.architecture.generate;


import br.otimizes.oplatool.architecture.papyrus.touml.ArchitectureBase;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.architecture.smarty.*;
import br.otimizes.oplatool.architecture.smarty.relationship.SaveRelationshipSMarty;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * This class generate an .smty file from an architecture
 */
public class GenerateArchitectureSMarty extends ArchitectureBase {

    /**
     * create an .smty output file from an architecture
     *
     * @param architecture - reference the architecture to be decoded
     * @param name         - name of pla to be created (the path is get using ReaderConfig)
     */
    public void generate(Architecture architecture, String name) {
        if (name.contains(FileConstants.FILE_SEPARATOR)) {
            String directory = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + name.split(FileConstants.getEscapedFileSeparator())[0];
            File file = new File(directory);
            file.mkdir();
        }
        String path = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + name + ".smty";
        SaveStringToFile.getInstance().createLogDir();
        String logPath = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR +
                "logs" + FileConstants.FILE_SEPARATOR + "log_" + name + ".txt";
        try {
            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String halfTab = "  ";
            printWriter.print("<project id=\"" + architecture.getProjectID() + "\" name=\"" + architecture.getProjectName() + "\" version=\"" + architecture.getProjectVersion() + "\">");
            VerifyMandatoryOptionalVariability.getInstance().verify(architecture);
            SaveTypeSMarty.getInstance().save(architecture, printWriter);
            SaveConcernListSMarty.getInstance().save(architecture, printWriter);
            printWriter.write("\n" + halfTab + "<profile mandatory=\"STEREOTYPE#1\" optional=\"STEREOTYPE#2\" variationPoint=\"STEREOTYPE#3\" inclusive=\"STEREOTYPE#4\" exclusive=\"STEREOTYPE#5\" requires=\"STEREOTYPE#6\" mutex=\"STEREOTYPE#7\"/>");
            printWriter.write("\n" + halfTab + "<diagram id=\"" + architecture.getDiagramID() + "\" name=\"" + architecture.getDiagramName() + "\" type=\"Class\">");
            ResizeAndReorderArchitectureSMarty.getInstance().execute(architecture);
            ArrayList<Interface> duplicatedInterfaces = architecture.getDuplicatedInterface();
            if (duplicatedInterfaces.size() > 0) {
                for (Interface inter : duplicatedInterfaces) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nInterface Duplicated: " + inter.getId() + " - " + inter.getName());
                }
            }
            SavePackagesSMarty.getInstance().save(architecture, printWriter);
            SaveClassSMarty.getInstance().save(architecture, printWriter);
            SaveInterfaceSMarty.getInstance().save(architecture, printWriter);
            SaveRelationshipSMarty.getInstance().save(architecture, printWriter, logPath);
            for (Package pkg : architecture.getAllPackages())
                saveReference(pkg, printWriter);
            SaveVariabilitySMarty.getInstance().save(architecture, printWriter, logPath);
            printWriter.write("\n" + halfTab + "</diagram>");
            SaveConcernLinkSMart.getInstance().save(architecture, printWriter);
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
     * @param pkg         - package that is parent to subpackage
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
