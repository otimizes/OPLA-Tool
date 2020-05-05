package br.ufpr.dinf.gres.architecture.config;

import org.apache.commons.lang.StringUtils;

/**
 * Representação do arquivo de configuração "application.yaml"
 *
 * @author Fernando-Godoy
 */
public class ApplicationYamlConfig {

    private String pathToTemplateModelsDirectory;
    private String directoryToSaveModels;
    private String directoryToInteraction;
    private String pathPapyrus;
    private String pathSmarty;
    private String directoryToExportModels;
    private String pathToProfile;
    private String pathToProfileConcern;
    private String pathToProfileRelationships;
    private String pathToProfilePatterns;
    private String pathLastOptimizationInput;
    private String emailUser;
    private String emailPassword;
    private Boolean smarty = false;
    private Boolean feature = false;
    private Boolean patterns = false;
    private Boolean relationships = false;

    public String getPathToTemplateModelsDirectory() {
        return StringUtils.defaultString(pathToTemplateModelsDirectory);
    }

    public void setPathToTemplateModelsDirectory(String pathToTemplateModelsDirectory) {
        this.pathToTemplateModelsDirectory = pathToTemplateModelsDirectory;
    }

    public String getDirectoryToSaveModels() {
        return StringUtils.defaultString(directoryToSaveModels);
    }

    public void setDirectoryToSaveModels(String directoryToSaveModels) {
        this.directoryToSaveModels = directoryToSaveModels;
    }

    public String getDirectoryToExportModels() {
        return StringUtils.defaultString(directoryToExportModels);
    }

    public void setDirectoryToExportModels(String directoryToExportModels) {
        this.directoryToExportModels = directoryToExportModels;
    }

    public String getPathToProfile() {
        return StringUtils.defaultString(pathToProfile);
    }

    public void setPathToProfile(String pathToProfile) {
        this.pathToProfile = pathToProfile;
    }

    public String getPathToProfileConcern() {
        return StringUtils.defaultString(pathToProfileConcern);
    }

    public void setPathToProfileConcern(String pathToProfileConcern) {
        this.pathToProfileConcern = pathToProfileConcern;
    }

    public String getPathToProfileRelationships() {
        return StringUtils.defaultString(pathToProfileRelationships);
    }

    public void setPathToProfileRelationships(String pathToProfileRelationships) {
        this.pathToProfileRelationships = pathToProfileRelationships;
    }

    public String getPathToProfilePatterns() {
        return StringUtils.defaultString(pathToProfilePatterns);
    }

    public void setPathToProfilePatterns(String pathToProfilePatterns) {
        this.pathToProfilePatterns = pathToProfilePatterns;
    }

    public String getPathLastOptimizationInput() {
        return pathLastOptimizationInput;
    }

    public void setPathLastOptimizationInput(String lastOptimizationInput) {
        this.pathLastOptimizationInput = lastOptimizationInput;
    }

    public String getDirectoryToInteraction() {
        return StringUtils.defaultString(directoryToInteraction);
    }

    public void setDirectoryToInteraction(String directoryToInteraction) {
        this.directoryToInteraction = directoryToInteraction;
    }

    public String getPathPapyrus() {
        return StringUtils.defaultString(pathPapyrus);
    }

    public void setPathPapyrus(String pathPapyrus) {
        this.pathPapyrus = pathPapyrus;
    }

    public Boolean getSmarty() {
        return smarty;
    }

    public void setSmarty(Boolean smarty) {
        this.smarty = smarty;
    }

    public Boolean getFeature() {
        return feature;
    }

    public void setFeature(Boolean feature) {
        this.feature = feature;
    }

    public Boolean getPatterns() {
        return patterns;
    }

    public void setPatterns(Boolean patterns) {
        this.patterns = patterns;
    }

    public Boolean getRelationships() {
        return relationships;
    }

    public void setRelationships(Boolean relationships) {
        this.relationships = relationships;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getPathSmarty() {
        return StringUtils.defaultString(pathSmarty);
    }

    public void setPathSmarty(String pathSmarty) {
        this.pathSmarty = pathSmarty;
    }
}
