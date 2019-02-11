package com.ufpr.br.opla.gui;

import arquitetura.io.FileUtils;
import com.ufpr.br.opla.configuration.ManagerApplicationConfig;
import com.ufpr.br.opla.configuration.UserHome;
import com.ufpr.br.opla.utils.OsUtils;
import com.ufpr.br.opla.utils.Utils;

import javax.swing.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiServices {

    private static final String PROFILES_DIR = "profiles/";

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private final ManagerApplicationConfig config;
    private final String profileSmartyName;
    private final String profileConcernsName;
    private final String profilePatternName;
    private final String profileRelationshipName;

    public GuiServices(ManagerApplicationConfig managerConfig) {
        config = managerConfig;

        profileSmartyName = "smarty.profile.uml";
        profileConcernsName = "concerns.profile.uml";
        profilePatternName = "patterns.profile.uml";
        profileRelationshipName = "relationships.profile.uml";
    }

    /**
     * Initialize comboObjectiveFunctions with function of given exepriemntID
     *
     * @param comboObjectiveFunctions
     * @param experimentId
     */
    public static void initializerComboObjectiveFunctions(JComboBox comboObjectiveFunctions, String experimentId) {
        String metricsSelectedForCurrentExperiment[] = db.Database.getOrdenedObjectives(experimentId).split(" ");

        comboObjectiveFunctions.setModel(new DefaultComboBoxModel());

        for (int i = 0; i < metricsSelectedForCurrentExperiment.length; i++)
            comboObjectiveFunctions.addItem(Utils.capitalize(metricsSelectedForCurrentExperiment[i]));

        comboObjectiveFunctions.updateUI();
    }

    public void configureSmartyProfile(JTextField fieldSmartyProfile, JCheckBox check, JButton button) {
        if (config.getConfig().getPathToProfile() != null && config.getConfig().getPathToProfile().equals("")) {
            check.setSelected(true);
            button.setEnabled(false);
        } else if (hasSmartyInConfiFile()) {
            fieldSmartyProfile.setText(config.getConfig().getPathToProfile());
        } else {
            Path target = Paths.get(UserHome.getOplaUserHome() + "profiles" + FILE_SEPARATOR + profileSmartyName);
            FileUtils.copy(PROFILES_DIR + profileSmartyName, target);

            fieldSmartyProfile.setText(target.toString());
            fieldSmartyProfile.updateUI();
            config.updatePathToProfileSmarty(target.toString());
        }
    }

    public void configureConcernsProfile(JTextField fieldConcernProfile, JCheckBox check, JButton button) {
        if (config.getConfig().getPathToProfileConcern() != null
                && config.getConfig().getPathToProfileConcern().equals("")) {
            check.setSelected(true);
            button.setEnabled(false);
        } else if (hasConcernsInConfiFile()) {
            fieldConcernProfile.setText(config.getConfig().getPathToProfileConcern());
        } else {

            Path target = Paths.get(UserHome.getOplaUserHome() + "profiles" + FILE_SEPARATOR + profileConcernsName);
            FileUtils.copy(PROFILES_DIR + profileConcernsName, target);

            fieldConcernProfile.setText(target.toString());
            fieldConcernProfile.updateUI();
            config.updatePathToProfileConcerns(target.toString());
        }
    }

    public void configurePatternsProfile(JTextField fieldPatterns, JCheckBox check, JButton button) {
        if (config.getConfig().getPathToProfilePatterns() != null
                && config.getConfig().getPathToProfilePatterns().equals("")) {
            check.setSelected(true);
            button.setEnabled(false);
        } else if (hasPatternsInConfigFile()) {
            fieldPatterns.setText(config.getConfig().getPathToProfilePatterns());
        } else {

            Path target = Paths.get(UserHome.getOplaUserHome() + "profiles" + FILE_SEPARATOR + profilePatternName);
            FileUtils.copy(PROFILES_DIR + profilePatternName, target);

            fieldPatterns.setText(target.toString());
            fieldPatterns.updateUI();
            config.updatePathToProfilePatterns(target.toString());
        }
    }

    public void configureRelationshipsProfile(JTextField fieldRelationships, JCheckBox check, JButton button) {
        if (config.getConfig().getPathToProfileRelationships() != null
                && config.getConfig().getPathToProfileRelationships().equals("")) {
            check.setSelected(true);
            button.setEnabled(false);
        } else if (hasRelationshipsInConfigFile()) {
            fieldRelationships.setText(config.getConfig().getPathToProfileRelationships());
        } else {

            Path target = Paths.get(UserHome.getOplaUserHome() + "profiles" + FILE_SEPARATOR + profileRelationshipName);
            FileUtils.copy(PROFILES_DIR + profileRelationshipName, target);

            fieldRelationships.setText(target.toString());
            fieldRelationships.updateUI();
            config.updatePathToProfileRelationships(target.toString());
        }
    }

    /**
     * Directory with templates for models. This files SHOULD NOT BE CHANGED.
     *
     * @param fieldTemplate
     */
    public void configureTemplates(JTextField fieldTemplate) {
        if (hasTemplateInConfigFile()) {
            fieldTemplate.setText(config.getConfig().getPathToTemplateModelsDirectory());
        } else {
            Path pathSimplesUml = Paths.get(UserHome.getOplaUserHome() + "templates" + FILE_SEPARATOR + "simples.uml");
            Path pathSimplesDi = Paths.get(UserHome.getOplaUserHome() + "templates" + GuiServices.FILE_SEPARATOR + "simples.di");
            Path pathSimplesNotation = Paths.get(UserHome.getOplaUserHome() + "templates" + GuiServices.FILE_SEPARATOR + "simples.notation");

            FileUtils.copy("templates/simples.uml", pathSimplesUml);
            FileUtils.copy("templates/simples.di", pathSimplesDi);
            FileUtils.copy("templates/simples.notation", pathSimplesNotation);

            String template = UserHome.getOplaUserHome() + "templates" + FILE_SEPARATOR;
            fieldTemplate.setText(template);
            fieldTemplate.updateUI();
            config.updatePathToTemplateFiles(template);
        }
    }

    /**
     * Output directory (.uml, .di .notation).
     *
     * @param fieldOutput
     */
    public void configureLocaleToExportModels(JTextField fieldOutput) {
        if (hasPathToSaveModelsInConfigFile()) {
            fieldOutput.setText(config.getConfig().getDirectoryToExportModels());
        } else {
            final String path = UserHome.getOplaUserHome() + "output" + FILE_SEPARATOR;
            fieldOutput.setText(path);
            fieldOutput.updateUI();
            config.updatePathToExportModels(path);
        }

    }

    /**
     * Internal use directory.
     *
     * @param fieldManipulationDir
     */
    public void configureLocaleToSaveModels(JTextField fieldManipulationDir) {
        if (hasPathToManipulationDir()) {
            fieldManipulationDir.setText(config.getConfig().getDirectoryToSaveModels());
        } else {
            final String path = UserHome.getOplaUserHome() + "temp" + FILE_SEPARATOR;
            fieldManipulationDir.setText(path);
            fieldManipulationDir.updateUI();
            config.updatePathToSaveModels(path);
        }
    }

    private boolean hasRelationshipsInConfigFile() {
        return config.getConfig().getPathToProfileRelationships() != null;
    }

    private boolean hasPatternsInConfigFile() {
        return config.getConfig().getPathToProfilePatterns() != null;
    }

    private boolean hasSmartyInConfiFile() {
        return config.getConfig().getPathToProfile() != null;
    }

    private boolean hasConcernsInConfiFile() {
        return config.getConfig().getPathToProfileConcern() != null;
    }

    private boolean hasTemplateInConfigFile() {
        return config.getConfig().getPathToTemplateModelsDirectory() != null;
    }

    private boolean hasPathToSaveModelsInConfigFile() {
        return config.getConfig().getDirectoryToExportModels() != null;
    }

    private boolean hasPathToManipulationDir() {
        return config.getConfig().getDirectoryToSaveModels() != null;
    }

    public void hidePanelPatternScopeByDefault(JPanel panelPatternScope) {
        panelPatternScope.setVisible(false);
    }

    /**
     * Copy GUI setting files (guisettings.yml) to oplatool root directory
     */
    public void copyFileGuiSettings() {
        Path target = Paths.get(UserHome.getOplaUserHome() + "guisettings.yml");
        if (!Files.exists(target)) {
            FileUtils.copy("config/guisettings.yml", target);
        }
    }

    /**
     * Copy hybervolume binary to oplatool bins directory if OS isn't Windows
     * (eca).
     *
     * @throws Exception
     */
    public void copyBinHypervolume() throws Exception {
        if (!OsUtils.isWindows()) {
            Path targetDir = Paths.get(UserHome.getOplaUserHome() + "bins");
            if (!Files.exists(targetDir)) {
                FileUtils.createDirectory(targetDir);
            }

            Path targetHvFile = Paths.get(targetDir + FILE_SEPARATOR + "hv");
            if (!Files.exists(targetHvFile)) {
                FileUtils.copy("hv", targetHvFile);
            }
        }
    }
}
