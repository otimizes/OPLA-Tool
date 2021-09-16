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

import br.otimizes.oplatool.architecture.helpers.StatisticalMethodsHelper;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.common.exceptions.JMException;
import br.otimizes.oplatool.core.jmetal4.core.*;
import br.otimizes.oplatool.core.jmetal4.metrics.Metrics;
import br.otimizes.oplatool.core.jmetal4.operators.mutation.PLAMutationOperator;
import br.otimizes.oplatool.core.jmetal4.problems.OPLA;
import br.otimizes.oplatool.core.jmetal4.qualityIndicator.QualityIndicator;
import br.otimizes.oplatool.core.jmetal4.util.Distance;
import br.otimizes.oplatool.core.jmetal4.util.Ranking;
import br.otimizes.oplatool.core.jmetal4.util.comparators.CrowdingComparator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the NSGA-II algorithm.
 */
public class NSGAIIASP extends Algorithm {
    private static final long serialVersionUID = 5815971727148859507L;
    private static final Logger LOGGER = Logger.getLogger(NSGAII.class);

    public NSGAIIASP(Problem problem) {
        super(problem);
    } // NSGAII

    /**
     * Runs the NSGA-II algorithm.
     *
     * @return a <code>SolutionSet</code> that is a set of non dominated
     * solutions as a result of the algorithm execution
     * @throws JMException
     * @throws Exception
     */
    public SolutionSet execute() throws JMException {
        SolutionSet population;
        SolutionSet offspringPopulation;
        SolutionSet union;
        PLAMutationOperator mutationOperator;
        Distance distance = new Distance();
        int populationSize = ((Integer) getInputParameter("populationSize")).intValue();
        int maxEvaluations = ((Integer) getInputParameter("maxEvaluations")).intValue();
        QualityIndicator indicators = (QualityIndicator) getInputParameter("indicators");
        population = new SolutionSet(populationSize);
        int evaluations = 0;
        int requiredEvaluations = 0;
        mutationOperator = (PLAMutationOperator) operators_.get("mutation");
        Operator crossoverOperator = operators_.get("crossover");
        Operator selectionOperator = operators_.get("selection");
        try {
            LOGGER.info("Calculating threshold CO");
            Solution newSolution1 = new Solution(problem_);
            mutationOperator.setThreshold(detectThreshold(newSolution1));
            LOGGER.info("Original threshold CO: " + mutationOperator.getThreshold()); //print do threashold
            LOGGER.info("Calculating threshold Large Class");
            mutationOperator.setThresholdLc(detectThresholdLc(newSolution1));
            LOGGER.info("Original threshold large class: " + mutationOperator.getThresholdLc()); //print do threashold
            LOGGER.info("Elegance NAC original: " + Metrics.EleganceNAC.evaluate((Architecture) newSolution1.getDecisionVariables()[0]));
            LOGGER.info("Elegance EX original: " + Metrics.EleganceEX.evaluate((Architecture) newSolution1.getDecisionVariables()[0]));
            LOGGER.info("Creating population");
            Solution newSolution;
            for (int i = 0; i < populationSize; i++) {
                newSolution = new Solution(problem_);
                mutationOperator.execute(newSolution);
                while (!(isValidSolution((Architecture) newSolution.getDecisionVariables()[0]))) {
                    newSolution = new Solution(problem_);
                    mutationOperator.execute(newSolution);
                    System.out.println("teste");
                }
                problem_.evaluate(newSolution);
                problem_.evaluateConstraints(newSolution);
                evaluations++;
                population.add(newSolution);
            }
        } catch (Exception e) {
            LOGGER.error(e);
            throw new JMException(e.getMessage());
        }
        try {
            LOGGER.info("Initializing evolutions");
            Solution newSolution1 = new Solution(problem_);
            OPLA opla = (OPLA) problem_;
            LOGGER.info("Calculating threshold LinkOverload");
            ArrayList<Integer> linkOverloadThrz = ((Architecture) newSolution1.getDecisionVariables()[0]).getTHZLinkOverload();
            LOGGER.info("Threshold Link Overload original: " + linkOverloadThrz);
            for (Solution solution : population.getSolutionSet()) {
                ((Architecture) solution.getDecisionVariables()[0]).verifyIfLinkOverloadAnomalyExists(linkOverloadThrz);
                opla.evaluateLinkOverload(solution);
            }
            while (evaluations < maxEvaluations) {
                System.out.println("Number of fitness evaluations: " + evaluations);
                offspringPopulation = new SolutionSet(populationSize);
                Solution[] parents = new Solution[2];
                for (int i = 0; i < (populationSize / 2); i++) {
                    if (evaluations < maxEvaluations) {
                        LOGGER.info("Origin Individual: " + i + " evolution: " + evaluations);
                        parents[0] = (Solution) selectionOperator.execute(population);
                        parents[1] = (Solution) selectionOperator.execute(population);
                        Object execute = crossoverOperator.execute(parents);
                        if (execute instanceof Solution) {
                            Solution offSpring = (Solution) crossoverOperator.execute(parents);
                            if (isValidSolution((Architecture) offSpring.getDecisionVariables()[0])) {
                                problem_.evaluateConstraints(offSpring);
                                mutationOperator.execute(offSpring);
                                if (isValidSolution((Architecture) offSpring.getDecisionVariables()[0])) {
                                    ((Architecture) offSpring.getDecisionVariables()[0]).verifyIfLinkOverloadAnomalyExists(linkOverloadThrz);
                                    problem_.evaluateConstraints(offSpring);
                                    opla.evaluateLinkOverload(offSpring);
                                    offspringPopulation.add(offSpring);
                                } else {
                                    System.out.println("Discarded");
                                    OPLA.contDiscardedSolutions_++;
                                }
                            } else {
                                System.out.println("Discarded");
                                OPLA.contDiscardedSolutions_++;
                            }
                        } else {
                            Solution[] offSpring = (Solution[]) crossoverOperator.execute(parents);
                            if (isValidSolution((Architecture) offSpring[0].getDecisionVariables()[0])) {
                                problem_.evaluateConstraints(offSpring[0]);
                                mutationOperator.execute(offSpring[0]);
                                if (isValidSolution((Architecture) offSpring[0].getDecisionVariables()[0])) {
                                    problem_.evaluateConstraints(offSpring[0]);
                                    ((Architecture) offSpring[0].getDecisionVariables()[0]).verifyIfLinkOverloadAnomalyExists(linkOverloadThrz);
                                    opla.evaluateLinkOverload(offSpring[0]);
                                    offspringPopulation.add(offSpring[0]);
                                } else {
                                    System.out.println("Discarded");
                                    OPLA.contDiscardedSolutions_++;
                                }
                            } else {
                                System.out.println("Discarded");
                                OPLA.contDiscardedSolutions_++;
                            }
                            if (isValidSolution((Architecture) offSpring[1].getDecisionVariables()[0])) {
                                problem_.evaluateConstraints(offSpring[1]);
                                mutationOperator.execute(offSpring[1]);
                                if (isValidSolution((Architecture) offSpring[1].getDecisionVariables()[0])) {
                                    problem_.evaluateConstraints(offSpring[1]);
                                    ((Architecture) offSpring[1].getDecisionVariables()[0]).verifyIfLinkOverloadAnomalyExists(linkOverloadThrz);
                                    opla.evaluateLinkOverload(offSpring[1]);
                                    offspringPopulation.add(offSpring[1]);
                                } else {
                                    System.out.println("Discarded");
                                    OPLA.contDiscardedSolutions_++;
                                }
                            } else {
                                System.out.println("Discarded");
                                OPLA.contDiscardedSolutions_++;
                            }
                        }
                        evaluations += 2;
                    }
                }
                System.out.println("Discarded:" + OPLA.contDiscardedSolutions_);
                LOGGER.info("Union solutions");
                union = population.union(offspringPopulation);
                LOGGER.info("Ranking the union");
                Ranking ranking = new Ranking(union);
                int remain = populationSize;
                int index = 0;
                SolutionSet front;
                population.clear();
                LOGGER.info("getSubfront()");
                front = ranking.getSubfront(index);
                while ((remain > 0) && (remain >= front.size())) {
                    LOGGER.info("crowdingDistanceAssignment()");
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    for (int k = 0; k < front.size(); k++) {
                        population.add(front.get(k));
                    }
                    remain = remain - front.size();
                    index++;
                    if (remain > 0) {
                        LOGGER.info("getSubfront()");
                        front = ranking.getSubfront(index);
                    }
                }
                if (remain > 0) {
                    LOGGER.info("crowdingDistanceAssignment()");
                    distance.crowdingDistanceAssignment(front, problem_.getNumberOfObjectives());
                    front.sort(new CrowdingComparator());
                    for (int k = 0; k < remain; k++) {
                        population.add(front.get(k));
                    }
                }
                union.clear();
                ranking.clear();
                front.clear();
                if ((indicators != null) && (requiredEvaluations == 0)) {
                    double HV = indicators.getHypervolume(population);
                    if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
                        requiredEvaluations = evaluations;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
            throw new JMException(e.getMessage());
        }
        LOGGER.info("setOutputParameter()");
        setOutputParameter("evaluations", requiredEvaluations);
        LOGGER.info("Ranking()");
        Ranking ranking = new Ranking(population);
        return ranking.getSubfront(0);
    }

    public boolean isValidSolution(Architecture solution) {
        final List<Interface> allInterfaces = new ArrayList<Interface>(solution.getAllInterfaces());
        if (!allInterfaces.isEmpty()) {
            for (Interface itf : allInterfaces) {
                if ((itf.getImplementors().isEmpty()) && (itf.getDependents().isEmpty()) && (!itf.getMethods().isEmpty())) {
                    return false;
                }
            }
        }
        return true;
    }

    public int detectThreshold(Solution solution) throws JMException {
        ArrayList<Integer> lstConcernCount = new ArrayList<>();
        final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]); // solução original
        final List<Package> allPackage = new ArrayList<Package>(arch.getAllPackages());
        if (!allPackage.isEmpty()) {
            for (Package selectedPackage : allPackage) {
                List<Class> lstClass = new ArrayList<>(selectedPackage.getAllClasses());
                for (Class selectedClass : lstClass) {
                    lstConcernCount.add(selectedClass.getPriConcerns().size());
                }
                List<Interface> lstInterface = new ArrayList<>(selectedPackage.getAllInterfaces());
                for (Interface selectedInterface : lstInterface) {
                    lstConcernCount.add(selectedInterface.getAllConcerns().size());
                }
            }

        }
        Double meanBrickConcerns = 0.0;
        for (Integer n : lstConcernCount) {
            meanBrickConcerns += n;
        }
        meanBrickConcerns = meanBrickConcerns / lstConcernCount.size();
        Double stdDevOfBrickConcerns = StatisticalMethodsHelper.getStandardDeviation(lstConcernCount);
        double THzb = meanBrickConcerns + stdDevOfBrickConcerns;
        return (int) Math.ceil(THzb);
    }

    public int detectThresholdLc(Solution solution) {
        ArrayList<Integer> attributesAndMethods = new ArrayList<>();
        final Architecture arch = ((Architecture) solution.getDecisionVariables()[0]); // solução original
        for (Class aClass : arch.getAllClasses()) {
            attributesAndMethods.add(aClass.getAllMethods().size() + aClass.getAllAttributes().size());
        }
        Double meanBrickConcerns = 0.0;
        for (Integer n : attributesAndMethods) {
            meanBrickConcerns += n;
        }
        meanBrickConcerns = meanBrickConcerns / attributesAndMethods.size();
        Double stdDevOfBrickConcerns = StatisticalMethodsHelper.getStandardDeviation(attributesAndMethods);
        double THzb = meanBrickConcerns + stdDevOfBrickConcerns;
        return (int) Math.ceil(THzb);
    }
}

