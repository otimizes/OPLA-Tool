/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.br.opla.algorithms;

import arquitetura.io.ReaderConfig;
import com.ufpr.br.opla.configuration.UserHome;
import com.ufpr.br.opla.configuration.VolatileConfs;
import com.ufpr.br.opla.utils.MutationOperatorsSelected;
import jmetal4.experiments.FeatureMutationOperators;
import jmetal4.experiments.OPLAConfigs;
import jmetal4.experiments.PAES_OPLA_FeatMutInitializer;
import jmetal4.experiments.PaesConfigs;

import javax.swing.*;
import java.util.List;

/**
 * @author elf
 */
public class PAES {


    public void execute(JComboBox comboAlgorithms, JCheckBox checkMutation, JTextField fieldMutationProb,
                        JTextArea fieldArchitectureInput, JTextField fieldNumberOfRuns, JTextField fieldPaesArchiveSize,
                        JTextField fieldMaxEvaluations, JCheckBox checkCrossover, JTextField fieldCrossoverProbability, String executionDescription) {

        execute(executionDescription, checkMutation.isSelected(), Double.parseDouble(fieldMutationProb.getText()) / 10,
                fieldArchitectureInput.getText(), Integer.parseInt(fieldNumberOfRuns.getText()), Integer.parseInt(fieldMaxEvaluations.getText()),
                Integer.parseInt(fieldPaesArchiveSize.getText()), checkCrossover.isSelected(), Double.parseDouble(fieldCrossoverProbability.getText()) / 10);

    }


    public void execute(JComboBox<String> cbAlgothm, JCheckBox ckMutation, JSlider jsMutation, JTextField tfInputArchitecturePath,
                        JTextField tfNumberRuns, JTextField tfPopulationSize, JTextField tfMaxEvaluations, JCheckBox ckCrossover, JSlider jsCrossover, JTextField tfDescription, JTextField tfArchiveSize) {
        execute(tfDescription.getText(), ckMutation.isSelected(), (double) jsMutation.getValue() / 10,
                tfInputArchitecturePath.getText(), Integer.parseInt(tfNumberRuns.getText()), Integer.parseInt(tfMaxEvaluations.getText()),
                Integer.parseInt(tfArchiveSize.getText()), ckCrossover.isSelected(), (double) (jsCrossover.getValue() / 10));
    }

    public void execute(String description, Boolean mutation, Double mutationProbability, String inputArchitecture, Integer numberRuns, Integer maxEvaluations, Integer archiveSize, Boolean crossover, Double crossoverProbability) {
        ReaderConfig.setPathToConfigurationFile(UserHome.getPathToConfigFile());
        ReaderConfig.load();

        PaesConfigs configs = new PaesConfigs();
        configs.setDescription(description);

        //Se mutação estiver marcada, pega os operadores selecionados
        //,e seta a probabilidade de mutacao
        if (mutation) {
            List<String> mutationsOperators = MutationOperatorsSelected.getSelectedMutationOperators();
            configs.setMutationOperators(mutationsOperators);
            configs.setMutationProbability(mutationProbability);
        }

        configs.setPlas(inputArchitecture);
        configs.setNumberOfRuns(numberRuns);
        configs.setMaxEvaluations(maxEvaluations);
        configs.setArchiveSize(archiveSize);


        //Se crossover estiver marcado, configura probabilidade
        //Caso contrario desativa
        if (crossover) {
            configs.setCrossoverProbability(crossoverProbability);
        } else {
            configs.disableCrossover();
        }

        //OPA-Patterns Configurations
        if (MutationOperatorsSelected.getSelectedMutationOperators().contains(FeatureMutationOperators.DESIGN_PATTERNS.getOperatorName())) {
            String[] array = new String[MutationOperatorsSelected.getSelectedPatternsToApply().size()];
            configs.setPatterns(MutationOperatorsSelected.getSelectedPatternsToApply().toArray(array));
            configs.setDesignPatternStrategy(VolatileConfs.getScopePatterns());
        }

        //Configura onde o db esta localizado
        configs.setPathToDb(UserHome.getPathToDb());

        //Instancia a classe de configuracao da OPLA.java
        OPLAConfigs oplaConfig = new OPLAConfigs();


        oplaConfig.setSelectedObjectiveFunctions(VolatileConfs.getObjectiveFunctionSelected());

        //Add as confs de OPLA na classe de configuracoes gerais.
        configs.setOplaConfigs(oplaConfig);

        //Utiliza a classe Initializer do NSGAII passando as configs.
        PAES_OPLA_FeatMutInitializer paes = new PAES_OPLA_FeatMutInitializer(configs);

        //Executa
        paes.run();
    }
}
