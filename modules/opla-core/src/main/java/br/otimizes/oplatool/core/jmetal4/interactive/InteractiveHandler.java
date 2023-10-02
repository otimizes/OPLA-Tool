package br.otimizes.oplatool.core.jmetal4.interactive;

import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.learning.SubjectiveAnalyzeAlgorithm;

import java.util.HashSet;

/*
 * Helper class which holds options related to interactivity.
 * @author Lucas
 */
public class InteractiveHandler {
    public static class InteractiveConfig {
        private int maxInteractions;
        private int firstInteraction;
        private int intervalInteraction;
        private InteractiveFunction interactiveFunction;

        public InteractiveConfig() {
        }

        public int getMaxInteractions() {
            return maxInteractions;
        }

        public InteractiveConfig setMaxInteractions(int maxInteractions) {
            this.maxInteractions = maxInteractions;
            return this;
        }

        public int getFirstInteraction() {
            return firstInteraction;
        }

        public InteractiveConfig setFirstInteraction(int firstInteraction) {
            this.firstInteraction = firstInteraction;
            return this;
        }

        public int getIntervalInteraction() {
            return intervalInteraction;
        }

        public InteractiveConfig setIntervalInteraction(int intervalInteraction) {
            this.intervalInteraction = intervalInteraction;
            return this;
        }

        public InteractiveFunction getInteractiveFunction() {
            return interactiveFunction;
        }

        public InteractiveConfig setInteractiveFunction(InteractiveFunction interactiveFunction) {
            this.interactiveFunction = interactiveFunction;
            return this;
        }
    }

    public static class InteractionData {
        private int currentInteraction = 0;
        private HashSet<Solution> bestOfUserEvaluation = new HashSet<>();

        public int getCurrentInteraction() {
            return currentInteraction;
        }

        public InteractionData setCurrentInteraction(int currentInteraction) {
            this.currentInteraction = currentInteraction;
            return this;
        }

        public HashSet<Solution> getBestOfUserEvaluation() {
            return bestOfUserEvaluation;
        }

        public InteractionData setBestOfUserEvaluation(HashSet<Solution> bestOfUserEvaluation) {
            this.bestOfUserEvaluation = bestOfUserEvaluation;
            return this;
        }
    }

    ;

    private InteractiveConfig config;
    private InteractionData data;
    private InteractWithDM interaction;

    private SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm;

    public InteractiveHandler() {
        this.data = new InteractionData();
        this.interaction = new InteractWithDM();
    }

    public InteractiveHandler setInteractiveConfig(InteractiveConfig config) {
        this.config = config;
        return this;
    }

    public boolean checkAndInteract(int generation, SolutionSet offspringPopulation) throws Exception {
        int currentInteraction = interaction.interactWithDM(
                generation,
                offspringPopulation,
                config.getMaxInteractions(),
                config.getFirstInteraction(),
                config.getIntervalInteraction(),
                config.getInteractiveFunction(),
                data.getCurrentInteraction(),
                data.getBestOfUserEvaluation());
        if (data.getCurrentInteraction() != currentInteraction) {
            data.setCurrentInteraction(currentInteraction);
            return true;
        } else {
            return false;
        }
    }

    public void subjectiveAnalyzeSolutionSet(OPLASolutionSet population) throws Exception {
        subjectiveAnalyzeAlgorithm = interaction.getSubjectiveAnalyzeAlgorithm();
        if (subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained())
            subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(population, false);
    }

    public void resetInteractionData() {
        data = new InteractionData();
    }

    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }
}
