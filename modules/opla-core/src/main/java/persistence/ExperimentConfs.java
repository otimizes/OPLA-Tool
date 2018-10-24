package persistence;

import database.Database;
import exceptions.MissingConfigurationException;
import jmetal4.experiments.ExperimentCommomConfigs;
import jmetal4.experiments.NSGAIIConfig;
import jmetal4.experiments.PaesConfigs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * Classe responsável por guardar E persistir/recuperar informações referentes a
 * qual configuração o experimento utilizou.
 * <p>
 * Ex: numero de rodas, funcoes objetivos utilizadas e assim por diante.
 *
 * @author elf
 */
public class ExperimentConfs {

    private String experimentId;
    private ExperimentCommomConfigs configs;
    private String algorithm;

    public ExperimentConfs(String experimentId, String algorithm, ExperimentCommomConfigs configs) {
        this.experimentId = experimentId;
        this.configs = configs;
        this.algorithm = algorithm;
    }

    /**
     * Return a {@link HashMap<String, String>} with configuration settings
     * given a experimentId.
     *
     * @param experimentId
     * @return {@link HashMap<String, String>}
     * @throws Exception
     */
    public static HashMap<String, String> getConfigs(String experimentId) throws Exception {

        HashMap<String, String> confs = new HashMap<>();

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM experiment_configurations where experiment_id = ");
        query.append(experimentId);

        try {
            Statement stat = Database.getConnection().createStatement();

            ResultSet result = stat.executeQuery(query.toString());
            while (result.next()) {
                confs.put("numberOfRuns", result.getString("number_of_runs"));
                confs.put("maxEvaluations", result.getString("max_evaluations"));
                confs.put("crossoverProbability", getProbability(result.getString("crossover_prob")));
                confs.put("mutationProbability", getProbability(result.getString("mutation_prob")));
                confs.put("populationSize", getInt(result.getString("population_size")));
                confs.put("mutationOperators", getMutationsOperators(result.getString("mutation_operators")));
                confs.put("patterns", result.getString("patterns"));
                confs.put("pattern_strategy", getString(result.getString("pattern_strategy")));
                confs.put("algorithm", result.getString("algorithm"));
                confs.put("archiveSize", getInt(result.getString("archive_size")));
                stat.close();
                confs.put("objective_functions", getObjectiveFunctionsForExperiment(experimentId));

            }

        } catch (SQLException | ClassNotFoundException | MissingConfigurationException e) {
            e.printStackTrace();
        }

        return confs;
    }

    private static String getString(String str) {
        return (str == null || str.isEmpty()) ? "-" : str;
    }

    private static String getMutationsOperators(String mutationOperators) {
        if (mutationOperators == null || mutationOperators.isEmpty())
            return "";

        return mutationOperators;
    }

    private static String getProbability(String probability) {
        return Double.valueOf(probability) == 0 ? "-" : probability;
    }

    private static String getInt(String archiveSize) {
        return Integer.valueOf(archiveSize) == 0 ? "-" : archiveSize;
    }

    private static String getObjectiveFunctionsForExperiment(String experimentId) throws Exception {
        Statement stat = Database.getConnection().createStatement();

        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM map_objectives_names where experiment_id = ");
        query.append(experimentId);

        ResultSet result = stat.executeQuery(query.toString());
        String funcs = result.getString("names");
        stat.close();
        return funcs;
    }

    public void save() throws Exception {
        persist();
    }

    private void persist() throws Exception {
        try {
            Statement stat = Database.getConnection().createStatement();

            StringBuilder query = new StringBuilder();
            StringBuilder patternsList = new StringBuilder();
            StringBuilder mutationOperatorsList = new StringBuilder();

            if (configs.getMutationOperators().contains("DesignPatterns")) {
                for (String p : configs.getPatterns()) {
                    patternsList.append(p);
                    patternsList.append(",");
                }
            }

            for (String operator : configs.getMutationOperators()) {
                mutationOperatorsList.append(operator);
                mutationOperatorsList.append(",");
            }

            if (patternsList.length() > 0)
                patternsList = removeLastComma(patternsList);
            if (mutationOperatorsList.length() > 0)
                mutationOperatorsList = removeLastComma(mutationOperatorsList);

            query.append("INSERT into experiment_configurations (experiment_id, number_of_runs,"
                    + " max_evaluations, crossover_prob, mutation_prob, patterns, pattern_strategy, algorithm, mutation_operators, archive_size, population_size) VALUES (");
            query.append(experimentId);
            query.append(",");
            query.append(configs.getNumberOfRuns());
            query.append(",");
            query.append(configs.getMaxEvaluation());
            query.append(",");
            query.append(configs.getCrossoverProbability());
            query.append(",");
            query.append(configs.getMutationProbability());
            query.append(",");
            query.append("'");
            query.append(patternsList);
            query.append("'");
            query.append(",");
            query.append("'");
            query.append(getDesignPatternStrategy());
            query.append("'");
            query.append(",");
            query.append("'");
            query.append(algorithm);
            query.append("'");
            query.append(",");
            query.append("'");
            query.append(mutationOperatorsList);
            query.append("'");
            query.append(",");
            query.append(getArchiveSize());
            query.append(",");
            query.append(getPopulationSize());
            query.append(")");

            stat.execute(query.toString());
            stat.close();
        } catch (SQLException | ClassNotFoundException | MissingConfigurationException e) {
            e.printStackTrace();
        }
    }

    private Object getPopulationSize() {
        if (this.algorithm.equalsIgnoreCase("NSGAII"))
            return ((NSGAIIConfig) this.configs).getPopulationSize();

        return 0;
    }

    /**
     * For PAES
     *
     * @return
     */
    private int getArchiveSize() {
        if (this.algorithm.equalsIgnoreCase("paes"))
            return ((PaesConfigs) this.configs).getArchiveSize();

        return 0;
    }

    private StringBuilder removeLastComma(StringBuilder list) {
        list.delete(list.length() - 1, list.length());
        return list;
    }

    private String getDesignPatternStrategy() {
        if (configs.getMutationOperators().contains("DesignPatterns")) {
            if (configs.getDesignPatternStrategy() == null)
                return "Random";
            if (configs.getDesignPatternStrategy() != null)
                return "Elements With Same Design Pattern or None";
        }
        return "";
    }

}
