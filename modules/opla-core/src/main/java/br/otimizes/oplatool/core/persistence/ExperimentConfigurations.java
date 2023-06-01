package br.otimizes.oplatool.core.persistence;

import br.otimizes.oplatool.core.jmetal4.experiments.ExperimentCommonConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIIIConfigs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.NSGAIII_jm6Configs;
import br.otimizes.oplatool.core.jmetal4.experiments.base.PAESConfigs;
import br.otimizes.oplatool.core.jmetal4.operators.MutationOperators;

/**
 * Class responsible for retrieving information related of which configuration the experiment use.
 * <p>
 * Ex: number of wheels, objective functions used and so on.
 *
 * @author elf
 */
public class ExperimentConfigurations {

    private String experimentId;
    private ExperimentCommonConfigs configs;
    private String algorithm;

    public ExperimentConfigurations(String experimentId, String algorithm, ExperimentCommonConfigs configs) {
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
        return Double.parseDouble(probability) == 0 ? "-" : probability;
    }

    /**
     * Get string of patterns
     *
     * @return string of patterns
     */
    public String getPatterns() {
        StringBuilder patternsList = new StringBuilder();

        if (configs.getMutationOperators().contains(MutationOperators.DESIGN_PATTERNS.toString())) {
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
        return Integer.parseInt(archiveSize) == 0 ? "-" : archiveSize;
    }


    /**
     * For NSGA-II and NSGA-III
     *
     * @return population size
     */
    public int getPopulationSize() {
        if (this.algorithm.equalsIgnoreCase("NSGAII"))
            return ((NSGAIIConfigs) this.configs).getPopulationSize();
        else if (this.algorithm.equalsIgnoreCase("NSGAIII"))
            return ((NSGAIIIConfigs) this.configs).getPopulationSize();
        else if (this.algorithm.equalsIgnoreCase("NSGAIII_jm6"))
            return ((NSGAIII_jm6Configs) this.configs).getPopulationSize();

        return 0;
    }

    public String getObjectives() {
        return "'" + this.configs.getObjectiveFunctions() + "'";
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
        if (configs.getMutationOperators().contains(MutationOperators.DESIGN_PATTERNS.toString())) {
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
