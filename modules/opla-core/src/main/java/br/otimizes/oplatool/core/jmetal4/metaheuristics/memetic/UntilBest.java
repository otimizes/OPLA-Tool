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

import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.core.jmetal4.util.Distance;
import br.otimizes.oplatool.core.jmetal4.util.Ranking;
import br.otimizes.oplatool.core.jmetal4.util.comparators.CrowdingComparator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class implements the UntilBest algorithm.
 */
public class UntilBest extends Algorithm {

    private static final long serialVersionUID = 5815971727148859507L;

    public UntilBest(Problem problem) {
        super(problem);
    } // NSGAII

    /**
     * Runs the NSGA-II algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException
     */
    @SuppressWarnings({"deprecation", "static-access"})
    public synchronized SolutionSet execute() throws JMException, ClassNotFoundException {
        SolutionSet offspringPopulation;
        SolutionSet union;
        Distance distance = new Distance();
        int populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        int maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
        QualityIndicator indicators = (QualityIndicator) getInputParameter("indicators");
        SolutionSet population = new SolutionSet(populationSize);
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
                        Solution[] offSpringForLocal = new Solution[2];

                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);

                        mutationOperator.execute(offSpring[0]);
                        mutationOperator.execute(offSpring[1]);
                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);
                        problem_.evaluate(offSpring[0]);
                        problem_.evaluate(offSpring[1]);

                        OPLASolution a0 = new OPLASolution(offSpring[0]);
                        OPLASolution b1 = new OPLASolution(offSpring[1]);

                        offSpringForLocal[0] = a0.getSolution();
                        offSpringForLocal[1] = b1.getSolution();

                        LocalSearchUntil local0 = new LocalSearchUntil(offSpring[0], offSpringForLocal[0], problem_, operatorLocal);
                        LocalSearchUntil local1 = new LocalSearchUntil(offSpring[1], offSpringForLocal[1], problem_, operatorLocal);

                        try {
                            executor.execute(local0);
                            executor.execute(local1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("erro na thread");
                        }

                        executor.shutdown();
                        while (!executor.isTerminated()) {
                        }

                        offspringPopulation.add(offSpringForLocal[0]);
                        offspringPopulation.add(offSpringForLocal[1]);
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
                if (remain > 0) {
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
            System.out.println("Erro no nsga");
            b.printStackTrace();
        }
        setOutputParameter("evaluations", requiredEvaluations);
        Ranking ranking = new Ranking(population);
        return ranking.getSubfront(0);
    } // execute
} // NSGA-II