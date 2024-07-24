package br.otimizes.oplatool.core.jmetal4.interactive;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.rits.cloning.Cloner;

import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.isearchai.learning.ClassifierAlgorithm;
import br.otimizes.isearchai.learning.SubjectiveAnalyzeAlgorithm;

/**
 * Class containing DM interactivity procedures
 */
public class InteractWithDM {
    private SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = null;

    public InteractWithDM() {}

    public synchronized int interactWithDM(int generation, SolutionSet solutionSet, int maxInteractions,
            int firstInteraction,
            int intervalInteraction, InteractiveFunction interactiveFunction,
            int currentInteraction, HashSet<Solution> bestOfUserEvaluation) throws Exception {
        for (Solution solution : solutionSet.getSolutionSet()) {
            solution.setEvaluation(0);
        }
        boolean isOnInteraction = (generation % intervalInteraction == 0 && generation >= firstInteraction)
                || generation == firstInteraction;
        boolean inTrainingDuring = currentInteraction < maxInteractions && isOnInteraction;
        if (inTrainingDuring) {
            Cloner cloner = new Cloner();
            List<Solution> solutions = cloner.shallowClone(solutionSet.getSolutionSet());
            SolutionSet newS = new SolutionSet(solutions.size());
            newS.setSolutionSet(solutions);
            solutionSet = interactiveFunction.run(newS);
            if (subjectiveAnalyzeAlgorithm == null) {
                subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(new SolutionSet(solutionSet),
                        ClassifierAlgorithm.CLUSTERING_MLP);
                subjectiveAnalyzeAlgorithm.run(null, false);
            } else {
                subjectiveAnalyzeAlgorithm.run(new SolutionSet(solutionSet), false);
            }
            bestOfUserEvaluation.addAll(solutionSet.getSolutionSet().stream().filter(p -> (p.getEvaluation() >= 5
                    && p.getEvaluatedByUser()) || (p.containsArchitecturalEvaluation() && p.getEvaluatedByUser()))
                    .collect(Collectors.toList()));
            currentInteraction++;
        }

        boolean inTrainingAPosteriori = currentInteraction < maxInteractions && Math.abs((currentInteraction
                * intervalInteraction) + (intervalInteraction / 2)) == generation && generation > firstInteraction;
        if (inTrainingAPosteriori) {
            subjectiveAnalyzeAlgorithm.run(new SolutionSet(solutionSet), true);
        }

        if (subjectiveAnalyzeAlgorithm != null) {
            subjectiveAnalyzeAlgorithm.setTrained(!subjectiveAnalyzeAlgorithm.isTrained()
                    && currentInteraction >= maxInteractions);
            boolean isTrainFinished = subjectiveAnalyzeAlgorithm.isTrained() &&
                    currentInteraction >= maxInteractions && isOnInteraction;
            if (isTrainFinished) {
                subjectiveAnalyzeAlgorithm
                        .evaluateSolutionSetScoreAndArchitecturalAlgorithm(new SolutionSet(solutionSet), true);
            }
        }
        return currentInteraction;
    }

    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }
}
