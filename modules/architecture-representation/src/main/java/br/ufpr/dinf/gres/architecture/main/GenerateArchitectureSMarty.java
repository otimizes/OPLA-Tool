package br.ufpr.dinf.gres.architecture.main;


import br.ufpr.dinf.gres.architecture.io.ReaderConfig;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.architecture.representation.*;
import br.ufpr.dinf.gres.architecture.representation.relationship.*;
import br.ufpr.dinf.gres.architecture.toSMarty.*;
import br.ufpr.dinf.gres.architecture.toSMarty.relationship.SaveRelationshipSMarty;
import br.ufpr.dinf.gres.architecture.toSMarty.util.SaveStringToFile;
import br.ufpr.dinf.gres.architecture.touml.ArchitectureBase;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GenerateArchitectureSMarty extends ArchitectureBase {


    public void generate(Architecture architecture, String name) {

        /// criar pasta TEMP caso o nome venha com '/'
        if (name.contains("/")) {
            String directory = ReaderConfig.getDirExportTarget() + "/" + name.split("/")[0];
            File file = new File(directory);
            file.mkdir();
        }

        String path = ReaderConfig.getDirExportTarget() + "/" + name + ".smty";

        SaveStringToFile.getInstance().createLogDir();
        String logPath = ReaderConfig.getDirExportTarget() + "/Logs/log_" + name + ".txt";

        System.out.println(path);
        try {

            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);


            String halfTab = "  ";
            String tab = "    ";

            // start project
            //printWriter.print("<project id=\""+architecture.getProjectID()+"\" name=\""+architecture.getProjectName()+"\" version=\""+architecture.getProjectVersion()+"\">");
            printWriter.print("<project id=\"" + architecture.getProjectID() + "\" name=\"" + architecture.getProjectName() + "\" version=\"1.0\">");

            //<type id="TYPE#29" path="java.util" name="Arrays" value="null" primitive="false" standard="true"/>


            // verify if elements are mandatory or optional using variability
            new VerifyMandatoryOptionalVariability(architecture);

            // save Types
            new SaveTypeSMarty(architecture, printWriter);
            // end types


            // save stereotypes
            new SaveConcernListSMarty(architecture, printWriter);
            // end stereotypes

            printWriter.write("\n" + halfTab + "<profile mandatory=\"STEREOTYPE#1\" optional=\"STEREOTYPE#2\" variationPoint=\"STEREOTYPE#3\" inclusive=\"STEREOTYPE#4\" exclusive=\"STEREOTYPE#5\" requires=\"STEREOTYPE#6\" mutex=\"STEREOTYPE#7\"/>");

            printWriter.write("\n" + halfTab + "<diagram id=\"" + architecture.getDiagramID() + "\" name=\"" + architecture.getDiagramName() + "\" type=\"Class\">");

            // resize and reorder elements
            new ResizeAndReorderArchitectureSMarty(architecture);

            // verify duplicate interfaces (duplicated interfaces cannot be open in SMarty Modeling)
            architecture.hasDuplicateInterface();

            ArrayList<Interface> duplicatedInterfaces = architecture.getDuplicateInterface();
            if (duplicatedInterfaces.size() > 0) {
                for (Interface inter : duplicatedInterfaces) {
                    SaveStringToFile.getInstance().appendStrToFile(logPath, "\nInterface Duplicated: " + inter.getId() + " - " + inter.getName());
                }
            }

            // Salve packages in file
            new SavePackagesSMarty(architecture, printWriter);


            // save class in file
            new SaveClassSMarty(architecture, printWriter);


            //// import interface
            //<interface id="INTERFACE#3" name="IManageAlbum" mandatory="true" x="357" y="129" height="216" width="280" parent="PACKAGE#3">
            new SaveInterfaceSMarty(architecture, printWriter);


            // save relationships
            new SaveRelationshipSMarty(architecture, printWriter, logPath);


            //fim relacionamento


            // save subpackage reference to package
            for (Package pkg : architecture.getAllPackages())
                saveReference(pkg, printWriter);



            ArrayList<String> discartedVariability = new ArrayList<>();

            // save variability
            new SaveVariabilitySMarty(architecture, printWriter, logPath);


            printWriter.write("\n" + halfTab + "</diagram>");


            // save link stereotype
            new SaveConcernLinkSMart(architecture,printWriter);
            // end save link stereotype

            printWriter.write("\n" + halfTab + "<products>");
            printWriter.write("\n" + halfTab + "</products>");
            // end projects
            printWriter.write("\n</project>");

            printWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    private void saveReference(Package pkg, PrintWriter printWriter) {
        String tab = "    ";
        for (Package subPkg : pkg.getNestedPackages()) {
            printWriter.write("\n" + tab + "<reference package=\"" + subPkg.getId() + "\" parent=\"" + pkg.getId() + "\" />");
            saveReference(subPkg, printWriter);
        }
    }




}
