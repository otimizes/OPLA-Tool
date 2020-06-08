package br.otimizes.oplatool.core.jmetal4.experiments;

import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.io.File;

public class CommonOPLAFeatMut {

    public static void setDirToSaveOutput(String experimentID, String executionID) {
        String dir;
        if (executionID != null) {
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + FileConstants.FILE_SEPARATOR + executionID
                    + FileConstants.FILE_SEPARATOR;
        } else {
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + FileConstants.FILE_SEPARATOR;
        }
        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();
    }
}
