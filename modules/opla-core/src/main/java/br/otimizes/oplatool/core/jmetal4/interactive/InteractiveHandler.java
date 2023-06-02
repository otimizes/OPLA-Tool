package br.otimizes.oplatool.core.jmetal4.interactive;

import java.util.HashSet;

import br.otimizes.oplatool.core.jmetal4.core.OPLASolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.learning.SubjectiveAnalyzeAlgorithm;

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

        public void setMaxInteractions(int maxInteractions) {
            this.maxInteractions = maxInteractions;
        }

        public int getFirstInteraction() {
            return firstInteraction;
        }

        public void setFirstInteraction(int firstInteraction) {
            this.firstInteraction = firstInteraction;
        }

        public int getIntervalInteraction() {
            return intervalInteraction;
        }

        public void setIntervalInteraction(int intervalInteraction) {
            this.intervalInteraction = intervalInteraction;
        }

        public InteractiveFunction getInteractiveFunction() {
            return interactiveFunction;
        }

        public void setInteractiveFunction(InteractiveFunction interactiveFunction) {
            this.interactiveFunction = interactiveFunction;
        }
    }

    public static class InteractionData {
        private int currentInteraction = 0;
        private HashSet<Solution> bestOfUserEvaluation = new HashSet<>();

        public int getCurrentInteraction() {
            return currentInteraction;
        }

        public void setCurrentInteraction(int currentInteraction) {
            this.currentInteraction = currentInteraction;
        }

        public HashSet<Solution> getBestOfUserEvaluation() {
            return bestOfUserEvaluation;
        }

        public void setBestOfUserEvaluation(HashSet<Solution> bestOfUserEvaluation) {
            this.bestOfUserEvaluation = bestOfUserEvaluation;
        }
    };

    private InteractiveConfig config;
    private InteractionData data;
    private InteractWithDM interaction;

    public InteractiveHandler(InteractiveConfig config) {
        this.config = config;
        this.data = new InteractionData();
        this.interaction = new InteractWithDM();
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
        SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = interaction.getSubjectiveAnalyzeAlgorithm();
        if (subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained())
            subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(population, false);
    }
}
