package br.ufpr.dinf.gres.core.jmetal4.experiments;

import br.ufpr.dinf.gres.domain.config.ApplicationFileConfigThreadScope;

import java.io.File;

public class CommonOPLAFeatMut {

    public static void setDirToSaveOutput(String experimentID, String executionID) {
        String dir;
        if (executionID != null) {
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + System.getProperty("file.separator") + experimentID + System.getProperty("file.separator") + executionID
                    + System.getProperty("file.separator");
        } else {
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + System.getProperty("file.separator") + experimentID + System.getProperty("file.separator");
        }
        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();
    }
}
