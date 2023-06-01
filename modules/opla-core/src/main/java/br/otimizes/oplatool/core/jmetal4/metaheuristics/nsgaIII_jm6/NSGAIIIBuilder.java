package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII_jm6;

import java.util.List;

import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;

/** Builder class */
public class NSGAIIIBuilder {
  // no access modifier means access from classes within the same package
  private Problem problem ;
  private int maxIterations ;
  private int populationSize ;
  private int numberOfDivisions;
  private Operator crossoverOperator ;
  private Operator mutationOperator ;
  private Operator selectionOperator ;

  
  /** Builder constructor */
  public NSGAIIIBuilder(Problem problem) {
    this.problem = problem ;
    maxIterations = 250 ;
    populationSize = 100 ;
    numberOfDivisions = 12;
  }

  public NSGAIIIBuilder setMaxEvaluations(int maxEvaluations) {
    this.maxIterations = maxEvaluations ;

    return this ;
  }

  public NSGAIIIBuilder setPopulationSize(int populationSize) {
    this.populationSize = populationSize ;

    return this ;
  }

  public NSGAIIIBuilder setNumberOfDivisions(int numberOfDivisions){
    this.numberOfDivisions = numberOfDivisions ;

    return this ;
  }

  public NSGAIIIBuilder setCrossoverOperator(Operator crossoverOperator) {
    this.crossoverOperator = crossoverOperator ;

    return this ;
  }

  public NSGAIIIBuilder setMutationOperator(Operator mutationOperator) {
    this.mutationOperator = mutationOperator ;

    return this ;
  }

  public NSGAIIIBuilder setSelectionOperator(Operator selectionOperator) {
    this.selectionOperator = selectionOperator ;

    return this ;
  }

  public Problem getProblem() {
    return problem;
  }

  public int getMaxEvaluations() {
    return maxIterations;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public int getNumberOfDivisions() { return numberOfDivisions; }

  public Operator getCrossoverOperator() {
    return crossoverOperator;
  }

  public Operator getMutationOperator() {
    return mutationOperator;
  }

  public Operator getSelectionOperator() {
    return selectionOperator;
  }

  public NSGAIII build() {
    return new NSGAIII(this) ;
  }
}
