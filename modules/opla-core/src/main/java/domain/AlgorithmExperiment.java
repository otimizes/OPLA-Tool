package domain;

import jmetal4.interactive.InteractiveFunction;
import learning.ClusteringAlgorithm;
import learning.Moment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlgorithmExperiment {
    private OptimizationAlgorithm algorithm;
    private String description;
    private Boolean mutation;
    private Double mutationProbability;
    private String inputArchitecture;
    private Integer numberRuns;
    private Integer populationSize;
    private Integer maxEvaluations;
    private Boolean crossover;
    private Double crossoverProbability;
    private Boolean interactive;
    private Integer maxInteractions;
    private ClusteringAlgorithm clusteringAlgorithm;
    private Moment clusteringMoment;
    private InteractiveFunction interactiveFunction;
    private List<String> mutationOperators = new ArrayList<>();
    private List<String> patterns = new ArrayList<>();
    private List<String> objectiveFunctions = new ArrayList<>();

    public AlgorithmExperiment() {
    }

    public AlgorithmExperiment(OptimizationAlgorithm algorithm, String description, Boolean mutation, Double mutationProbability,
                               String inputArchitecture, Integer numberRuns, Integer populationSize, Integer maxEvaluations, Boolean crossover,
                               Double crossoverProbability, Boolean interactive, Integer maxInteractions, ClusteringAlgorithm clusteringAlgorithm,
                               Moment clusteringMoment, InteractiveFunction interactiveFunction) {
        this.algorithm = algorithm;
        this.description = description;
        this.mutation = mutation;
        this.mutationProbability = mutationProbability;
        this.inputArchitecture = inputArchitecture;
        this.numberRuns = numberRuns;
        this.populationSize = populationSize;
        this.maxEvaluations = maxEvaluations;
        this.crossover = crossover;
        this.crossoverProbability = crossoverProbability;
        this.interactive = interactive;
        this.maxInteractions = maxInteractions;
        this.clusteringAlgorithm = clusteringAlgorithm;
        this.clusteringMoment = clusteringMoment;
        this.interactiveFunction = interactiveFunction;
    }

    public AlgorithmExperiment(String algorithm, String description, String mutation, String mutationProbability,
                               String inputArchitecture, String numberRuns, String populationSize, String maxEvaluations,
                               String crossover, String crossoverProbability, String interactive, String maxInteractions,
                               String clusteringAlgorithm, String clusteringMoment, String interactiveFunction,
                               String mutationOperators, String patterns, String objectiveFunctions) {
        this.algorithm = OptimizationAlgorithm.valueOf(algorithm);
        this.description = description;
        this.mutation = Boolean.valueOf(mutation);
        this.mutationProbability = Double.valueOf(mutationProbability);
        this.inputArchitecture = inputArchitecture;
        this.numberRuns = Integer.valueOf(numberRuns);
        this.populationSize = Integer.valueOf(populationSize);
        this.maxEvaluations = Integer.valueOf(maxEvaluations);
        this.crossover = Boolean.valueOf(crossover);
        this.crossoverProbability = Double.valueOf(crossoverProbability);
        this.interactive = false;
        this.maxInteractions = 5;
        this.clusteringAlgorithm = ClusteringAlgorithm.valueOf(clusteringAlgorithm);
        this.clusteringMoment = Moment.valueOf(clusteringMoment);
        this.interactiveFunction = null;
        this.mutationOperators = Arrays.asList(mutationOperators.split(","));
        if (patterns != null)
            this.patterns = Arrays.asList(patterns.split(","));
        this.objectiveFunctions = Arrays.asList(objectiveFunctions.split(","));
    }

    public OptimizationAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(OptimizationAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getMutation() {
        return mutation;
    }

    public void setMutation(Boolean mutation) {
        this.mutation = mutation;
    }

    public Double getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(Double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public String getInputArchitecture() {
        return inputArchitecture;
    }

    public void setInputArchitecture(String inputArchitecture) {
        this.inputArchitecture = inputArchitecture;
    }

    public Integer getNumberRuns() {
        return numberRuns;
    }

    public void setNumberRuns(Integer numberRuns) {
        this.numberRuns = numberRuns;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(Integer populationSize) {
        this.populationSize = populationSize;
    }

    public Integer getMaxEvaluations() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(Integer maxEvaluations) {
        this.maxEvaluations = maxEvaluations;
    }

    public Boolean getCrossover() {
        return crossover;
    }

    public void setCrossover(Boolean crossover) {
        this.crossover = crossover;
    }

    public Double getCrossoverProbability() {
        return crossoverProbability;
    }

    public void setCrossoverProbability(Double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    public Boolean getInteractive() {
        return interactive;
    }

    public void setInteractive(Boolean interactive) {
        this.interactive = interactive;
    }

    public Integer getMaxInteractions() {
        return maxInteractions;
    }

    public void setMaxInteractions(Integer maxInteractions) {
        this.maxInteractions = maxInteractions;
    }

    public ClusteringAlgorithm getClusteringAlgorithm() {
        return clusteringAlgorithm;
    }

    public void setClusteringAlgorithm(ClusteringAlgorithm clusteringAlgorithm) {
        this.clusteringAlgorithm = clusteringAlgorithm;
    }

    public Moment getClusteringMoment() {
        return clusteringMoment;
    }

    public void setClusteringMoment(Moment clusteringMoment) {
        this.clusteringMoment = clusteringMoment;
    }

    public InteractiveFunction getInteractiveFunction() {
        return interactiveFunction;
    }

    public void setInteractiveFunction(InteractiveFunction interactiveFunction) {
        this.interactiveFunction = interactiveFunction;
    }

    public List<String> getMutationOperators() {
        return mutationOperators;
    }

    public void setMutationOperators(List<String> mutationOperators) {
        this.mutationOperators = mutationOperators;
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<String> patterns) {
        this.patterns = patterns;
    }

    public List<String> getObjectiveFunctions() {
        return objectiveFunctions;
    }

    public void setObjectiveFunctions(List<String> objectiveFunctions) {
        this.objectiveFunctions = objectiveFunctions;
    }
}
