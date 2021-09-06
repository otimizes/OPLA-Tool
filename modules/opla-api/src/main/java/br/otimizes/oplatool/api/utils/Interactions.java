package br.otimizes.oplatool.api.utils;

import br.otimizes.oplatool.architecture.io.OPLALogs;
import br.otimizes.oplatool.architecture.io.OptimizationInfo;
import br.otimizes.oplatool.architecture.io.OptimizationInfoStatus;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Interactions {
    public static Map<String, Interaction> interactions = new HashMap<>();

    public static synchronized void update(String id, SolutionSet solutionSet) {
//        Interaction interaction = Interactions.interactions.get(id);
//        interaction.updated = true;
//        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
//            Solution original = interaction.solutionSet.get(i);
//            Solution update = solutionSet.get(i);
//            original.setEvaluation(update.getEvaluation());
//        }
        Interactions.interactions.put(id, new Interaction(true, solutionSet));
    }

    public static void update(String token, String hash, SolutionSet solutionSet) {
        List<OptimizationInfo> optimizationInfos = OPLALogs.get(token, hash);
        optimizationInfos.get(0).status = OptimizationInfoStatus.RUNNING;
        optimizationInfos.get(0).logs = "Current interaction finished.";
        update(token + FileConstants.FILE_SEPARATOR + hash, solutionSet);
    }

    public static Interaction get(String id) {
        return Interactions.interactions.get(id);
    }

    public static Interaction get(String token, String hash) {
        return Interactions.get(token + FileConstants.FILE_SEPARATOR + hash);
    }

    public static Interaction set(String id, Interaction interaction) {
        return Interactions.interactions.put(id, interaction);
    }


    public static void remove(String id) {
        Interactions.interactions.remove(id);
//        Interactions.interactions = new Interaction();
    }

    public static Boolean isEmpty() {
        return Interactions.interactions != null;
    }
}
