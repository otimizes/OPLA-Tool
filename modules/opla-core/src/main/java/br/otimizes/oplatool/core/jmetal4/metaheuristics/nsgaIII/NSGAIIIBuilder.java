package br.otimizes.oplatool.core.jmetal4.metaheuristics.nsgaIII;

import br.otimizes.oplatool.core.jmetal4.core.Operator;
import br.otimizes.oplatool.core.jmetal4.core.Problem;
import br.otimizes.isearchai.interactive.InteractiveHandler;

/** Builder class */
public class NSGAIIIBuilder {
    // no access modifier means access from classes within the same package
    private Problem problem;
    private int maxIterations;
    private int populationSize;
    private int numberOfDivisions;
    private Operator crossoverOperator;
    private Operator mutationOperator;
    private Operator selectionOperator;
    private InteractiveHandler interactive;

    /** Builder constructor */
    public NSGAIIIBuilder(Problem problem) {
        this.problem = problem;
        maxIterations = 250;
        populationSize = 100;
        numberOfDivisions = 12;
        interactive = null;
    }

    public NSGAIIIBuilder setMaxEvaluations(int maxEvaluations) {
        this.maxIterations = maxEvaluations;

        return this;
    }

    public NSGAIIIBuilder setPopulationSize(int populationSize) {
        this.populationSize = populationSize;

        return this;
    }

    public NSGAIIIBuilder setNumberOfDivisions(int numberOfDivisions) {
        this.numberOfDivisions = numberOfDivisions;

        return this;
    }

    public NSGAIIIBuilder setCrossoverOperator(Operator crossoverOperator) {
        this.crossoverOperator = crossoverOperator;

        return this;
    }

    public NSGAIIIBuilder setMutationOperator(Operator mutationOperator) {
        this.mutationOperator = mutationOperator;

        return this;
    }

    public NSGAIIIBuilder setSelectionOperator(Operator selectionOperator) {
        this.selectionOperator = selectionOperator;

        return this;
    }

    public NSGAIIIBuilder setInteractive(InteractiveHandler interactive) {
        this.interactive = interactive;

        return this;
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

    public int getNumberOfDivisions() {
        return numberOfDivisions;
    }

    public Operator getCrossoverOperator() {
        return crossoverOperator;
    }

    public Operator getMutationOperator() {
        return mutationOperator;
    }

    public Operator getSelectionOperator() {
        return selectionOperator;
    }

    public InteractiveHandler getInteractive() {
        return interactive;
    }

    public NSGAIII build() {
        return new NSGAIII(this);
    }
}
