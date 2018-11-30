package br.ufpr.dinf.gres.opla.config;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import br.ufpr.dinf.gres.opla.view.util.AlertUtil;
import br.ufpr.dinf.gres.opla.view.util.Constants;
import br.ufpr.dinf.gres.opla.view.util.UserHome;

/**
 * @author elf
 */
public class ManagerApplicationConfig {

    private static final String LOCAL_APPLICATION_YAML = Constants.CONFIG_PATH + Constants.FILE_SEPARATOR + Constants.APPLICATION_YAML_NAME;

    private static final Logger LOGGER = Logger.getLogger(ManagerApplicationConfig.class);

    private ApplicationYamlConfig applicationYaml;
    private PathConfig configurationFile;
    private Yaml yaml;

    public ManagerApplicationConfig() {
        try {
            Path pahtApplicationYaml = UserHome.getApplicationYamlConfig();
            configYamlInstance();
            if (!Files.exists(pahtApplicationYaml)) {
                copyYamlFile(pahtApplicationYaml);
                this.configurationFile = new PathConfig(new ApplicationYamlConfig());
            } else {
                try (FileInputStream fileInputStream = new FileInputStream(pahtApplicationYaml.toFile())) {
                    this.applicationYaml = this.yaml.loadAs(fileInputStream, ApplicationYamlConfig.class);
                    this.configurationFile = new PathConfig(applicationYaml);
                }
            }

        } catch (IOException ex) {
            LOGGER.warn(ex);
            AlertUtil.showMessage(AlertUtil.DEFAULT_ALERT_ERROR);
            System.exit(0);
        }
    }

    public ApplicationYamlConfig getApplicationYaml() {
        return applicationYaml;
    }

    private void copyYamlFile(Path pahtApplicationYaml) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(LOCAL_APPLICATION_YAML);
        applicationYaml = this.yaml.loadAs(inputStream, ApplicationYamlConfig.class);
        try (FileWriter fileWriter = new FileWriter(pahtApplicationYaml.toFile())) {
            this.yaml.dump(applicationYaml, fileWriter);
        }
        IOUtils.closeQuietly(inputStream);
    }

    private void configYamlInstance() {
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
        options.setPrettyFlow(true);

        this.yaml = new Yaml(options);
    }

    public PathConfig getConfig() {
        return this.configurationFile;
    }

    public void updatePathToProfileSmarty(String newpath) throws IOException {
        this.configurationFile.setPathToProfileSmarty(Paths.get(newpath));
        this.applicationYaml.setPathToProfile(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfilePatterns(String newpath) throws IOException {
        this.configurationFile.setPathToProfilePatterns(Paths.get(newpath));
        this.applicationYaml.setPathToProfilePatterns(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfileRelationships(String newpath) throws IOException {
        this.configurationFile.setPathToProfileRelationships(Paths.get(newpath));
        this.applicationYaml.setPathToProfileRelationships(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfileConcerns(String newpath) throws IOException {
        this.configurationFile.setPathToProfileConcern(Paths.get(newpath));
        this.applicationYaml.setPathToProfileConcern(newpath);
        updateConfigurationFile();
    }

    public void updatePathToTemplateFiles(String newpath) throws IOException {
        this.configurationFile.setPathToTemplateModelsDirectory(Paths.get(newpath));
        this.applicationYaml.setPathToTemplateModelsDirectory(newpath);
        updateConfigurationFile();
    }

    public String updateDefaultPathToTemplateFiles() throws IOException {
        String pathToTemplateModelsDirectory = UserHome.getPathToTemplates();
        this.updatePathToTemplateFiles(pathToTemplateModelsDirectory);
        return pathToTemplateModelsDirectory;
    }

    public void updatePathToExportModels(String newpath) throws IOException {
        this.configurationFile.setDirectoryToExportModels(Paths.get(newpath));
        this.applicationYaml.setDirectoryToExportModels(newpath);
        updateConfigurationFile();
    }

    public String configureDefaultLocaleToExportModels() throws IOException {
        String path = UserHome.getOplaUserHome() + Constants.OUTPUT_DIR + Constants.FILE_SEPARATOR;
        this.updatePathToExportModels(path);
        return path;
    }

    public String setProfilesToSpecificPath(String path) throws IOException {
        String dir = path.substring(0, path.lastIndexOf(System.getProperty("file.separator")));
        this.updatePathToProfileSmarty(dir + System.getProperty("file.separator") + "smarty.profile.uml");
        this.updatePathToProfileConcerns(dir + System.getProperty("file.separator") + "concerns.profile.uml");
        this.updatePathToProfilePatterns(dir + System.getProperty("file.separator") + "patterns.profile.uml");
        this.updatePathToProfileRelationships(dir + System.getProperty("file.separator") + "relationships.profile.uml");
        return dir;
    }

    public void updatePathToInteraction(String newpath) throws IOException {
        this.configurationFile.setDirectoryToInteraction(Paths.get(newpath));
        this.applicationYaml.setDirectoryToInteraction(newpath);
        updateConfigurationFile();
    }

    public void updatePathPapyurs(String newpath) throws IOException {
        this.configurationFile.setPathPapyrus(Paths.get(newpath));
        this.applicationYaml.setPathPapyrus(newpath);
        updateConfigurationFile();
    }

    public void updatePathToSaveModels(String newpath) throws IOException {
        this.configurationFile.setDirectoryToSaveModels(Paths.get(newpath));
        this.applicationYaml.setDirectoryToSaveModels(newpath);
        updateConfigurationFile();
    }

    public String updateDefaultPathToSaveModels() throws IOException {
        String pathTempDir = UserHome.getOplaUserHome() + Constants.TEMP_DIR + Constants.FILE_SEPARATOR;
        this.updatePathToSaveModels(pathTempDir);
        return pathTempDir;
    }

    public void updatePathLastOptimizationInput(String newpath) throws IOException {
        this.configurationFile.setPathLastOptimizationInput(Paths.get(newpath));
        this.applicationYaml.setPathLastOptimizationInput(newpath);
        updateConfigurationFile();
    }

    private void updateConfigurationFile() throws IOException {
        try {
            configYamlInstance();
            yaml.dump(applicationYaml, new FileWriter(UserHome.getApplicationYamlConfig().toFile()));
        } catch (IOException ex) {
            LOGGER.warn("Ops, Error when try update configuration file:", ex);
            throw ex;
        }
    }

    /**
     * Retorna os profile que estão em uso ou seja, não "" nem null.
     */
    public String getProfilesUsed() {
        StringBuilder profiles = new StringBuilder();

        if (Files.exists(this.configurationFile.getPathToProfileSmarty())) {
            profiles.append(this.configurationFile.getPathToProfileSmarty().toString());
            profiles.append(",");
        }

        if (Files.exists(this.configurationFile.getPathToProfileConcern())) {
            profiles.append(this.configurationFile.getPathToProfileConcern());
            profiles.append(",");
        }

        if (Files.exists(this.configurationFile.getPathToProfilePatterns())) {
            profiles.append(this.configurationFile.getPathToProfilePatterns());
            profiles.append(",");
        }

        if (Files.exists(this.configurationFile.getPathToProfileRelationships())) {
            profiles.append(this.configurationFile.getPathToProfileRelationships());
        }
        return profiles.toString();
    }


}
