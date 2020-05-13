package br.ufpr.dinf.gres.core.persistence;

import br.ufpr.dinf.gres.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.ufpr.dinf.gres.core.jmetal4.experiments.base.NSGAIIConfigs;
import br.ufpr.dinf.gres.core.jmetal4.experiments.base.PAESConfigs;
import br.ufpr.dinf.gres.core.jmetal4.operators.FeatureMutationOperators;

/**
 * Class responsible for retrieving information related of which configuration the experiment use.
 * <p>
 * Ex: number of wheels, objective functions used and so on.
 *
 * @author elf
 */
public class ExperimentConfs {

    private String experimentId;
    private ExperimentCommonConfigs configs;
    private String algorithm;

    public ExperimentConfs(String experimentId, String algorithm, ExperimentCommonConfigs configs) {
        this.experimentId = experimentId;
        this.configs = configs;
        this.algorithm = algorithm;
    }

    private static String getString(String str) {
        return (str == null || str.isEmpty()) ? "-" : str;
    }

    private static String getMutationsOperators(String mutationOperators) {
        if (mutationOperators == null || mutationOperators.isEmpty())
            return "";

        return mutationOperators;
    }

    public static String getProbability(String probability) {
        return Double.valueOf(probability) == 0 ? "-" : probability;
    }

    /**
     * Get string of patterns
     *
     * @return string of patterns
     */
    public String getPatterns() {
        StringBuilder patternsList = new StringBuilder();

        if (configs.getMutationOperators().contains(FeatureMutationOperators.DESIGN_PATTERNS.toString())) {
            for (String p : configs.getPatterns()) {
                patternsList.append(p);
                patternsList.append(",");
            }
        }

        if (patternsList.length() > 0)
            patternsList = removeLastComma(patternsList);

        return patternsList.toString();
    }

    /**
     * Get mutation operators
     *
     * @return mutation operators
     */
    public String getMutationOperators() {
        StringBuilder mutationOperatorsList = new StringBuilder();
        for (String operator : configs.getMutationOperators()) {
            mutationOperatorsList.append(operator);
            mutationOperatorsList.append(",");
        }
        if (mutationOperatorsList.length() > 0)
            mutationOperatorsList = removeLastComma(mutationOperatorsList);
        return mutationOperatorsList.toString();
    }

    public static String getInt(String archiveSize) {
        return Integer.valueOf(archiveSize) == 0 ? "-" : archiveSize;
    }


    /**
     * For NSGA-II
     *
     * @return population size
     */
    public int getPopulationSize() {
        if (this.algorithm.equalsIgnoreCase("NSGAII"))
            return ((NSGAIIConfigs) this.configs).getPopulationSize();

        return 0;
    }

    public String getObjectives() {
        return "'" + this.configs.getObjectiveFuncions() + "'";
    }

    /**
     * For PAES
     *
     * @return archive size
     */
    public int getArchiveSize() {
        if (this.algorithm.equalsIgnoreCase("paes"))
            return ((PAESConfigs) this.configs).getArchiveSize();

        return 0;
    }

    /**
     * Remove last comma
     *
     * @param builder string builder
     * @return string build without last comma
     */
    public StringBuilder removeLastComma(StringBuilder builder) {
        builder.delete(builder.length() - 1, builder.length());
        return builder;
    }

    /**
     * Get design pattern strategy
     *
     * @return string of strategy
     */
    public String getDesignPatternStrategy() {
        if (configs.getMutationOperators().contains(FeatureMutationOperators.DESIGN_PATTERNS.toString())) {
            if (configs.getDesignPatternStrategy() == null)
                return "Random";
            if (configs.getDesignPatternStrategy() != null)
                return "Elements With Same Design Pattern or None";
        }
        return "";
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public ExperimentCommonConfigs getConfigs() {
        return configs;
    }

    public void setConfigs(ExperimentCommonConfigs configs) {
        this.configs = configs;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
