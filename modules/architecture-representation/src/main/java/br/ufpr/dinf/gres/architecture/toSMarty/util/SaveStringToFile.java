package br.ufpr.dinf.gres.architecture.toSMarty.util;

import br.ufpr.dinf.gres.architecture.flyweights.VariationPointFlyweight;
import br.ufpr.dinf.gres.architecture.io.ReaderConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveStringToFile {

    public SaveStringToFile() {
    }

    private static final SaveStringToFile INSTANCE = new SaveStringToFile();

    public static SaveStringToFile getInstance() {
        return INSTANCE;
    }

    public void appendStrToFile(String fileName,
                                String str) {
        try {
            // Open given file in append mode.
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(fileName, true));
            out.write(str);
            out.close();
        } catch (IOException e) {
            System.out.println("exception occoured" + e);
        }
    }

    public void createLogDir(){
        String directory = ReaderConfig.getDirExportTarget() + "/Logs";
        File file = new File(directory);
        file.mkdir();
    }

    public void createTempDir() {
        String directory = ReaderConfig.getDirExportTarget() + "/TEMP";
        File file = new File(directory);
        file.mkdir();
    }

    public void deleteTempFolder() {
        String directory = ReaderConfig.getDirExportTarget() + "/TEMP";
        File folder = new File(directory);
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                f.delete();
            }
        }
        folder.delete();
    }

}
