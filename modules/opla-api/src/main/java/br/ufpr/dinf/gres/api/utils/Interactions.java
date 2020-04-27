package br.ufpr.dinf.gres.api.utils;

import br.ufpr.dinf.gres.core.jmetal4.core.Solution;
import br.ufpr.dinf.gres.core.jmetal4.core.SolutionSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Interactions {
    public static Map<Long, Interaction> interactions = new HashMap<>();

    public static void update(Long id, SolutionSet solutionSet) {
        Interaction interaction = Interactions.interactions.get(id);
        interaction.updated = true;
        for (int i = 0; i < solutionSet.getSolutionSet().size(); i++) {
            Solution original = interaction.solutionSet.get(i);
            Solution update = solutionSet.get(i);
            original.setEvaluation(update.getEvaluation());
        }
    }

    public static Interaction get(Long id) {
        return Optional.ofNullable(Interactions.interactions.get(id)).orElse(new Interaction());
    }
    public static Interaction set(Long id, Interaction interaction) {
        return Interactions.interactions.put(id, interaction);
    }
}
