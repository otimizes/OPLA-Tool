package br.ufpr.dinf.gres.api.utils;

import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;
import br.ufpr.dinf.gres.domain.config.FileConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Interactions {
    public static Map<String, Interaction> interactions = new HashMap<>();

    public static void update(String id, SolutionSet solutionSet) {
        Interaction interaction = Interactions.interactions.get(id);
        interaction.updated = true;
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            Solution original = interaction.solutionSet.get(i);
            Solution update = solutionSet.get(i);
            original.setEvaluation(update.getEvaluation());
        }
    }

    public static void update(String token, String hash, SolutionSet solutionSet) {
        update(token + FileConstants.FILE_SEPARATOR + hash, solutionSet);
    }

    public static Interaction get(String id) {
        return Optional.ofNullable(Interactions.interactions.get(id)).orElse(new Interaction());
    }

    public static Interaction get(String token, String hash) {
        return Interactions.get(token + FileConstants.FILE_SEPARATOR + hash);
    }

    public static Interaction set(String id, Interaction interaction) {
        return Interactions.interactions.put(id, interaction);
    }

    public static Interaction set(String token, String hash, Interaction interaction) {
        return Interactions.set(token + FileConstants.FILE_SEPARATOR + hash, interaction);
    }
}
