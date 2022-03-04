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

package br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.Algorithm;
import br.otimizes.oplatool.core.jmetal4.core.OPLASolution;
import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.oplatool.core.jmetal4.core.Solution;
import br.otimizes.oplatool.core.jmetal4.core.SolutionSet;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.core.jmetal4.util.Distance;
import br.otimizes.oplatool.core.jmetal4.util.Ranking;
import br.otimizes.oplatool.core.jmetal4.util.comparators.CrowdingComparator;

/**
 * This class implements the BestOf12 algorithm.
 */
public class Bestof12 extends Algorithm {

    private static final long serialVersionUID = 5815971727148859507L;

    public Bestof12(Problem problem) {
        super(problem);

    }

    /**
     * Runs the NSGA-II algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     *         solutions as a result of the algorithm execution
     * @throws JMException
     */
    public synchronized SolutionSet execute() throws JMException, ClassNotFoundException {
        SolutionSet population;
        SolutionSet offspringPopulation;
        SolutionSet union;
        Distance distance = new Distance();
        int populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        int maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
        QualityIndicator indicators = (QualityIndicator) getInputParameter("indicators");
        population = new SolutionSet(populationSize);
        int evaluations = 0;
        int requiredEvaluations = 0;
        Operator mutationOperator = operators_.get("mutation");
        Operator crossoverOperator = operators_.get("crossover");
        Operator selectionOperator = operators_.get("selection");
        Operator operatorLocal = operators_.get("operatorLocal");
        try {
            Solution newSolution;
            for (int i = 0; i < populationSize; i++) {
                newSolution = new Solution(problem_);
                mutationOperator.execute(newSolution);
                operatorLocal.execute(newSolution);
                problem_.evaluate(newSolution);
                evaluations++;
                population.add(newSolution);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        try {
            while (evaluations < maxEvaluations) {
                offspringPopulation = new SolutionSet(populationSize);
                Solution[] parents = new Solution[2];
                for (int i = 0; i < (populationSize / 2); i++) {
                    if (evaluations < maxEvaluations) {
                        ExecutorService executor = Executors.newFixedThreadPool(12);
                        parents[0] = (Solution) selectionOperator.execute(population);
                        parents[1] = (Solution) selectionOperator.execute(population);

                        Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                        Solution[] offSpringForLocal0 = new Solution[2];
                        Solution[] offSpringForLocal1 = new Solution[2];
                        Solution[] offSpringForLocal2 = new Solution[2];
                        Solution[] offSpringForLocal3 = new Solution[2];
                        Solution[] offSpringForLocal4 = new Solution[2];
                        Solution[] offSpringForLocal5 = new Solution[2];
                        Solution[] offSpringForLocal6 = new Solution[2];
                        Solution[] offSpringForLocal7 = new Solution[2];
                        Solution[] offSpringForLocal8 = new Solution[2];
                        Solution[] offSpringForLocal9 = new Solution[2];
                        Solution[] offSpringForLocal10 = new Solution[2];
                        Solution[] offSpringForLocal11 = new Solution[2];

                        double[] arraySolutionBest0 = new double[12];
                        double[] arraySolutionBest1 = new double[12];

                        SolutionSet solutionsLocal0 = new SolutionSet(12);
                        SolutionSet solutionsLocal1 = new SolutionSet(12);

                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);

                        mutationOperator.execute(offSpring[0]);
                        mutationOperator.execute(offSpring[1]);

                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);

                        OPLASolution a0 = new OPLASolution(offSpring[0]);
                        OPLASolution b0 = new OPLASolution(offSpring[1]);
                        OPLASolution a1 = new OPLASolution(offSpring[0]);
                        OPLASolution b1 = new OPLASolution(offSpring[1]);
                        OPLASolution a2 = new OPLASolution(offSpring[0]);
                        OPLASolution b2 = new OPLASolution(offSpring[1]);
                        OPLASolution a3 = new OPLASolution(offSpring[0]);
                        OPLASolution b3 = new OPLASolution(offSpring[1]);
                        OPLASolution a4 = new OPLASolution(offSpring[0]);
                        OPLASolution b4 = new OPLASolution(offSpring[1]);
                        OPLASolution a5 = new OPLASolution(offSpring[0]);
                        OPLASolution b5 = new OPLASolution(offSpring[1]);
                        OPLASolution a6 = new OPLASolution(offSpring[0]);
                        OPLASolution b6 = new OPLASolution(offSpring[1]);
                        OPLASolution a7 = new OPLASolution(offSpring[0]);
                        OPLASolution b7 = new OPLASolution(offSpring[1]);
                        OPLASolution a8 = new OPLASolution(offSpring[0]);
                        OPLASolution b8 = new OPLASolution(offSpring[1]);
                        OPLASolution a9 = new OPLASolution(offSpring[0]);
                        OPLASolution b9 = new OPLASolution(offSpring[1]);
                        OPLASolution a10 = new OPLASolution(offSpring[0]);
                        OPLASolution b10 = new OPLASolution(offSpring[1]);
                        OPLASolution a11 = new OPLASolution(offSpring[0]);
                        OPLASolution b11 = new OPLASolution(offSpring[1]);

                        offSpringForLocal0[0] = a0.getSolution();
                        offSpringForLocal0[1] = b0.getSolution();
                        offSpringForLocal1[0] = a1.getSolution();
                        offSpringForLocal1[1] = b1.getSolution();
                        offSpringForLocal2[0] = a2.getSolution();
                        offSpringForLocal2[1] = b2.getSolution();
                        offSpringForLocal3[0] = a3.getSolution();
                        offSpringForLocal3[1] = b3.getSolution();
                        offSpringForLocal4[0] = a4.getSolution();
                        offSpringForLocal4[1] = b4.getSolution();
                        offSpringForLocal5[0] = a5.getSolution();
                        offSpringForLocal5[1] = b5.getSolution();
                        offSpringForLocal6[0] = a6.getSolution();
                        offSpringForLocal6[1] = b6.getSolution();
                        offSpringForLocal7[0] = a7.getSolution();
                        offSpringForLocal7[1] = b7.getSolution();
                        offSpringForLocal8[0] = a8.getSolution();
                        offSpringForLocal8[1] = b8.getSolution();
                        offSpringForLocal9[0] = a9.getSolution();
                        offSpringForLocal9[1] = b9.getSolution();
                        offSpringForLocal10[0] = a10.getSolution();
                        offSpringForLocal10[1] = b10.getSolution();
                        offSpringForLocal11[0] = a11.getSolution();
                        offSpringForLocal11[1] = b11.getSolution();

                        LocalSearch local0 = new LocalSearch(offSpringForLocal0, problem_, operatorLocal);
                        LocalSearch local1 = new LocalSearch(offSpringForLocal1, problem_, operatorLocal);
                        LocalSearch local2 = new LocalSearch(offSpringForLocal2, problem_, operatorLocal);
                        LocalSearch local3 = new LocalSearch(offSpringForLocal3, problem_, operatorLocal);
                        LocalSearch local4 = new LocalSearch(offSpringForLocal4, problem_, operatorLocal);
                        LocalSearch local5 = new LocalSearch(offSpringForLocal5, problem_, operatorLocal);
                        LocalSearch local6 = new LocalSearch(offSpringForLocal6, problem_, operatorLocal);
                        LocalSearch local7 = new LocalSearch(offSpringForLocal7, problem_, operatorLocal);
                        LocalSearch local8 = new LocalSearch(offSpringForLocal8, problem_, operatorLocal);
                        LocalSearch local9 = new LocalSearch(offSpringForLocal9, problem_, operatorLocal);
                        LocalSearch local10 = new LocalSearch(offSpringForLocal10, problem_, operatorLocal);
                        LocalSearch local11 = new LocalSearch(offSpringForLocal11, problem_, operatorLocal);

                        try {
                            executor.execute(local0);
                            executor.execute(local1);
                            executor.execute(local2);
                            executor.execute(local3);
                            executor.execute(local4);
                            executor.execute(local5);
                            executor.execute(local6);
                            executor.execute(local7);
                            executor.execute(local8);
                            executor.execute(local9);
                            executor.execute(local10);
                            executor.execute(local11);

                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("error on thread");
                        }
                        executor.shutdown();
                        while (!executor.isTerminated()) {
                        }

                        solutionsLocal0.add(offSpringForLocal0[0]);
                        solutionsLocal1.add(offSpringForLocal0[1]);

                        solutionsLocal0.add(offSpringForLocal1[0]);
                        solutionsLocal1.add(offSpringForLocal1[1]);

                        solutionsLocal0.add(offSpringForLocal2[0]);
                        solutionsLocal1.add(offSpringForLocal2[1]);

                        solutionsLocal0.add(offSpringForLocal3[0]);
                        solutionsLocal1.add(offSpringForLocal3[1]);

                        solutionsLocal0.add(offSpringForLocal4[0]);
                        solutionsLocal1.add(offSpringForLocal4[1]);

                        solutionsLocal0.add(offSpringForLocal5[0]);
                        solutionsLocal1.add(offSpringForLocal5[1]);

                        solutionsLocal0.add(offSpringForLocal6[0]);
                        solutionsLocal1.add(offSpringForLocal6[1]);

                        solutionsLocal0.add(offSpringForLocal7[0]);
                        solutionsLocal1.add(offSpringForLocal7[1]);

                        solutionsLocal0.add(offSpringForLocal8[0]);
                        solutionsLocal1.add(offSpringForLocal8[1]);

                        solutionsLocal0.add(offSpringForLocal9[0]);
                        solutionsLocal1.add(offSpringForLocal9[1]);

                        solutionsLocal0.add(offSpringForLocal10[0]);
                        solutionsLocal1.add(offSpringForLocal10[1]);

                        solutionsLocal0.add(offSpringForLocal11[0]);
                        solutionsLocal1.add(offSpringForLocal11[1]);

                        for (int k = 0; k < offSpring[0].numberOfObjectives(); k++) {
                            arraySolutionBest0[0] += offSpringForLocal0[0].getObjective(k);
                            arraySolutionBest1[0] += offSpringForLocal0[1].getObjective(k);
                            arraySolutionBest0[1] += offSpringForLocal1[0].getObjective(k);
                            arraySolutionBest1[1] += offSpringForLocal1[1].getObjective(k);
                            arraySolutionBest0[2] += offSpringForLocal2[0].getObjective(k);
                            arraySolutionBest1[2] += offSpringForLocal2[1].getObjective(k);
                            arraySolutionBest0[3] += offSpringForLocal3[0].getObjective(k);
                            arraySolutionBest1[3] += offSpringForLocal3[1].getObjective(k);
                            arraySolutionBest0[4] += offSpringForLocal4[0].getObjective(k);
                            arraySolutionBest1[4] += offSpringForLocal4[1].getObjective(k);
                            arraySolutionBest0[5] += offSpringForLocal5[0].getObjective(k);
                            arraySolutionBest1[5] += offSpringForLocal5[1].getObjective(k);
                            arraySolutionBest0[6] += offSpringForLocal6[0].getObjective(k);
                            arraySolutionBest1[6] += offSpringForLocal6[1].getObjective(k);
                            arraySolutionBest0[7] += offSpringForLocal7[0].getObjective(k);
                            arraySolutionBest1[7] += offSpringForLocal7[1].getObjective(k);
                            arraySolutionBest0[8] += offSpringForLocal8[0].getObjective(k);
                            arraySolutionBest1[8] += offSpringForLocal8[1].getObjective(k);
                            arraySolutionBest0[9] += offSpringForLocal9[0].getObjective(k);
                            arraySolutionBest1[9] += offSpringForLocal9[1].getObjective(k);
                            arraySolutionBest0[10] += offSpringForLocal10[0].getObjective(k);
                            arraySolutionBest1[10] += offSpringForLocal10[1].getObjective(k);
                            arraySolutionBest0[11] += offSpringForLocal11[0].getObjective(k);
                            arraySolutionBest1[11] += offSpringForLocal11[1].getObjective(k);
                        }

                        int position0 = 0, position1 = 0;
                        double min0 = arraySolutionBest0[0];
                        double min1 = arraySolutionBest1[0];
                        for (int w = 0; w < 12; w++) {
                            if (arraySolutionBest0[w] < min0) {
                                min0 = arraySolutionBest0[w];
                                position0 = w;
                            }
                            if (arraySolutionBest1[w] < min1) {
                                min1 = arraySolutionBest1[w];
                                position1 = w;
                            }
                        }

                        offspringPopulation.add(solutionsLocal0.get(position0));
                        offspringPopulation.add(solutionsLocal1.get(position1));
                        evaluations += 2;
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
                if (remain > 0) { // front contains individuals to insert
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    front.sort(new CrowdingComparator());
                    for (int k = 0; k < remain; k++) {
                        population.add(front.get(k));
                    }
                    remain = 0;
                }
                if ((indicators != null) && (requiredEvaluations == 0)) {
                    double HV = indicators.getHypervolume(population);
                    if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
                        requiredEvaluations = evaluations;
                    }
                }
            }
        } catch (Exception b) {
            System.out.println("Erro no Bestof12");
            b.printStackTrace();
        }
        setOutputParameter("evaluations", requiredEvaluations);
        Ranking ranking = new Ranking(population);
        return ranking.getSubfront(0);
    }
}