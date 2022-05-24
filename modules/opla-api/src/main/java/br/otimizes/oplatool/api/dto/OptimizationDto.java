package br.otimizes.oplatool.api.dto;

import br.otimizes.oplatool.api.gateway.OptimizationAlgorithms;
import br.otimizes.oplatool.architecture.builders.ArchitectureBuilders;
import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveFunction;
import br.otimizes.oplatool.core.learning.ClusteringAlgorithm;
import br.otimizes.oplatool.core.learning.MachineLearningAlgorithm;
import br.otimizes.oplatool.core.learning.Moment;
import br.otimizes.oplatool.domain.config.ApplicationFileConfig;
import br.otimizes.oplatool.domain.config.ApplicationYamlConfig;
import br.otimizes.oplatool.patterns.strategies.ScopeSelection;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptimizationDto {
    private OptimizationAlgorithms algorithm = OptimizationAlgorithms.NSGAII;
    private String description = "teste";
    private Boolean mutation = true;
    private Double mutationProbability = 0.9;
    private String inputArchitecture = "";
    private Integer numberRuns = 1;
    private Integer populationSize = 20;
    private Integer maxEvaluations = 300;
    private Boolean crossover = false;
    private Double crossoverProbability = 0.0;
    private Boolean interactive = false;
    private Integer maxInteractions = 0;
    private Integer firstInteraction = 0;
    private Integer intervalInteraction = 0;
    private Integer archiveSize = 0;
    private ClusteringAlgorithm clusteringAlgorithm = ClusteringAlgorithm.KMEANS;
    private Moment clusteringMoment = Moment.INTERACTIVE;
    private ArchitectureBuilders architectureBuilder = ArchitectureBuilders.SMARTY;
    @JsonIgnore
    private InteractiveFunction interactiveFunction;
    private List<String> mutationOperators = Arrays.asList("FEATURE_DRIVEN_OPERATOR", "MOVE_METHOD_MUTATION", "MOVE_ATTRIBUTE_MUTATION", "MOVE_OPERATION_MUTATION", "ADD_CLASS_MUTATION", "ADD_MANAGER_CLASS_MUTATION");
    private List<String> crossoverOperators = new ArrayList<>();
    private List<String> patterns = new ArrayList<>();
    private ScopeSelection scopeSelection = ScopeSelection.RANDOM;
    private List<String> objectiveFunctions = new ArrayList<>();
    public ApplicationYamlConfig config = ApplicationFileConfig.getInstance();
    private MachineLearningAlgorithm machineLearningAlgorithm = MachineLearningAlgorithm.MLP;

    public MachineLearningAlgorithm getMachineLearningAlgorithm() {
        return machineLearningAlgorithm;
    }

    public void setMachineLearningAlgorithm(MachineLearningAlgorithm machineLearningAlgorithm) {
        this.machineLearningAlgorithm = machineLearningAlgorithm;
    }

    public OptimizationDto() {
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

    public Integer getFirstInteraction() {
        return firstInteraction;
    }

    public void setFirstInteraction(Integer firstInteraction) {
        this.firstInteraction = firstInteraction;
    }

    public Integer getIntervalInteraction() {
        return intervalInteraction;
    }

    public void setIntervalInteraction(Integer intervalInteraction) {
        this.intervalInteraction = intervalInteraction;
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

    public OptimizationAlgorithms getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(OptimizationAlgorithms algorithm) {
        this.algorithm = algorithm;
    }

    public ApplicationYamlConfig getConfig() {
        return config;
    }

    public void setConfig(ApplicationYamlConfig config) {
        this.config = config;
    }

    public Integer getArchiveSize() {
        return archiveSize;
    }

    public void setArchiveSize(Integer archiveSize) {
        this.archiveSize = archiveSize;
    }

    public ScopeSelection getScopeSelection() {
        return scopeSelection;
    }

    public void setScopeSelection(ScopeSelection scopeSelection) {
        this.scopeSelection = scopeSelection;
    }

    public ArchitectureBuilders getArchitectureBuilder() {
        return architectureBuilder;
    }

    public void setArchitectureBuilder(ArchitectureBuilders architectureBuilder) {
        this.architectureBuilder = architectureBuilder;
    }

    public List<String> getCrossoverOperators() {
        return crossoverOperators;
    }

    public void setCrossoverOperators(List<String> crossoverOperators) {
        this.crossoverOperators = crossoverOperators;
    }
}
