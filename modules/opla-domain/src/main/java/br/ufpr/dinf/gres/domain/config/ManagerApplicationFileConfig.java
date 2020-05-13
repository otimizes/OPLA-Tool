package br.ufpr.dinf.gres.domain.config;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * @author elf
 */
public class ManagerApplicationFileConfig {

    private static final String LOCAL_APPLICATION_YAML = FileConstants.CONFIG_PATH + FileConstants.FILE_SEPARATOR + FileConstants.APPLICATION_YAML_NAME;
    private static final Logger LOGGER = Logger.getLogger(ManagerApplicationFileConfig.class.getName());
    private ApplicationYamlConfig applicationYaml;
    private Yaml yaml;

    public ManagerApplicationFileConfig() {
        try {
            Path pahtApplicationYaml = UserHome.getApplicationYamlConfig();
            configYamlInstance();
            if (!Files.exists(pahtApplicationYaml)) {
                copyYamlFile(pahtApplicationYaml);
            } else {
                try (FileInputStream fileInputStream = new FileInputStream(pahtApplicationYaml.toFile())) {
                    this.applicationYaml = this.yaml.loadAs(fileInputStream, ApplicationYamlConfig.class);
                }
            }

        } catch (IOException ex) {
            LOGGER.info(ex.getMessage());
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

    public void updatePathToProfileSmarty(String newpath) throws IOException {
        this.applicationYaml.setPathToProfile(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfilePatterns(String newpath) throws IOException {
        this.applicationYaml.setPathToProfilePatterns(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfileRelationships(String newpath) throws IOException {
        this.applicationYaml.setPathToProfileRelationships(newpath);
        updateConfigurationFile();
    }

    public void updatePathToProfileConcerns(String newpath) throws IOException {
        this.applicationYaml.setPathToProfileConcern(newpath);
        updateConfigurationFile();
    }

    public void updatePathToTemplateFiles(String newpath) throws IOException {
        this.applicationYaml.setPathToTemplateModelsDirectory(newpath);
        updateConfigurationFile();
    }

    public String updateDefaultPathToTemplateFiles() throws IOException {
        String pathToTemplateModelsDirectory = UserHome.getPathToTemplates();
        this.updatePathToTemplateFiles(pathToTemplateModelsDirectory);
        return pathToTemplateModelsDirectory;
    }

    public void updatePathToExportModels(String newpath) throws IOException {
        this.applicationYaml.setDirectoryToExportModels(newpath);
        updateConfigurationFile();
    }

    public String configureDefaultLocaleToExportModels() throws IOException {
        String path = UserHome.getOplaUserHome() + FileConstants.OUTPUT_DIR + FileConstants.FILE_SEPARATOR;
        this.updatePathToExportModels(path);
        return path;
    }

    public String setProfilesToSpecificPath(String path) throws IOException {
        String dir = path.substring(0, path.lastIndexOf(System.getProperty("file.separator")));
        this.updatePathToProfileSmarty(dir + System.getProperty("file.separator") + "smarty.profile.uml");
        this.updatePathToProfileConcerns(dir + System.getProperty("file.separator") + "concerns.profile.uml");
        this.updatePathToProfilePatterns(dir + System.getProperty("file.separator") + "br.ufpr.dinf.gres.patterns.profile.uml");
        this.updatePathToProfileRelationships(dir + System.getProperty("file.separator") + "relationships.profile.uml");
        return dir;
    }

    public void updatePathToInteraction(String newpath) throws IOException {
        this.applicationYaml.setDirectoryToInteraction(newpath);
        updateConfigurationFile();
    }

    public void updatePathPapyurs(String newpath) throws IOException {
        this.applicationYaml.setPathPapyrus(newpath);
        updateConfigurationFile();
    }

    public void updatePathToSaveModels(String newpath) throws IOException {
        this.applicationYaml.setDirectoryToSaveModels(newpath);
        updateConfigurationFile();
    }

    public String updateDefaultPathToSaveModels() throws IOException {
        String pathTempDir = UserHome.getOplaUserHome() + FileConstants.TEMP_DIR + FileConstants.FILE_SEPARATOR;
        this.updatePathToSaveModels(pathTempDir);
        return pathTempDir;
    }

    public void updatePathLastOptimizationInput(String newpath) throws IOException {
        this.applicationYaml.setPathLastOptimizationInput(newpath);
        updateConfigurationFile();
    }

    private void updateConfigurationFile() throws IOException {
        try {
            configYamlInstance();
            yaml.dump(applicationYaml, new FileWriter(UserHome.getApplicationYamlConfig().toFile()));
        } catch (IOException ex) {
            LOGGER.info("Ops, Error when try update configuration file:" + ex.getMessage());
            throw ex;
        }
    }

}
