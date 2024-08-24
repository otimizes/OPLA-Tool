//  Solution.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Description:
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
package br.otimizes.oplatool.core.jmetal4.core;

import br.otimizes.isearchai.learning.ml.MLSolution;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.common.Variable;
import br.otimizes.oplatool.core.jmetal4.encodings.variable.Binary;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Class representing a solution for a problem.
 */
public class Solution implements Serializable, MLSolution<Element> {

    /**
     *
     */
    private static final long serialVersionUID = 2508217794094374887L;

    String solutionName;

    /**
     * Stores the problem
     */
    Problem problem_;

    /**
     * Stores the type of the variable
     */
    private SolutionType type_;

    /**
     * Stores the decision variables of the solution.
     */
    private Variable[] variable_;

    /**
     * Stores the objectives values of the solution.
     */
    private double[] objective_;

    // para guardar os valores de cada metrica quando executar o GA
    private double[] objective_temp_;

    /**
     * Stores a list of maximum values for each objective.
     */
    public double[] objectiveMax;

    /**
     * Stores the number of objective values of the solution
     */
    private int numberOfObjectives_;

    /**
     * Stores the so called fitness value. Used in some metaheuristics
     */
    private double fitness_;

    /**
     * Used in algorithm AbYSS, this field is intended to be used to know when a
     * <code>Solution</code> is marked.
     */
    private boolean marked_;

    /**
     * Stores the so called rank of the solution. Used in NSGA-II
     */
    private int rank_;

    /**
     * Stores the overall constraint violation of the solution.
     */
    private double overallConstraintViolation_;

    /**
     * Stores the number of constraints violated by the solution.
     */
    private int numberOfViolatedConstraints_;

    /**
     * This field is intended to be used to know the location of a solution into
     * a <code>SolutionSet</code>. Used in MOCell
     */
    private int location_;

    /**
     * Stores the distance to his k-nearest neighbor into a
     * <code>SolutionSet</code>. Used in SPEA2.
     */
    private double kDistance_;

    /**
     * Stores the crowding distance of the the solution in a
     * <code>SolutionSet</code>. Used in NSGA-II.
     */
    private double crowdingDistance_;

    /**
     * Stores the distance between this solution and a <code>SolutionSet</code>.
     * Used in AbySS.
     */
    private double distanceToSolutionSet_;

    /**
     * Stores the cluster realized by AbstractClustering Filter
     */
    private Double clusterId_;

    private int userEvaluation;

    private String executionId_;

    private String experimentId_;

    private int id;

    private int idOrigem;

    private Boolean clusterNoise_;

    public Boolean evaluatedByUser;

    private double[] normalizedObjective_;

    public Boolean evaluatedByUser3;

    public Boolean ratedSolution;

    /**
     * Constructor.
     */
    private int clusterIDForMetaHeuristics;

    private double vDistance_;

    public Solution() {
        problem_ = null;
        marked_ = false;
        overallConstraintViolation_ = 0.0;
        numberOfViolatedConstraints_ = 0;
        type_ = null;
        variable_ = null;
        objective_ = null;
        objectiveMax = null;
    }

    // Solution

    /**
     * Constructor
     *
     * @param numberOfObjectives Number of objectives of the solution
     *                           <p>
     *                           This constructor is used mainly to read objective values from
     *                           a file to variables of a SolutionSet to apply quality
     *                           indicators
     */
    public Solution(int numberOfObjectives) {
        numberOfObjectives_ = numberOfObjectives;
        objective_ = new double[numberOfObjectives];
        normalizedObjective_ = new double[numberOfObjectives_];
        objectiveMax = new double[numberOfObjectives];
    }

    public Solution(Problem problem) throws ClassNotFoundException {
        problem_ = problem;
        type_ = problem.getSolutionType();
        numberOfObjectives_ = problem.getNumberOfObjectives();
        objective_ = new double[numberOfObjectives_];
        fitness_ = 0.0;
        kDistance_ = 0.0;
        crowdingDistance_ = 0.0;
        distanceToSolutionSet_ = Double.POSITIVE_INFINITY;
        normalizedObjective_ = new double[numberOfObjectives_];
        variable_ = type_.createVariables();
        objectiveMax = new double[numberOfObjectives_];
    }

    // Solution
    public Solution(Problem problem, Variable[] variables) {
        problem_ = problem;
        type_ = problem.getSolutionType();
        numberOfObjectives_ = problem.getNumberOfObjectives();
        objective_ = new double[numberOfObjectives_];
        fitness_ = 0.0;
        kDistance_ = 0.0;
        crowdingDistance_ = 0.0;
        distanceToSolutionSet_ = Double.POSITIVE_INFINITY;
        normalizedObjective_ = new double[numberOfObjectives_];
        variable_ = variables;
        objectiveMax = new double[numberOfObjectives_];
    }

    // Constructor

    /**
     * Copy constructor.
     *
     * @param solution Solution to copy.
     */
    public Solution(Solution solution) {
        problem_ = solution.problem_;
        type_ = solution.type_;
        numberOfObjectives_ = solution.numberOfObjectives();
        objective_ = new double[numberOfObjectives_];
        normalizedObjective_ = new double[numberOfObjectives_];
        for (int i = 0; i < objective_.length; i++) {
            objective_[i] = solution.getObjective(i);
            normalizedObjective_[i] = solution.getNormalizedObjective(i);
        }
        // for
        // <-
        objectiveMax = new double[numberOfObjectives_];
        for (int i = 0; i < objective_.length; i++) {
            objective_[i] = solution.getObjective(i);
        }
        if (solution.objective_temp_ != null) {
            objective_temp_ = new double[solution.objective_temp_.length];
            for (int i = 0; i < objective_temp_.length; i++) {
                objective_temp_[i] = solution.getObjectiveTemp(i);
            }
        }
        variable_ = type_.copyVariables(solution.variable_);
        overallConstraintViolation_ = solution.getOverallConstraintViolation();
        numberOfViolatedConstraints_ = solution.getNumberOfViolatedConstraint();
        distanceToSolutionSet_ = solution.getDistanceToSolutionSet();
        crowdingDistance_ = solution.getCrowdingDistance();
        kDistance_ = solution.getKDistance();
        fitness_ = solution.getFitness();
        marked_ = solution.isMarked();
        rank_ = solution.getRank();
        location_ = solution.getLocation();
    }

    // Solution
    static public Solution getNewSolution(Problem problem) throws ClassNotFoundException {
        return new Solution(problem);
    }

    /**
     * Gets the distance from the solution to a <code>SolutionSet</code>. <b>
     * REQUIRE </b>: this method has to be invoked after calling
     * <code>setDistanceToPopulation</code>.
     *
     * @return the distance to a specific solutionSet.
     */
    public double getDistanceToSolutionSet() {
        return distanceToSolutionSet_;
    }

    // getDistanceToSolutionSet

    /**
     * Sets the distance between this solution and a <code>SolutionSet</code>.
     * The value is stored in <code>distanceToSolutionSet_</code>.
     *
     * @param distance The distance to a solutionSet.
     */
    public void setDistanceToSolutionSet(double distance) {
        distanceToSolutionSet_ = distance;
    }

    // SetDistanceToSolutionSet

    /**
     * Gets the distance from the solution to his k-nearest nighbor in a
     * <code>SolutionSet</code>. Returns the value stored in
     * <code>kDistance_</code>. <b> REQUIRE </b>: this method has to be invoked
     * after calling <code>setKDistance</code>.
     *
     * @return the distance to k-nearest neighbor.
     */
    public double getKDistance() {
        return kDistance_;
    }

    // getKDistance

    /**
     * Sets the distance between the solution and its k-nearest neighbor in a
     * <code>SolutionSet</code>. The value is stored in <code>kDistance_</code>.
     *
     * @param distance The distance to the k-nearest neighbor.
     */
    public void setKDistance(double distance) {
        kDistance_ = distance;
    }

    // setKDistance

    /**
     * Gets the crowding distance of the solution into a
     * <code>SolutionSet</code>. Returns the value stored in
     * <code>crowdingDistance_</code>. <b> REQUIRE </b>: this method has to be
     * invoked after calling <code>setCrowdingDistance</code>.
     *
     * @return the distance crowding distance of the solution.
     */
    public double getCrowdingDistance() {
        return crowdingDistance_;
    }

    // getCrowdingDistance

    /**
     * Sets the crowding distance of a solution in a <code>SolutionSet</code>.
     * The value is stored in <code>crowdingDistance_</code>.
     *
     * @param distance The crowding distance of the solution.
     */
    public void setCrowdingDistance(double distance) {
        crowdingDistance_ = distance;
    }

    // setCrowdingDistance

    /**
     * Gets the fitness of the solution. Returns the value of stored in the
     * variable <code>fitness_</code>. <b> REQUIRE </b>: This method has to be
     * invoked after calling <code>setFitness()</code>.
     *
     * @return the fitness.
     */
    public double getFitness() {
        return fitness_;
    }

    // getFitness

    /**
     * Sets the fitness of a solution. The value is stored in
     * <code>fitness_</code>.
     *
     * @param fitness The fitness of the solution.
     */
    public void setFitness(double fitness) {
        fitness_ = fitness;
    }

    // setFitness

    /**
     * Sets the value of the i-th objective.
     *
     * @param i     The number identifying the objective.
     * @param value The value to be stored.
     */
    public void setObjective(int i, double value) {
        objective_[i] = value;
    }

    // setObjective

    /**
     * Returns the value of the i-th objective.
     *
     * @param i The value of the objective.
     */
    public double getObjective(int i) {
        return objective_[i];
    }

    // getObjective
    public double[] getObjectives() {
        return objective_;
    }

    @JsonIgnore
    public Problem getProblem() {
        return problem_;
    }

    public void setProblem(Problem problem_) {
        this.problem_ = problem_;
    }

    public void createObjectiveTemp(int numberOfObjectives) {
        objective_temp_ = new double[numberOfObjectives];
    }

    /* public void createObjectiveMax(int numberOfObjectives) {
        objectiveMax = new double[numberOfObjectives];
    }

    */
    public void createObjective(int numberOfObjectives) {
        objective_ = new double[numberOfObjectives];
    }

    public void setObjectiveTemp(int i, double value) {
        objective_temp_[i] = value;
    }

    // setObjective
    public double getObjectiveTemp(int i) {
        return objective_temp_[i];
    }

    // getObjective

    /**
     * Returns the number of objectives.
     *
     * @return The number of objectives.
     */
    public int numberOfObjectives() {
        if (objective_ == null)
            return 0;
        else
            return numberOfObjectives_;
    }

    // numberOfObjectives
    public void setNumberOfObjectives(int numberOfObjectives_) {
        this.numberOfObjectives_ = numberOfObjectives_;
    }

    /**
     * Returns the number of decision variables of the solution.
     *
     * @return The number of decision variables.
     */
    public int numberOfVariables() {
        return problem_.getNumberOfVariables();
    }

    // numberOfVariables

    /**
     * Returns a string representing the solution.
     *
     * @return The string.
     */
    public String toString() {
        String aux = "";
        for (int i = 0; i < this.numberOfObjectives_; i++) aux = aux + this.getObjective(i) + " ";
        if (objective_temp_ != null) {
            aux = aux + " -> ";
            for (int i = 0; i < objective_temp_.length; i++) {
                aux = aux + this.getObjectiveTemp(i) + " ";
            }
        }
        return aux;
    }

    // toString
    public String toStringObjectivesTemp() {
        String aux = "";
        if (objective_temp_ != null) {
            for (int i = 0; i < objective_temp_.length; i++) {
                aux = aux + this.getObjectiveTemp(i) + " ";
            }
        }
        return aux;
    }

    public String toStringObjectives() {
        String aux = "";
        if (objective_ != null) {
            for (int i = 0; i < objective_.length; i++) {
                aux = aux + this.getObjective(i) + " ";
            }
        }
        return aux;
    }

    /**
     * Returns the decision variables of the solution.
     *
     * @return the <code>DecisionVariables</code> object representing the
     * decision variables of the solution.
     */
    @JsonIgnore
    public Variable[] getDecisionVariables() {
        return variable_;
    }

    // getDecisionVariables

    /**
     * Sets the decision variables for the solution.
     *
     * @param decisionVariables The <code>DecisionVariables</code> object representing the
     *                          decision variables of the solution.
     */
    public void setDecisionVariables(Variable[] decisionVariables) {
        variable_ = decisionVariables;
    }

    // setDecisionVariables

    /**
     * Indicates if the solution is marked.
     *
     * @return true if the method <code>marked</code> has been called and, after
     * that, the method <code>unmarked</code> hasn't been called. False
     * in other case.
     */
    public boolean isMarked() {
        return this.marked_;
    }

    // isMarked

    /**
     * Establishes the solution as marked.
     */
    public void marked() {
        this.marked_ = true;
    }

    // marked

    /**
     * Established the solution as unmarked.
     */
    public void unMarked() {
        this.marked_ = false;
    }

    // unMarked

    /**
     * Gets the rank of the solution. <b> REQUIRE </b>: This method has to be
     * invoked after calling <code>setRank()</code>.
     *
     * @return the rank of the solution.
     */
    public int getRank() {
        return this.rank_;
    }

    // getRank

    /**
     * Sets the rank of a solution.
     *
     * @param value The rank of the solution.
     */
    public void setRank(int value) {
        this.rank_ = value;
    }

    // setRank

    /**
     * Gets the overall constraint violated by the solution. <b> REQUIRE </b>:
     * This method has to be invoked after calling
     * <code>overallConstraintViolation</code>.
     *
     * @return the overall constraint violation by the solution.
     */
    public double getOverallConstraintViolation() {
        return this.overallConstraintViolation_;
    }

    // getOverallConstraintViolation

    /**
     * Sets the overall constraints violated by the solution.
     *
     * @param value The overall constraints violated by the solution.
     */
    public void setOverallConstraintViolation(double value) {
        this.overallConstraintViolation_ = value;
    }

    // setOverallConstraintViolation

    /**
     * Gets the number of constraint violated by the solution. <b> REQUIRE </b>:
     * This method has to be invoked after calling
     * <code>setNumberOfViolatedConstraint</code>.
     *
     * @return the number of constraints violated by the solution.
     */
    public int getNumberOfViolatedConstraint() {
        return this.numberOfViolatedConstraints_;
    }

    // getNumberOfViolatedConstraint

    /**
     * Sets the number of constraints violated by the solution.
     *
     * @param value The number of constraints violated by the solution.
     */
    public void setNumberOfViolatedConstraint(int value) {
        this.numberOfViolatedConstraints_ = value;
    }

    // setNumberOfViolatedConstraint

    /**
     * Gets the location of this solution in a <code>SolutionSet</code>. <b>
     * REQUIRE </b>: This method has to be invoked after calling
     * <code>setLocation</code>.
     *
     * @return the location of the solution into a solutionSet
     */
    public int getLocation() {
        return this.location_;
    }

    // getLocation

    /**
     * Sets the location of the solution into a solutionSet.
     *
     * @param location The location of the solution.
     */
    public void setLocation(int location) {
        this.location_ = location;
    }

    // setLocation
    /**
     * Sets the type of the variable.
     *
     * @param type
     *            The type of the variable.
     */
    // public void setType(String type) {
    // type_ = Class.forName("") ;
    // } // setType

    /**
     * Gets the type of the variable
     *
     * @return the type of the variable
     */
    @JsonIgnore
    public SolutionType getType() {
        return type_;
    }

    // getType

    /**
     * Sets the type of the variable.
     *
     * @param type The type of the variable.
     */
    public void setType(SolutionType type) {
        type_ = type;
    }

    // setType

    /**
     * Returns the aggregative value of the solution
     *
     * @return The aggregative value.
     */
    public double getAggregativeValue() {
        double value = 0.0;
        for (int i = 0; i < numberOfObjectives(); i++) {
            value += getObjective(i);
        }
        return value;
    }

    // getAggregativeValue
    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    /**
     * Returns the number of bits of the chromosome in case of using a binary
     * representation
     *
     * @return The number of bits if the case of binary variables, 0 otherwise
     */
    @JsonIgnore
    public int getNumberOfBits() {
        int bits = 0;
        for (int i = 0; i < variable_.length; i++)
            try {
                if ((variable_[i].getVariableType() == Class.forName("br.otimizes.oplatool.core.jmetal4.encodings.variable.Binary")) || (variable_[i].getVariableType() == Class.forName("br.otimizes.oplatool.core.jmetal4.encodings.variable.BinaryReal")))
                    bits += ((Binary) (variable_[i])).getNumberOfBits();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        return bits;
    }

    // getNumberOfBits
    public Double getClusterId() {
        return clusterId_;
    }

    public void setClusterId(Double clusterId_) {
        this.clusterId_ = clusterId_;
    }

    public int getEvaluation() {
        return userEvaluation;
    }

    public void setEvaluation(int userEvaluation) {
        this.userEvaluation = userEvaluation;
    }

    @Override
    @JsonIgnore
    public List<Element> getElements() {
        return getAlternativeArchitecture().getElementsWithPackages();
    }

    public String getExecutionId() {
        return executionId_;
    }

    public void setExecutionId(String executionId_) {
        this.executionId_ = executionId_;
    }

    public Boolean getClusterNoise() {
        return clusterNoise_ != null && clusterNoise_;
    }

    public void setClusterNoise(Boolean clusterNoise_) {
        this.clusterNoise_ = clusterNoise_;
    }

    @JsonIgnore
    public boolean containsElementsEvaluation() {
        for (Element element : getAlternativeArchitecture().getElementsWithPackages()) {
            if (element.isFreezeByDM()) {
                return true;
            }
        }
        return false;
    }

    public Architecture getAlternativeArchitecture() {
        return (Architecture) getDecisionVariables()[0];
    }

    public boolean getEvaluatedByUser() {
        return evaluatedByUser != null && evaluatedByUser;
    }

    public void setEvaluatedByUser(Boolean evaluatedByUser) {
        this.evaluatedByUser = evaluatedByUser;
    }

    public Boolean getEvaluatedByUser3() {
        return evaluatedByUser3 != null && evaluatedByUser3;
    }

    public void setEvaluatedByUser3(Boolean evaluatedByUser) {
        this.evaluatedByUser3 = evaluatedByUser;
    }

    public Boolean getRatedSolution() {
        return ratedSolution != null && ratedSolution;
    }

    public void setRatedSolution(Boolean ratedSolution) {
        this.ratedSolution = ratedSolution;
    }

    public String getExperimentId() {
        return experimentId_;
    }

    public void setExperimentId(String experimentId_) {
        this.experimentId_ = experimentId_;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNormalizedObjective(int i, double value) {
        normalizedObjective_[i] = value;
    }

    public double getNormalizedObjective(int i) {
        return normalizedObjective_[i];
    }

    public void setClusterIDForMetaHeuristics(int id) {
        this.clusterIDForMetaHeuristics = id;
    }

    public int getClusterIDForMetaHeuristics() {
        return this.clusterIDForMetaHeuristics;
    }

    public void setVDistance(double val) {
        this.vDistance_ = val;
    }

    public double getVDistance() {
        return this.vDistance_;
    }

    public int getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(int idOrigem) {
        this.idOrigem = idOrigem;
    }

    /**
     * Weigh the objective according to the assessments
     *
     * @param objective  The objective value that will be weighted.
     * @param evaluation The value of evaluation (3 or 4).
     */
    public double getObjectiveWithWeight(double objective, int evaluation) {
        double weightsEvaluate4 = 0.14;
        double weightsEvaluate3 = 0.06;
        double value = objective;
        if (evaluation == 4) {
            value = (objective - objective * weightsEvaluate4);
        }
        if (evaluation == 3) {
            value = (objective - objective * weightsEvaluate3);
        }
        return value;
    }

    /**
     * Sets the value of the i-th objective.
     *
     * @param i     The number identifying the objective.
     * @param value The value to be stored.
     */
    public void setObjectiveMax(int i, double value) {
        objectiveMax[i] = value;
    }

    // setObjective

    /**
     * Returns the maximum value of the i-th objective.
     *
     * @param i The value of the objective.
     */
    public double getObjectiveMax(int i) {
        return objectiveMax[i];
    }

    // getObjective

    /**
     * run through all objectives to check the biggest
     *
     * @return returns a solution with the highest objective values
     */
    public void checkMajorObjective(SolutionSet solutionSet) {
        for (int i = 0; i < solutionSet.get(0).numberOfObjectives(); i++) {
            double maxObjective = 0;
            for (int j = 0; j < solutionSet.size(); j++) {
                if (maxObjective < solutionSet.get(j).getObjective(i)) {
                    maxObjective = solutionSet.get(j).getObjective(i);
                }
            }
            setObjectiveMax(i, maxObjective);
        }
    }
}
// Solution
