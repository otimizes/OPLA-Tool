package com.ufpr.br.opla.configuration;

import com.ufpr.br.opla.utils.Utils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */

/**
 * @author elf
 */
public class ManagerApplicationConfig {

    private static final Logger LOGGER = Logger.getLogger(ManagerApplicationConfig.class);

    private DirTarget configurationFile;

    public ManagerApplicationConfig() throws FileNotFoundException {
        try {
            Yaml yaml = new Yaml();
            Path path = Paths.get(UserHome.getConfigurationFilePath());
            this.configurationFile = yaml.loadAs(new FileInputStream(path.toFile()), DirTarget.class);
        } catch (FileNotFoundException ex) {
            LOGGER.info(ex);
            throw ex;
        }
    }

    public DirTarget getConfig() {
        return this.configurationFile;
    }

    public void updatePathToProfileSmarty(String newpath) {
        this.configurationFile.setPathToProfile(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfilePatterns(String newpath) {
        this.configurationFile.setPathToProfilePatterns(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfileRelationships(String newpath) {
        this.configurationFile.setPathToProfileRelationships(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfileConcerns(String newpath) {
        this.configurationFile.setPathToProfileConcern(newpath);
        updateConfigurationFile();
    }

    public void updatePathToTemplateFiles(String newpath) {
        this.configurationFile.setPathToTemplateModelsDirectory(newpath);
        updateConfigurationFile();
    }

    public void updatePathToExportModels(String newpath) {
        this.configurationFile.setDirectoryToExportModels(newpath);
        updateConfigurationFile();
    }

    public void updatePathToSaveModels(String path) {
        this.configurationFile.setDirectoryToSaveModels(path);
        updateConfigurationFile();

    }

    /**
     * Retorna os profile que estão em uso ou seja, não "" nem null.
     */
    public String getProfilesUsed() {
        StringBuilder profiles = new StringBuilder();

        if (Utils.notNullAndNotEmpty(this.configurationFile.getPathToProfile())) {
            profiles.append(this.configurationFile.getPathToProfile());
            profiles.append(",");
        }

        if (Utils.notNullAndNotEmpty(this.configurationFile.getPathToProfileConcern())) {
            profiles.append(this.configurationFile.getPathToProfileConcern());
            profiles.append(",");
        }

        if (Utils.notNullAndNotEmpty(this.configurationFile.getPathToProfilePatterns())) {
            profiles.append(this.configurationFile.getPathToProfilePatterns());
            profiles.append(",");
        }

        if (Utils.notNullAndNotEmpty(this.configurationFile.getPathToProfileRelationships())) {
            profiles.append(this.configurationFile.getPathToProfileRelationships());
        }
        return profiles.toString();
    }

    private void updateConfigurationFile() {
        try {
            final DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
            options.setPrettyFlow(true);

            Yaml yaml = new Yaml(options);
            yaml.dump(configurationFile, new FileWriter(Paths.get(UserHome.getConfigurationFilePath()).toFile()));
        } catch (IOException ex) {
            LOGGER.info("Ops, Error when try update configuration file:", ex);
        }
    }
}
