package br.otimizes.oplatool.core.jmetal4.experiments;

import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Util {

    private static String fileSeparator = FileConstants.FILE_SEPARATOR;

    public static void moveResourceToExperimentFolder(String experimentId) {
        StringBuilder sourcePath = new StringBuilder();
        String dirExportTarget = ApplicationFileConfigThreadScope.getDirectoryToExportModels();
        sourcePath.append(dirExportTarget);
        sourcePath.append(fileSeparator);
        sourcePath.append("resources");

        File source = new File(sourcePath.toString());
        String destinationPath = dirExportTarget +
                fileSeparator +
                experimentId +
                fileSeparator +
                "resources";
        File destination = new File(destinationPath);
        source.renameTo(destination);
    }

    public static void copyFolder(String experimentId, String executionId) throws IOException {
        StringBuilder sourcePath = new StringBuilder();
        String dirExportTarget = ApplicationFileConfigThreadScope.getDirectoryToExportModels();
        sourcePath.append(dirExportTarget);
        sourcePath.append(fileSeparator);
        sourcePath.append("resources");

        File src = new File(sourcePath.toString());
        String destinationPath = dirExportTarget +
                fileSeparator +
                experimentId +
                fileSeparator +
                executionId +
                fileSeparator +
                "resources";
        File dest = new File(destinationPath);
        FileUtils.copyDirectory(src, dest);
    }

    public static void moveAllFilesToExecutionDirectory(String experimentID, String executionID) {
        String[] exts = {"uml", "di", "notation"};
        @SuppressWarnings("unchecked")
        List<File> listFiles = (List<File>) FileUtils.listFiles(new File(ApplicationFileConfigThreadScope.getDirectoryToExportModels()), exts, false);
        for (File file : listFiles) {
            if (executionID == null) {

                String path = ApplicationFileConfigThreadScope.getDirectoryToExportModels() +
                        experimentID +
                        fileSeparator +
                        file.getName();
                file.renameTo(new File(path));
            } else {
                String path = ApplicationFileConfigThreadScope.getDirectoryToExportModels() +
                        experimentID +
                        fileSeparator +
                        executionID +
                        fileSeparator +
                        file.getName();
                file.renameTo(new File(path));
            }
        }
    }
}
