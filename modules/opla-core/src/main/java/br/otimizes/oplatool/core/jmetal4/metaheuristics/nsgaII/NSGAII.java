//  NSGAII.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaII;

import br.otimizes.oplatool.architecture.io.OPLALogs;
import br.otimizes.oplatool.architecture.io.OptimizationInfo;
import br.otimizes.oplatool.architecture.io.OptimizationInfoStatus;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveFunction;
import br.otimizes.oplatool.core.jmetal4.operators.CrossoverOperators;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.CrossoverUtils;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.PLACrossoverOperator;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.core.jmetal4.util.Distance;
import br.otimizes.oplatool.core.jmetal4.util.Ranking;
import br.otimizes.oplatool.core.jmetal4.util.comparators.CrowdingComparator;
import br.otimizes.oplatool.core.learning.ClassifierAlgorithm;
import br.otimizes.oplatool.core.learning.SubjectiveAnalyzeAlgorithm;
import br.otimizes.oplatool.core.learning.mlmodels.MachineLearningModel;
import br.otimizes.oplatool.domain.OPLAThreadScope;
import br.otimizes.oplatool.domain.config.ApplicationFileConfigThreadScope;
import br.otimizes.oplatool.domain.config.FileConstants;
import com.rits.cloning.Cloner;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the NSGA-II algorithm.
 */
public class NSGAII extends Algorithm {

    private static final long serialVersionUID = 5815971727148859507L;
    private static final Logger LOGGER = Logger.getLogger(NSGAII.class);
    SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = null;

    MachineLearningModel machineLearningModel;

    public NSGAII(Problem problem) {
        super(problem);
    }

    /**
     * Runs the NSGA-II algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException default exception
     */
    public SolutionSet execute() throws JMException {
        LOGGER.info("Initializing execution");
        SolutionSet offspringPopulation;
        SolutionSet union;
        Distance distance = new Distance();
        int populationSize = (Integer) getInputParameter("populationSize");
        int maxEvaluations = (Integer) getInputParameter("maxEvaluations");
        int maxInteractions = (Integer) getInputParameter("maxInteractions");
        int firstInteraction = (Integer) getInputParameter("firstInteraction");
        int intervalInteraction = (Integer) getInputParameter("intervalInteraction");
        Boolean interactive = (Boolean) getInputParameter("interactive");
        this.machineLearningModel = (MachineLearningModel) getInputParameter("machineLearningModel");
        InteractiveFunction interactiveFunction = ((InteractiveFunction) getInputParameter("interactiveFunction"));
        int currentInteraction = 0;
        QualityIndicator indicators = (QualityIndicator) getInputParameter("indicators");
        HashSet<Solution> bestOfUserEvaluation = new HashSet<>();
        SolutionSet population = new SolutionSet(populationSize);
        int evaluations = 0;
        int requiredEvaluations = 0;

        Operator mutationOperator = operators_.get("mutation");
        Operator crossoverOperator = operators_.get("crossover");
        Operator selectionOperator = operators_.get("selection");

        ArrayList<Integer> originalArchElementCount;
        ArrayList<Integer> newArchElementCount;
        try {
            Solution solution_base = new Solution(problem_);
            problem_.evaluateConstraints(solution_base);
            problem_.evaluate(solution_base);
            saveBaseHypervolume(solution_base);
            originalArchElementCount = CrossoverUtils.getElementAmount(solution_base);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JMException(e.getMessage());
        }
        try {
            Solution newSolution;
            for (int i = 0; i < populationSize; i++) {
                newSolution = newRandomSolution(mutationOperator);
                evaluations++;
                population.add(newSolution);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JMException(e.getMessage());
        }

        try {
            while (evaluations < maxEvaluations) {
                offspringPopulation = new SolutionSet(populationSize);
                Solution[] parents = new Solution[2];
                for (int i = 0; i < (populationSize / 2); i++) {
                    if (evaluations < maxEvaluations) {
                        if (((PLACrossoverOperator) crossoverOperator).getOperators().contains(CrossoverOperators.PLA_COMPLEMENTARY_CROSSOVER.name())) {
                            parents = CrossoverUtils.selectionComplementary(population);
                        } else {
                            parents[0] = (Solution) selectionOperator.execute(population);
                            parents[1] = (Solution) selectionOperator.execute(population);
                        }
                        Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                        for (Solution child : offSpring) {
                            problem_.evaluateConstraints(child);
                            mutationOperator.execute(child);
                            problem_.evaluateConstraints(child);
                            problem_.evaluate(child);
                            newArchElementCount = CrossoverUtils.getElementAmount(child);
                            if (IsValidArchElements(originalArchElementCount, newArchElementCount)) {
                                offspringPopulation.add(child);
                            }
                            evaluations += 1;
                        }
                    }
                }

                union = population.union(offspringPopulation);
                Ranking ranking = new Ranking(union);

                int remain = populationSize;
                int index = 0;
                SolutionSet front;
                population.clear();
                front = ranking.getSubfront(index);

                while ((remain > 0) && (remain >= front.size())) {
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    for (int k = 0; k < front.size(); k++) {
                        population.add(front.get(k));
                    }
                    remain = remain - front.size();
                    index++;
                    if (remain > 0) {
                        front = ranking.getSubfront(index);
                    }
                }
                if (remain > 0) {
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    front.sort(new CrowdingComparator());
                    for (int k = 0; k < remain; k++) {
                        population.add(front.get(k));
                    }
                    remain = 0;
                }
                int generation = evaluations / populationSize;
                OPLAThreadScope.currentGeneration.set(generation);
                OPLALogs.add(new OptimizationInfo(Thread.currentThread().getId(), "Generation " + generation, OptimizationInfoStatus.RUNNING));
                if (interactive) {
                    currentInteraction = interactWithDM(generation, offspringPopulation, maxInteractions,
                            firstInteraction, intervalInteraction, interactiveFunction, currentInteraction,
                            bestOfUserEvaluation);
                    for (int i = 0; i < population.getSolutionSet().size(); i++) {
                        if (population.get(i).getEvaluation() == 1) {
                            population.getSolutionSet().set(i, newRandomSolution(mutationOperator));
                        }
                    }
                }

                if ((indicators != null) && (requiredEvaluations == 0)) {
                    double HV = indicators.getHypervolume(population);
                    if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
                        requiredEvaluations = evaluations;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new JMException(e.getMessage());
        }

        setOutputParameter("evaluations", requiredEvaluations);
        Ranking ranking = new Ranking(population);
        SolutionSet subfrontToReturn = ranking.getSubfront(0);

        subfrontToReturn.setCapacity(subfrontToReturn.getCapacity() + bestOfUserEvaluation.size());
        for (Solution solution : bestOfUserEvaluation) {
            if (!subfrontToReturn.getSolutionSet().contains(solution)) {
                subfrontToReturn.add(solution);
            }
        }

        if (interactive && subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained()) {
            try {
                subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(new OPLASolutionSet(subfrontToReturn), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subfrontToReturn;
    }

    private synchronized int interactWithDM(int generation, SolutionSet solutionSet, int maxInteractions, int firstInteraction,
                                            int intervalInteraction, InteractiveFunction interactiveFunction,
                                            int currentInteraction, HashSet<Solution> bestOfUserEvaluation) throws Exception {
//        COMMENT TO INHERIT SCORES
        for (Solution solution : solutionSet.getSolutionSet()) {
            solution.setEvaluation(0);
        }
        boolean isOnInteraction = (generation % intervalInteraction == 0 && generation >= firstInteraction) || generation == firstInteraction;
        boolean inTrainingDuring = currentInteraction < maxInteractions && isOnInteraction;
        if (inTrainingDuring) {
            Cloner cloner = new Cloner();
            List<Solution> solutions = cloner.shallowClone(solutionSet.getSolutionSet());
            SolutionSet newS = new SolutionSet(solutions.size());
            newS.setSolutionSet(solutions);
            solutionSet = interactiveFunction.run(newS);
            if (subjectiveAnalyzeAlgorithm == null) {
                subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(new OPLASolutionSet(solutionSet), this.machineLearningModel);
                subjectiveAnalyzeAlgorithm.run(null, false);
            } else {
                subjectiveAnalyzeAlgorithm.run(new OPLASolutionSet(solutionSet), false);
            }
            bestOfUserEvaluation.addAll(solutionSet.getSolutionSet().stream().filter(p -> (p.getEvaluation() >= 5
                    && p.getEvaluatedByUser()) || (p.containsArchitecturalEvaluation() && p.getEvaluatedByUser())).collect(Collectors.toList()));
            currentInteraction++;
        }

        boolean inTrainingAPosteriori = currentInteraction < maxInteractions && Math.abs((currentInteraction
                * intervalInteraction) + (intervalInteraction / 2)) == generation && generation > firstInteraction;
        if (inTrainingAPosteriori) {
            subjectiveAnalyzeAlgorithm.run(new OPLASolutionSet(solutionSet), true);
        }

        if (subjectiveAnalyzeAlgorithm != null) {
            subjectiveAnalyzeAlgorithm.setTrained(!subjectiveAnalyzeAlgorithm.isTrained()
                    && currentInteraction >= maxInteractions);
            boolean isTrainFinished = subjectiveAnalyzeAlgorithm.isTrained() &&
                    currentInteraction >= maxInteractions && isOnInteraction;
            if (isTrainFinished) {
                subjectiveAnalyzeAlgorithm.evaluateSolutionSetScoreAndArchitecturalAlgorithm(new OPLASolutionSet(solutionSet), true);
            }
        }
        return currentInteraction;
    }

    private Solution newRandomSolution(Operator mutationOperator) throws Exception {
        Solution newSolution;
        newSolution = new Solution(problem_);
        mutationOperator.execute(newSolution);
        problem_.evaluate(newSolution);
        problem_.evaluateConstraints(newSolution);
        return newSolution;
    }

    private void saveBaseHypervolume(Solution solution) {
        SaveStringToFile.getInstance().createLogDir();
        String path = ApplicationFileConfigThreadScope.getDirectoryToExportModels() + FileConstants.FILE_SEPARATOR + "logs" + FileConstants.FILE_SEPARATOR + "fitness_base.txt";
        try {
            FileWriter fileWriter = new FileWriter(path);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (Double fit : solution.getObjectives()) {
                printWriter.write(fit.toString());
                printWriter.write(" ");
            }
            printWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean IsValidArchElements(ArrayList<Integer> lst1, ArrayList<Integer> lst2) {
        if (!("" + lst1.get(0)).equals("" + lst2.get(0))) {
            return false;
        }
        if (!("" + lst1.get(1)).equals("" + lst2.get(1))) {
            return false;
        }
        return ("" + lst1.get(2)).equals("" + lst2.get(2));
    }

    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }
}