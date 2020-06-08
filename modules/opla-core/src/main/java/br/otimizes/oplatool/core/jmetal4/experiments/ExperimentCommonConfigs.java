package br.otimizes.oplatool.core.jmetal4.experiments;

import br.otimizes.oplatool.core.jmetal4.interactive.InteractiveFunction;
import br.otimizes.oplatool.core.jmetal4.operators.MutationOperators;
import br.otimizes.oplatool.core.learning.ClusteringAlgorithm;
import br.otimizes.oplatool.core.learning.Moment;
import br.otimizes.oplatool.architecture.builders.ArchitectureBuilders;
import br.ufpr.dinf.gres.loglog.LogLog;
import br.otimizes.oplatool.patterns.strategies.scopeselection.impl.ElementsWithSameDesignPatternSelection;
import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ExperimentCommonConfigs {

    private boolean log = false;
    private LogLog logger;
    private String pathToDb;
    private int numberOfRuns;
    private int maxEvaluations;
    private double crossoverProbability;
    private double mutationProbability;
    private String plas;
    private OPLAConfigs oplaConfigs;
    private String[] patterns = null;
    private String description;
    private InteractiveFunction interactiveFunction;
    private Boolean interactive;
    private int maxInteractions;
    private int firstInteraction;
    private int intervalInteraction;
    private ArchitectureBuilders architectureBuilder = ArchitectureBuilders.SMARTY;
    private Moment clusteringMoment;
    private ClusteringAlgorithm clusteringAlgorithm;
    private List<String> mutationOperators = new ArrayList<>();
    private List<String> crossoverOperators = new ArrayList<>();
    private ElementsWithSameDesignPatternSelection applyStrategy;

    public void activeLogs() {
        log = true;
    }

    public String getPlas() {
        return this.plas;
    }

    public void setPlas(String plas) {
        this.plas = plas;
    }

    public int getNumberOfRuns() {
        return numberOfRuns;
    }

    public void setNumberOfRuns(int numberOfRuns) {
        validateGreaterOrEqualOne("numberOfRuns", numberOfRuns);
        this.numberOfRuns = numberOfRuns;
    }

    public int getMaxEvaluation() {
        return maxEvaluations;
    }

    public void setMaxEvaluations(int maxEvaluations) {
        validateGreaterOrEqualOne("maxEvaluation", maxEvaluations);
        this.maxEvaluations = maxEvaluations;
    }

    protected void validateArgument(String arg, double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException(arg + " must be a value between 0 and 1");
        }
    }

    public double getCrossoverProbability() {
        return this.crossoverProbability;
    }

    public void setCrossoverProbability(double crossoverProbability) {
        validateArgument("crossoverProbability", crossoverProbability);
        this.crossoverProbability = crossoverProbability;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(double mutationProbability) {
        validateArgument("mutationProbability", mutationProbability);
        this.mutationProbability = mutationProbability;
    }

    public void disableCrossover() {
        this.crossoverProbability = 0;
    }

    public void disableMutation() {
        this.mutationProbability = 0;
    }

    public List<String> getMutationOperators() {
        return mutationOperators;
    }

    public void setMutationOperators(List<String> mutationOperators) {
        this.mutationOperators = mutationOperators;
    }

    public OPLAConfigs getOplaConfigs() {
        return oplaConfigs;
    }

    public void setOplaConfigs(OPLAConfigs oplaConfigs) {
        this.oplaConfigs = oplaConfigs;
    }

    /**
     * Get Design Patterns to apply. If none is set return all.
     *
     * @return String[]
     */
    public String[] getPatterns() {
        if (patterns == null) {
            return new String[]{"Strategy", "Bridge", "Mediator"};
        }
        return patterns;
    }

    public void setPatterns(String... patternsList) {
        for (int i = 0; i < patternsList.length; i++) {
            patternsList[i] = WordUtils.capitalize(patternsList[i].toLowerCase());
        }
        this.patterns = patternsList;

        if (Collections.disjoint(Arrays.asList(this.patterns), Arrays.asList("Strategy", "Bridge", "Mediator"))) {
            throw new IllegalArgumentException("Invalid(s) Design Pattern(s). Valids are: Stragety, Bridge and Mediator");
        }
    }

    public ElementsWithSameDesignPatternSelection getDesignPatternStrategy() {
        return applyStrategy;
    }

    public void setDesignPatternStrategy(ElementsWithSameDesignPatternSelection elementsWithSameDesignPatternSelection) {
        this.applyStrategy = elementsWithSameDesignPatternSelection;
    }

    public boolean isLog() {
        return log && logger != null;
    }

    public String getPathToDb() {
        return pathToDb;
    }

    public void setPathToDb(String pathToDb) {
        this.pathToDb = pathToDb;
    }

    public void excludeDesignPatternsFromMutationOperatorList() {
        this.getMutationOperators().remove(MutationOperators.DESIGN_PATTERNS.toString());
    }

    protected void validateGreaterOrEqualOne(String arg, int numberOfRuns) {
        if (numberOfRuns < 1) {
            throw new IllegalArgumentException(arg + " must be greater or equal 1");
        }
    }

    public LogLog getLogger() {
        return logger;
    }

    public void setLogger(LogLog logLog) {
        this.logger = logLog;
    }

    public String getDescription() {
        return description;
    }

    public int getIntervalInteraction() {
        return intervalInteraction;
    }

    public void setIntervalInteraction(Integer intervalInteraction) {
        this.intervalInteraction = intervalInteraction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getObjectiveFuncions() {
        return oplaConfigs.getSelectedObjectiveFunctions();
    }

    public Boolean getInteractive() {
        return interactive != null && interactive;
    }

    public void setInteractive(Boolean interactive) {
        this.interactive = interactive;
    }

    public InteractiveFunction getInteractiveFunction() {
        return interactiveFunction;
    }

    public void setInteractiveFunction(InteractiveFunction interactiveFunction) {
        this.interactiveFunction = interactiveFunction;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public int getMaxEvaluations() {
        return maxEvaluations;
    }

    public int getMaxInteractions() {
        return maxInteractions;
    }

    public void setMaxInteractions(int maxInteractions) {
        this.maxInteractions = maxInteractions;
    }

    public int getFirstInteraction() {
        return firstInteraction;
    }

    public void setFirstInteraction(int firstInteraction) {
        this.firstInteraction = firstInteraction;
    }

    public ElementsWithSameDesignPatternSelection getApplyStrategy() {
        return applyStrategy;
    }

    public void setApplyStrategy(ElementsWithSameDesignPatternSelection applyStrategy) {
        this.applyStrategy = applyStrategy;
    }

    public ClusteringAlgorithm getClusteringAlgorithm() {
        return clusteringAlgorithm != null ? clusteringAlgorithm : ClusteringAlgorithm.KMEANS;
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

    public void setIntervalInteraction(int intervalInteraction) {
        this.intervalInteraction = intervalInteraction;
    }
}
