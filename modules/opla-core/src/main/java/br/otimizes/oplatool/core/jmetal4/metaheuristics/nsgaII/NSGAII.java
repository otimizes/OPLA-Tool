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
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.smarty.util.SaveStringToFile;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveFunction;
import br.otimizes.oplatool.core.jmetal4.operators.CrossoverOperators;
import br.otimizes.oplatool.core.jmetal4.operators.crossover.PLACrossoverOperator;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.core.jmetal4.util.Distance;
import br.otimizes.oplatool.core.jmetal4.util.Ranking;
import br.otimizes.oplatool.core.jmetal4.util.comparators.CrowdingComparator;
import br.otimizes.oplatool.core.learning.ClassifierAlgorithm;
import br.otimizes.oplatool.core.learning.SubjectiveAnalyzeAlgorithm;
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
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class implements the NSGA-II algorithm.
 */
public class NSGAII extends Algorithm {

    private static final long serialVersionUID = 5815971727148859507L;
    private static final Logger LOGGER = Logger.getLogger(NSGAII.class);
    SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm = null;

    /**
     * Constructor
     *
     * @param problem Problem to solve
     */
    public NSGAII(Problem problem) {
        super(problem);

    }

    /**
     * Runs the NSGA-II algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException default exception
     * @throws Exception   default exception
     */
    public SolutionSet execute() throws JMException {
        LOGGER.info("Iniciando Execução");
        int populationSize;
        int maxEvaluations;
        int evaluations;

        QualityIndicator indicators;
        int requiredEvaluations;

        SolutionSet population;
        SolutionSet offspringPopulation;
        SolutionSet union;

        Operator mutationOperator;
        Operator crossoverOperator;
        Operator selectionOperator;

        Distance distance = new Distance();

        populationSize = (Integer) getInputParameter("populationSize");
        maxEvaluations = (Integer) getInputParameter("maxEvaluations");
        int maxInteractions = (Integer) getInputParameter("maxInteractions");
        int firstInteraction = (Integer) getInputParameter("firstInteraction");
        int intervalInteraction = (Integer) getInputParameter("intervalInteraction");
        Boolean interactive = (Boolean) getInputParameter("interactive");
        InteractiveFunction interactiveFunction = ((InteractiveFunction) getInputParameter("interactiveFunction"));

        int currentInteraction = 0;
        indicators = (QualityIndicator) getInputParameter("indicators");
        HashSet<Solution> bestOfUserEvaluation = new HashSet<>();

        population = new SolutionSet(populationSize);
        evaluations = 0;

        requiredEvaluations = 0;

        mutationOperator = operators_.get("mutation");
        crossoverOperator = operators_.get("crossover");
        selectionOperator = operators_.get("selection");

        ArrayList<Integer> originalArchElementCount;
        originalArchElementCount = new ArrayList<>();
        ArrayList<Integer> newArchElementCount;
        newArchElementCount = new ArrayList<>();

        try {
            Solution solution_base = new Solution(problem_);
            problem_.evaluateConstraints(solution_base);
            problem_.evaluate(solution_base);
            saveBaseHypervolume(solution_base);
            originalArchElementCount = CountArchElements(solution_base);
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
                            parents = selectionComplementary(population);
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

                            newArchElementCount = CountArchElements(child);
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
                SolutionSet front = null;
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
                for (Solution solution : offspringPopulation.getSolutionSet()) {
                    solution.setEvaluation(0);
//                        If you wish block replicated freezed solutions, uncomment this line
//                        for (Element elementsWithPackage : solution.getAlternativeArchitecture().getElementsWithPackages()) {
//                            elementsWithPackage.unsetFreeze();
//                        }
                }
                if (interactive && currentInteraction < maxInteractions && ((generation % intervalInteraction == 0 && generation >= firstInteraction) || generation == firstInteraction)) {
                    offspringPopulation = interactiveFunction.run(offspringPopulation);
                    if (subjectiveAnalyzeAlgorithm == null) {
                        subjectiveAnalyzeAlgorithm = new SubjectiveAnalyzeAlgorithm(new OPLASolutionSet(offspringPopulation), ClassifierAlgorithm.CLUSTERING_MLP);
                        subjectiveAnalyzeAlgorithm.run(null, false);
                    } else {
                        subjectiveAnalyzeAlgorithm.run(new OPLASolutionSet(offspringPopulation), false);
                    }
                    bestOfUserEvaluation.addAll(offspringPopulation.getSolutionSet().stream().filter(p -> (p.getEvaluation() >= 5 && p.getEvaluatedByUser()) || (p.containsArchitecturalEvaluation() && p.getEvaluatedByUser())).collect(Collectors.toList()));
                    currentInteraction++;
                }

                if (interactive && currentInteraction < maxInteractions && Math.abs((currentInteraction * intervalInteraction) + (intervalInteraction / 2)) == generation && generation > firstInteraction) {
                    subjectiveAnalyzeAlgorithm.run(new OPLASolutionSet(offspringPopulation), true);
                }

                if (interactive && subjectiveAnalyzeAlgorithm != null && !subjectiveAnalyzeAlgorithm.isTrained() && currentInteraction >= maxInteractions) {
                    subjectiveAnalyzeAlgorithm.setTrained(true);
                }

                if (interactive && subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained() && currentInteraction >= maxInteractions && ((generation % intervalInteraction == 0 && generation >= firstInteraction) || generation == firstInteraction)) {
                    subjectiveAnalyzeAlgorithm.evaluateSolutionSetSubjectiveAndArchitecturalMLP(new OPLASolutionSet(offspringPopulation), true);
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
        SolutionSet populationOriginal = new Cloner().shallowClone(population);
        Ranking ranking = new Ranking(population);
        SolutionSet subfrontToReturn = ranking.getSubfront(0);
        removeBadSolutions(subfrontToReturn, populationOriginal, interactive);

        subfrontToReturn.setCapacity(subfrontToReturn.getCapacity() + bestOfUserEvaluation.size());
        for (Solution solution : bestOfUserEvaluation) {
            if (!subfrontToReturn.getSolutionSet().contains(solution)) {
                subfrontToReturn.add(solution);
            }
        }

        if (interactive && subjectiveAnalyzeAlgorithm != null && subjectiveAnalyzeAlgorithm.isTrained()) {
            try {
                subjectiveAnalyzeAlgorithm.evaluateSolutionSetSubjectiveAndArchitecturalMLP(new OPLASolutionSet(subfrontToReturn), false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return subfrontToReturn;
    }

    private void removeBadSolutions(SolutionSet population, SolutionSet original, Boolean interactive) {
        if (interactive) {
            for (int i = 0; i < population.getSolutionSet().size(); i++) {
                if (population.get(i).getEvaluation() == 1) {
                    population.remove(i);
                }
            }
        }
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
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    public Solution[] selectionComplementary(SolutionSet pop) {

        ArrayList<ArrayList<Solution>> lstFitness = new ArrayList<>();

        int num_obj = pop.get(0).numberOfObjectives();
        for (int i = 0; i < num_obj; i++) {
            ArrayList<Solution> arrayList = new ArrayList<>();
            lstFitness.add(arrayList);
        }
        for (Solution s : pop.getSolutionSet()) {
            for (int i = 0; i < num_obj; i++) {
                lstFitness.get(i).add(s);
            }
        }

        for (int i = 0; i < num_obj; i++) {
            sortFitnessSoluction(lstFitness.get(i), i);
        }

        Random generator = new Random();
        Solution[] parent = new Solution[2];

        int lstFitness1Selected = 0;
        int lstFitness2Selected = 0;
        if (num_obj == 2) {
            lstFitness2Selected = 1;
        }
        if (num_obj > 2) {
            lstFitness1Selected = generator.nextInt(num_obj);
            lstFitness2Selected = generator.nextInt(num_obj);
            while (lstFitness1Selected == lstFitness2Selected) {
                lstFitness1Selected = generator.nextInt(num_obj);
            }
        }

        ArrayList<Integer> weightsList = new ArrayList<>();
        int qtd_solution = pop.getSolutionSet().size();
        int weight = qtd_solution * 2;
        weightsList.add(weight);

        for (int i = 1; i < qtd_solution; i++) {
            weight = (qtd_solution - i) + weightsList.get(i - 1);
            weightsList.add(weight);
        }
        int max_weight = weightsList.get(weightsList.size() - 1);
        int pos_fitness1 = 0;
        int pos_fitness2 = 0;

        if (num_obj == 1) {
            int rnd = generator.nextInt(max_weight) + 1;
            for (int pos = 0; pos < qtd_solution; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    pos_fitness1 = pos;
                    break;
                }
            }
            rnd = generator.nextInt(max_weight) + 1;
            for (int pos = 0; pos < qtd_solution; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    pos_fitness2 = pos;
                    break;
                }
            }
            while (pos_fitness1 == pos_fitness2) {
                rnd = generator.nextInt(max_weight) + 1;
                for (int pos = 0; pos < qtd_solution; pos++) {
                    if (weightsList.get(pos) >= rnd) {
                        pos_fitness2 = pos;
                        break;
                    }
                }
            }
        } else {
            int rnd = generator.nextInt(max_weight) + 1;
            for (int pos = 0; pos < qtd_solution; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    pos_fitness1 = pos;
                    break;
                }
            }
            rnd = generator.nextInt(max_weight) + 1;
            for (int pos = 0; pos < qtd_solution; pos++) {
                if (weightsList.get(pos) >= rnd) {
                    pos_fitness2 = pos;
                    break;
                }
            }
        }

        parent[0] = lstFitness.get(lstFitness1Selected).get(pos_fitness1);
        parent[1] = lstFitness.get(lstFitness2Selected).get(pos_fitness2);

        for (int i = 1; i < num_obj; i++) {
            lstFitness.get(i).clear();
        }
        lstFitness.clear();

        return parent;
    }

    public void sortFitnessSoluction(ArrayList<Solution> listFitness, int objective) {
        for (int i = 0; i < listFitness.size() - 1; i++) {
            for (int j = i + 1; j < listFitness.size(); j++) {
                if (listFitness.get(i).getObjective(objective) > listFitness.get(j).getObjective(objective)) {
                    Solution aux = listFitness.get(i);
                    listFitness.set(i, listFitness.get(j));
                    listFitness.set(j, aux);
                }
            }
        }
    }

    public ArrayList<Integer> CountArchElements(Solution solution) {

        ArrayList<Integer> countArchElements;
        countArchElements = new ArrayList<>();
        countArchElements.add(0);
        countArchElements.add(0);
        countArchElements.add(0);

        try {

            int tempAtr = 0;
            int tempMet = 0;
            int tempOP = 0;

            Architecture arch = ((Architecture) solution.getDecisionVariables()[0]);

            List<Class> allClasses = new ArrayList<>(arch.getAllClasses());
            for (Class selectedClass : allClasses) {
                tempAtr = tempAtr + selectedClass.getAllAttributes().size();
                tempMet = tempMet + selectedClass.getAllMethods().size();
            }

            List<Interface> allInterface = new ArrayList<>(arch.getAllInterfaces());
            for (Interface selectedInterface : allInterface) {
                tempOP = tempOP + selectedInterface.getOperations().size();
            }

            countArchElements.set(0, tempAtr);
            countArchElements.set(1, tempMet);
            countArchElements.set(2, tempOP);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return countArchElements;
    }

    public boolean IsValidArchElements(ArrayList<Integer> lst1, ArrayList<Integer> lst2) {
        if (!("" + lst1.get(0)).equals("" + lst2.get(0))) {
            return false;
        }
        if (!("" + lst1.get(1)).equals("" + lst2.get(1))) {
            return false;
        }
        if (!("" + lst1.get(2)).equals("" + lst2.get(2))) {
            return false;
        }
        return true;
    }

    public SubjectiveAnalyzeAlgorithm getSubjectiveAnalyzeAlgorithm() {
        return subjectiveAnalyzeAlgorithm;
    }

    public void setSubjectiveAnalyzeAlgorithm(SubjectiveAnalyzeAlgorithm subjectiveAnalyzeAlgorithm) {
        this.subjectiveAnalyzeAlgorithm = subjectiveAnalyzeAlgorithm;
    }
}