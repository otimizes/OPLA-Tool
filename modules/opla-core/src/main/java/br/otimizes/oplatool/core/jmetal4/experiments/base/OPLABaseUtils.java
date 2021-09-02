package br.otimizes.oplatool.core.jmetal4.experiments.base;

import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import br.otimizes.oplatool.domain.config.UserHome;
import br.otimizes.oplatool.persistence.service.OPLACommand;

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
                OPLACommand.execCmd(UserHome.getOPLAHV() + " -r \"" + referencePoint + "\" " + dir + "fitness.hypervolume >> " + dir + "fitness.hypervolume.final");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
