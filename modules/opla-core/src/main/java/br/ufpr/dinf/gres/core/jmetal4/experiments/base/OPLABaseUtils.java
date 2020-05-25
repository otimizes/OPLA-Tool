package br.ufpr.dinf.gres.core.jmetal4.experiments.base;

import br.ufpr.dinf.gres.core.jmetal4.core.OPLASolutionSet;
import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.domain.config.ApplicationFileConfigThreadScope;
import br.ufpr.dinf.gres.domain.config.FileConstants;
import br.ufpr.dinf.gres.domain.config.UserHome;
import br.ufpr.dinf.gres.persistence.service.OPLACommand;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class OPLABaseUtils {

    public static void saveHypervolume(String experimentID, String executionID, SolutionSet allSolutions, String plaName) {
        String dir;
        if (executionID != null)
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + FileConstants.FILE_SEPARATOR + executionID + FileConstants.FILE_SEPARATOR + "fitness" + FileConstants.FILE_SEPARATOR;
        else
            dir = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + experimentID + FileConstants.FILE_SEPARATOR + "fitness" + FileConstants.FILE_SEPARATOR;

        File newDir = new File(dir);
        if (!newDir.exists())
            newDir.mkdirs();

        if (executionID != null) {
            for (Solution solution : allSolutions.getSolutionSet()) {
                solution.setExecutionId(executionID);
                solution.setExperimentId(experimentID);
            }
        }
        new OPLASolutionSet(allSolutions).printObjectivesToFile(dir + FileConstants.FILE_SEPARATOR + "fitness.txt");
        if (executionID == null) {
            String referencePoint = Arrays.stream(allSolutions.get(0).getObjectives()).mapToObj(s -> "1.01").collect(Collectors.joining(" "));
            try {
                OPLACommand.execCmd(UserHome.getOplaHv() + " -r \"" + referencePoint + "\" " + dir + "fitness.hypervolume >> " + dir + "fitness.hypervolume.final");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
