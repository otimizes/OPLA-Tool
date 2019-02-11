package jmetal4.experiments;

import arquitetura.io.ReaderConfig;

import java.io.File;

public class CommonOPLAFeatMut {

    public static void setDirToSaveOutput(String experimentID, String executionID) {
        String dir;
        if (executionID != null) {
            dir = ReaderConfig.getDirExportTarget() + System.getProperty("file.separator")  + experimentID + System.getProperty("file.separator") + executionID
                    + System.getProperty("file.separator");
        } else {
            dir = ReaderConfig.getDirExportTarget() + System.getProperty("file.separator")  + experimentID + System.getProperty("file.separator");
        }
        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();
    }
}
