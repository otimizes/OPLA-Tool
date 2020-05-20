package br.ufpr.dinf.gres.core.jmetal4.experiments.base;

import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.domain.config.ApplicationFileConfigThreadScope;
import br.ufpr.dinf.gres.domain.config.FileConstants;

import java.io.File;

public class OPLABaseUtils {

    public static void saveHypervolume(String experimentID, String executionID, SolutionSet allSolutions, String plaName) {
        String dir;
        if (executionID != null)
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + FileConstants.FILE_SEPARATOR + executionID + "/Hypervolume/";
        else
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + "/Hypervolume/";

        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();

        if (executionID != null) {
            for (Solution solution : allSolutions.getSolutionSet()) {
                solution.setExecutionId(executionID);
                solution.setExperimentId(experimentID);
            }
        }
        new OPLASolutionSet(allSolutions).printObjectivesToFile(dir + "/hypervolume.txt");
    }
}
