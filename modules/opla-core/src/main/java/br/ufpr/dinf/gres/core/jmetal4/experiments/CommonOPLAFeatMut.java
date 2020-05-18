package br.ufpr.dinf.gres.core.jmetal4.experiments;

import br.ufpr.dinf.gres.domain.config.ApplicationFileConfigThreadScope;
import br.ufpr.dinf.gres.domain.config.FileConstants;

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
