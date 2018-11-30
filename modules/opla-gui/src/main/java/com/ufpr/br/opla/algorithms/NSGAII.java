/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.br.opla.algorithms;

import arquitetura.io.ReaderConfig;
import br.ufpr.dinf.gres.loglog.Logger;
import com.ufpr.br.opla.configuration.UserHome;
import com.ufpr.br.opla.configuration.VolatileConfs;
import com.ufpr.br.opla.utils.MutationOperatorsSelected;
import domain.AlgorithmExperiment;
import jmetal4.experiments.FeatureMutationOperators;
import jmetal4.experiments.NSGAIIConfig;
import jmetal4.experiments.NSGAII_OPLA_FeatMutInitializer;
import jmetal4.experiments.OPLAConfigs;
import jmetal4.interactive.InteractiveFunction;
import learning.ClusteringAlgorithm;
import learning.Moment;

import javax.swing.*;
import java.util.List;

/**
 * @author elf
 */
public class NSGAII {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(NSGAII.class);

    @Deprecated
    public void execute(JComboBox comboAlgorithms, JCheckBox checkMutation, JTextField fieldMutationProb,
                        JTextArea fieldArchitectureInput, JTextField fieldNumberOfRuns, JTextField fieldPopulationSize,
                        JTextField fieldMaxEvaluations, JCheckBox checkCrossover, JTextField fieldCrossoverProbability,
                        String executionDescription) {
        execute(executionDescription, checkMutation.isSelected(), Double.parseDouble(fieldMutationProb.getText()),
                fieldArchitectureInput.getText(), Integer.parseInt(fieldNumberOfRuns.getText()), Integer.parseInt(fieldPopulationSize.getText()),
                Integer.parseInt(fieldMaxEvaluations.getText()), checkCrossover.isSelected(), Double.parseDouble(fieldCrossoverProbability.getText()), false, null, null, null, null);

    }

    public void execute(JComboBox<String> cbAlgothm, JCheckBox ckMutation, JSlider jsMutation, JTextField inputArchitecture, JTextField tfNumberRuns,
                        JTextField tfPopulationSize, JTextField tfMaxEvaluations, JCheckBox ckCrossover, JSlider jsCrossover, JTextField tfDescription,
                        JCheckBox ckEnableInteraction, JTextField tfMaxInteractions, JComboBox<String> clusteringAlgorithm, JComboBox<String> clusteringMoment, InteractiveFunction interactiveFunction) {
        execute(tfDescription.getText(), ckMutation.isSelected(), (double) jsMutation.getValue() / 10,
                inputArchitecture.getText(), Integer.parseInt(tfNumberRuns.getText()), Integer.parseInt(tfPopulationSize.getText()),
                Integer.parseInt(tfMaxEvaluations.getText()), ckCrossover.isSelected(), (double) (jsCrossover.getValue() / 10), ckEnableInteraction.isSelected(),
                Integer.parseInt(tfMaxInteractions.getText()), clusteringAlgorithm.getSelectedItem() != null ? ClusteringAlgorithm.valueOf(clusteringAlgorithm.getSelectedItem().toString()) : ClusteringAlgorithm.KMEANS, Moment.valueOf(clusteringMoment.getSelectedItem().toString()), interactiveFunction);
    }

    public void execute(AlgorithmExperiment algorithmExperiment) {
        execute(algorithmExperiment.getDescription(), algorithmExperiment.getMutation(),
                algorithmExperiment.getMutationProbability(), algorithmExperiment.getInputArchitecture(), algorithmExperiment.getNumberRuns(),
                algorithmExperiment.getPopulationSize(), algorithmExperiment.getMaxEvaluations(), algorithmExperiment.getCrossover(),
                algorithmExperiment.getCrossoverProbability(), algorithmExperiment.getInteractive(), algorithmExperiment.getMaxInteractions(),
                algorithmExperiment.getClusteringAlgorithm(), algorithmExperiment.getClusteringMoment(), algorithmExperiment.getInteractiveFunction());
    }

    public void execute(String description, Boolean mutation, Double mutationProbability, String inputArchitecture, Integer numberRuns,
                        Integer populationSize, Integer maxEvaluations, Boolean crossover, Double crossoverProbability, Boolean interactive, Integer maxInteractions, ClusteringAlgorithm clusteringAlgorithm, Moment clusteringMoment, InteractiveFunction interactiveFunction) {
        try {

            LOGGER.info("set configuration path");
            ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
            ReaderConfig.load();

            LOGGER.info("Create NSGA Config");
            NSGAIIConfig configs = new NSGAIIConfig();

            configs.setLogger(Logger.getLogger());
            configs.activeLogs();
            configs.setDescription(description);
            configs.setInteractive(interactive);
            configs.setInteractiveFunction(interactiveFunction);
            configs.setMaxInteractions(maxInteractions);
            configs.setClusteringMoment(clusteringMoment);
            configs.setClusteringAlgorithm(clusteringAlgorithm);

            // Se mutação estiver marcada, pega os operadores selecionados ,e seta a probabilidade de mutacao
            if (mutation) {
                LOGGER.info("Configure Mutation Operator");
                List<String> mutationsOperators = MutationOperatorsSelected.getSelectedMutationOperators();
                configs.setMutationOperators(mutationsOperators);
                configs.setMutationProbability(mutationProbability);
            }

            configs.setPlas(inputArchitecture);
            configs.setNumberOfRuns(numberRuns);
            configs.setPopulationSize(populationSize);
            configs.setMaxEvaluations(maxEvaluations);

            // Se crossover estiver marcado, configura probabilidade
            // Caso contrario desativa
            if (crossover) {
                LOGGER.info("Configure Crossover Probability");
                configs.setCrossoverProbability(crossoverProbability);
            } else {
                configs.disableCrossover();
            }

            // OPA-Patterns Configurations
            if (MutationOperatorsSelected.getSelectedMutationOperators().contains(FeatureMutationOperators.DESIGN_PATTERNS.getOperatorName())) {
                // joao
                LOGGER.info("Instanciando o campo do Patterns - oplatool classe nsgaii");
                String[] array = new String[MutationOperatorsSelected.getSelectedPatternsToApply().size()];
                configs.setPatterns(MutationOperatorsSelected.getSelectedPatternsToApply().toArray(array));
                configs.setDesignPatternStrategy(VolatileConfs.getScopePatterns());

            }

            List<String> operadores = configs.getMutationOperators();

            for (int i = 0; i < operadores.size(); i++) {
                if (operadores.get(i) == "DesignPatterns") {
                    operadores.remove(i);
                }
            }
            configs.setMutationOperators(operadores);
            // operadores convencionais ok
            // operadores padroes ok
            // String[] padroes = configs.getPatterns();

            // Configura onde o db esta localizado
            configs.setPathToDb(UserHome.getPathToDb());

            // Instancia a classe de configuracao da OPLA.java
            LOGGER.info("Create OPLA Config");
            OPLAConfigs oplaConfig = new OPLAConfigs();

            // Funcoes Objetivo
            oplaConfig.setSelectedObjectiveFunctions(VolatileConfs.getObjectiveFunctionSelected());

            // Add as confs de OPLA na classe de configuracoes gerais.
            configs.setOplaConfigs(oplaConfig);

            // Utiliza a classe Initializer do NSGAII passando as configs.
            NSGAII_OPLA_FeatMutInitializer nsgaii = new NSGAII_OPLA_FeatMutInitializer(configs);

            // Executa
            LOGGER.info("Execução NSGAII");
            nsgaii.run();
            LOGGER.info("Fim Execução NSGAII");

        } catch (Exception e) {
            LOGGER.error(e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}