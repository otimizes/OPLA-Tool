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
package jmetal4.metaheuristics.nsgaII;

import jmetal4.core.*;
import jmetal4.qualityIndicator.QualityIndicator;
import jmetal4.util.Distance;
import jmetal4.util.JMException;
import jmetal4.util.Ranking;
import jmetal4.util.comparators.CrowdingComparator;

/**
 * This class implements the NSGA-II algorithm.
 */
public class NSGAII extends Algorithm {

    /**
     * Constructor
     *
     * @param problem Problem to solve
     */
    public NSGAII(Problem problem) {
        super(problem);

    } // NSGAII

    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize = (Integer) getInputParameter("populationSize");
        SolutionSet population = new SolutionSet(populationSize);
        int currentEvaluations = 0;

        return execute(population, currentEvaluations);
    }

    private int initializePopulation(SolutionSet population, int populationSize, Operator mutationOperator, int currentEvaluations) {
        try {
            // Create the initial solutionSet
            Solution newSolution;
            for (int i = population.size(); i < populationSize; i++) {
                newSolution = new Solution(problem_);
                // criar a diversidade na populacao inicial
                mutationOperator.execute(newSolution);
                problem_.evaluate(newSolution);

                //problem_.evaluateConstraints(newSolution);
                currentEvaluations++;
                population.add(newSolution);
            } //for  
        } catch (Exception e) {
            System.err.println(e);
        }
        return currentEvaluations;
    }

    /**
     * Runs the NSGA-II algorithm.
     *
     * @param initialPopulation
     * @param currentEvaluations
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException
     * @throws java.lang.ClassNotFoundException
     */
    public SolutionSet execute(SolutionSet initialPopulation, int currentEvaluations) throws JMException, ClassNotFoundException {
        int populationSize;
        int maxEvaluations;

        QualityIndicator indicators; // QualityIndicator object
        int requiredEvaluations; // Use in the example of use of the
        // indicators object (see below)

        SolutionSet offspringPopulation;
        SolutionSet union;

        Operator mutationOperator;
        Operator crossoverOperator;
        Operator selectionOperator;

        Distance distance = new Distance();

        //Read the parameters
        populationSize = ((Integer) getInputParameter("populationSize"));
        maxEvaluations = ((Integer) getInputParameter("maxEvaluations"));
        indicators = (QualityIndicator) getInputParameter("indicators");

//        //Initialize the variables
//        population = new SolutionSet(populationSize);
//        evaluations = 0;
        requiredEvaluations = 0;

        //Read the operators
        mutationOperator = operators_.get("mutation");
        crossoverOperator = operators_.get("crossover");
        selectionOperator = operators_.get("selection");

        if (initialPopulation.size() < populationSize) {
            currentEvaluations = initializePopulation(initialPopulation, populationSize, mutationOperator, currentEvaluations);
        }

        try {
            // Generations 
            while (currentEvaluations < maxEvaluations) {
                System.out.println("==>" + currentEvaluations);
                // Create the offSpring solutionSet      
                offspringPopulation = new SolutionSet(populationSize);
                Solution[] parents = new Solution[2];

                for (int i = 0; i < (populationSize / 2); i++) {
                    if (currentEvaluations < maxEvaluations) {
                        //obtain parents
                        parents[0] = (Solution) selectionOperator.execute(initialPopulation);
                        parents[1] = (Solution) selectionOperator.execute(initialPopulation);

                        Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);

                        mutationOperator.execute(offSpring[0]);
                        mutationOperator.execute(offSpring[1]);
                        problem_.evaluateConstraints(offSpring[0]);
                        problem_.evaluateConstraints(offSpring[1]);

                        problem_.evaluate(offSpring[0]);
                        problem_.evaluate(offSpring[1]);

                        offspringPopulation.add(offSpring[0]);
                        offspringPopulation.add(offSpring[1]);
                        currentEvaluations += 2;
                    } // if                            
                } // for

                // Create the solutionSet union of solutionSet and offSpring
                union = ((SolutionSet) initialPopulation).union(offspringPopulation);

                // Ranking the union
                Ranking ranking = new Ranking(union);

                int remain = populationSize;
                int index = 0;
                SolutionSet front = null;
                initialPopulation.clear();

                // Obtain the next front
                front = ranking.getSubfront(index);

                while ((remain > 0) && (remain >= front.size())) {
                    //Assign crowding distance to individuals
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    //Add the individuals of this front
                    for (int k = 0; k < front.size(); k++) {
                        initialPopulation.add(front.get(k));
                    } // for

                    //Decrement remain
                    remain = remain - front.size();

                    //Obtain the next front
                    index++;
                    if (remain > 0) {
                        front = ranking.getSubfront(index);
                    } // if        
                } // while

                // Remain is less than front(index).size, insert only the best one
                if (remain > 0) {  // front contains individuals to insert                        
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    front.sort(new CrowdingComparator());
                    for (int k = 0; k < remain; k++) {
                        initialPopulation.add(front.get(k));
                    } // for

                    remain = 0;
                } // if                               

                // This piece of code shows how to use the indicator object into the code
                // of NSGA-II. In particular, it finds the number of evaluations required
                // by the algorithm to obtain a Pareto front with a hypervolume higher
                // than the hypervolume of the true Pareto front.
                if ((indicators != null)
                        && (requiredEvaluations == 0)) {
                    double HV = indicators.getHypervolume(initialPopulation);
                    if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
                        requiredEvaluations = currentEvaluations;
                    } // if
                } // if
            } // while
        } catch (Exception e) {

        }

        // Return as output parameter the required evaluations
        setOutputParameter("evaluations", requiredEvaluations);

        // Return the first non-dominated front
        Ranking ranking = new Ranking(initialPopulation);
        return ranking.getSubfront(0);
        //return population;
    } // execute
} // NSGA-II