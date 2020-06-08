package br.otimizes.oplatool.core.jmetal4.metaheuristics.memetic;
//NSGAII.java
//
//Author:
//   Antonio J. Nebro <antonio@lcc.uma.es>
//   Juan J. Durillo <durillo@lcc.uma.es>
//
//Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.core.jmetal4.util.Distance;
import br.otimizes.oplatool.core.jmetal4.util.Ranking;
import br.otimizes.oplatool.core.jmetal4.util.comparators.CrowdingComparator;

/**
 * This class implements the BestOf2 algorithm.
 */
public class Bestof2 extends Algorithm {

    private static final long serialVersionUID = 5815971727148859507L;

    /**
     * Constructor
     *
     * @param problem Problem to solve
     */
    public Bestof2(Problem problem) {
        super(problem);

    }

    public SolutionSet solutionBest(SolutionSet result) {
        double vConventional = 0;
        double vPatterns = 0;
        SolutionSet best = result;
        try {
            for (int k = 0; k < result.get(0).numberOfObjectives(); k++) {
                vConventional += result.get(0).getObjective(k);
                vPatterns += result.get(1).getObjective(k);
            }

            if (vConventional < vPatterns) {
                result.remove(1);
                best = result;

            } else if (vPatterns < vConventional) {
                result.remove(0);
                best = result;
            } else {
                result.remove(0);
                best = result;
            }
            return best;
        } catch (Exception e) {
            e.printStackTrace();
            return best;
        }

    }

    /**
     * Runs the NSGA-II algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize;
        int maxEvaluations;
        int evaluations;

        QualityIndicator indicators; // QualityIndicator object
        int requiredEvaluations; // Use in the example of use of the
        // indicators object (see below)

        SolutionSet population;
        SolutionSet offspringPopulation;
        SolutionSet union;

        Operator mutationOperator;
        // jcn
        Operator operatorLocal;

        Operator crossoverOperator;
        Operator selectionOperator;

        Distance distance = new Distance();

        // Read the parameters
        populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
        indicators = (QualityIndicator) getInputParameter("indicators");

        // Initialize the variables
        population = new SolutionSet(populationSize);
        evaluations = 0;

        requiredEvaluations = 0;

        // Read the operators
        mutationOperator = operators_.get("mutation");
        crossoverOperator = operators_.get("crossover");
        selectionOperator = operators_.get("selection");
        operatorLocal = operators_.get("operatorLocal");

        try {
            // Create the initial solutionSet
            Solution newSolution;

            // extrair fitness original
            //newSolution = new Solution(problem_);
            //System.out.println("parar");
            //problem_.evaluate(newSolution);
            //OptionPane.showMessageDialog(null, "fitness original");

            for (int i = 0; i < populationSize; i++) {
                // criar a diversidade na populacao inicial
                newSolution = new Solution(problem_);
                mutationOperator.execute(newSolution);
                //jcn
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
                System.out.println("\n ||||||| \n");
                System.out.println("Evolucao:" + evaluations);
                System.out.println("\n ||||||| \n");

                offspringPopulation = new SolutionSet(populationSize);
                Solution[] parents = new Solution[2];

                // crossover
                for (int i = 0; i < (populationSize / 2); i++) {
                    if (evaluations < maxEvaluations) {
                        System.out.println("\n --- \n");
                        System.out.println("Bestof2 INDIVIDUO: " + i + " evolucao: " + evaluations + " \n");
                        System.out.println("\n ---\n ");
                        // tempo
                        long timeBegin = System.currentTimeMillis();
                        // tempo
                        parents[0] = (Solution) selectionOperator.execute(population);
                        parents[1] = (Solution) selectionOperator.execute(population);

                        Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                        //////////
                        Solution[] offSpringForLocal = new Solution[2];
                        //////////

                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);

                        // operador convencional begin

                        mutationOperator.execute(offSpring[0]);
                        mutationOperator.execute(offSpring[1]);

                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);

                        problem_.evaluate(offSpring[0]);
                        problem_.evaluate(offSpring[1]);
                        // operador convencional end
                        // ------------------------------------------------------

                        OPLASolution a = new OPLASolution(offSpring[0]);
                        OPLASolution b = new OPLASolution(offSpring[1]);
                        offSpringForLocal[0] = a.getSolution();
                        offSpringForLocal[1] = b.getSolution();

                        operatorLocal.execute(offSpringForLocal[0]);
                        operatorLocal.execute(offSpringForLocal[1]);

                        problem_.evaluateConstraints(offSpringForLocal[0]);
                        problem_.evaluateConstraints(offSpringForLocal[1]);

                        problem_.evaluate(offSpringForLocal[0]);
                        problem_.evaluate(offSpringForLocal[1]);

                        SolutionSet comparation0 = new SolutionSet(2);
                        SolutionSet comparation1 = new SolutionSet(2);

                        comparation0.add(offSpring[0]);
                        comparation0.add(offSpringForLocal[0]);

                        comparation1.add(offSpring[1]);
                        comparation1.add(offSpringForLocal[1]);

                        SolutionSet best0 = new SolutionSet(2);
                        SolutionSet best1 = new SolutionSet(2);
                        best0 = this.solutionBest(comparation0);
                        best1 = this.solutionBest(comparation1);

                        offspringPopulation.add(best0.get(0));
                        offspringPopulation.add(best1.get(0));
                        evaluations += 2;
                    }

                }
                // Create the solutionSet union of solutionSet and offSpring
                union = ((SolutionSet) population).union(offspringPopulation);

                // Ranking the union
                Ranking ranking = new Ranking(union);

                int remain = populationSize;
                int index = 0;
                SolutionSet front = null;
                population.clear();

                // Obtain the next front
                front = ranking.getSubfront(index);

                while ((remain > 0) && (remain >= front.size())) {
                    // Assign crowding distance to individuals
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    // Add the individuals of this front
                    for (int k = 0; k < front.size(); k++) {
                        population.add(front.get(k));
                    }

                    // Decrement remain
                    remain = remain - front.size();

                    // Obtain the next front
                    index++;
                    if (remain > 0) {
                        front = ranking.getSubfront(index);
                    }
                }

                // Remain is less than front(index).size, insert only the best
                // one
                if (remain > 0) { // front contains individuals to insert
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    front.sort(new CrowdingComparator());
                    for (int k = 0; k < remain; k++) {
                        population.add(front.get(k));
                    }
                    remain = 0;
                }

                // This piece of code shows how to use the indicator object into
                // the code
                // of NSGA-II. In particular, it finds the number of evaluations
                // required
                // by the algorithm to obtain a Pareto front with a hypervolume
                // higher
                // than the hypervolume of the true Pareto front.
                if ((indicators != null) && (requiredEvaluations == 0)) {
                    double HV = indicators.getHypervolume(population);
                    if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
                        requiredEvaluations = evaluations;
                    }
                }
            }

        } catch (Exception b) {
            System.out.println("Erro no Bestof2");
            b.printStackTrace();
        }

        // Return as output parameter the required evaluations
        setOutputParameter("evaluations", requiredEvaluations);

        // Return the first non-dominated front
        Ranking ranking = new Ranking(population);
        // System.out.println(ranking);
        return ranking.getSubfront(0);
        // return population;
    } // execute
} // NSGA-II