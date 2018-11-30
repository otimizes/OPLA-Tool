package jmetal4.experiments;

import br.ufpr.dinf.gres.loglog.LogLog;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.impl.ElementsWithSameDesignPatternSelection;
import jmetal4.interactive.InteractiveFunction;
import learning.ClusteringAlgorithm;
import learning.Moment;
import org.apache.commons.lang.WordUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ExperimentCommomConfigs {

    private boolean log = false;
    private LogLog logger; //Precisa para captar os logs na GUI.
    private String pathToDb;
    private int numberOfRuns;
    private int maxEvaluations;
    private double crossoverProbability;
    private double mutationProbability;
    private String plas;
    private OPLAConfigs oplaConfigs;
    private String[] patterns = null; //OPLA-Patterns....
    private String description;
    private InteractiveFunction interactiveFunction;
    private Boolean interactive;
    public int maxInteractions;
    public Moment clusteringMoment;
    public ClusteringAlgorithm clusteringAlgorithm;

    private List<String> mutationOperators = new ArrayList<String>();
    private ElementsWithSameDesignPatternSelection applyStrategy;

    public void activeLogs() {
        log = true;
    }

    public String getPlas() {
        return this.plas;
    }

    /**
     * PLAs - Path to PLAs separated by comma.
     *
     * @param plas
     */
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

    /**
     * Set patterns to use.
     * <p>
     * This method will normalize patternsList to downcase and capitalized.
     *
     * @param patternsList
     */
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

    /**
     * Set the strategy to apply Design Patterns. Two possibilities:<br/>
     * ElementsWithSameDesignPatternSelection or null for Random
     *
     * @param elementsWithSameDesignPatternSelection
     */
    public void setDesignPatternStrategy(ElementsWithSameDesignPatternSelection elementsWithSameDesignPatternSelection) {
        this.applyStrategy = elementsWithSameDesignPatternSelection;
    }

    /**
     * If true execute log method NSGAII_OPLA_FeatMut.logInforamtions() or
     * PAES_OPLA_FeatMut.logInforamtions();
     *
     * @return
     */
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
        this.getMutationOperators().remove("DesignPatterns");
    }

    protected void validateGreaterOrEqualOne(String arg, int numberOfRuns) {
        if (numberOfRuns < 1) {
            throw new IllegalArgumentException(arg + " must be greater or equal 1");
        }
    }

    public LogLog getLogger() {
        return logger;
    }

    /**
     * Seta uma instância de {@link LogLog}.
     *
     * @param logLog
     */
    public void setLogger(LogLog logLog) {
        this.logger = logLog;
    }

    public String getDescription() {
        return description;
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
}
