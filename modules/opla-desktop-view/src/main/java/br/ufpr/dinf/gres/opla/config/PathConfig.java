package br.ufpr.dinf.gres.opla.config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Fernando-Godoy
 */
public class PathConfig {

    private Path directoryToSaveModels;

    private Path directoryToInteraction;
    
    private Path pathPapyrus;

    private Path directoryToExportModels;

    private Path pathToProfileConcern;

    private Path pathToTemplateModelsDirectory;

    private Path pathToProfileRelationships;

    private Path pathToProfilePatterns;

    private Path pahtToProfileSmarty;

    private Path pathToNewConfigurations;

    private Path dirTarget;

    private Path dirExportTarget;

    private Path pathLastOptimizationInput;

    private int archiveSize;

    public PathConfig() {
    }

    public PathConfig(ApplicationYamlConfig applicationYaml) {
        this.directoryToSaveModels = Paths.get(applicationYaml.getDirectoryToSaveModels());
        this.pathPapyrus = Paths.get(applicationYaml.getPathPapyrus());
        this.directoryToInteraction = Paths.get(applicationYaml.getDirectoryToInteraction());
        this.directoryToExportModels = Paths.get(applicationYaml.getDirectoryToExportModels());
        this.pahtToProfileSmarty = Paths.get(applicationYaml.getPathToProfile());
        this.pathToProfileConcern = Paths.get(applicationYaml.getPathToProfileConcern());
        this.pathToProfilePatterns = Paths.get(applicationYaml.getPathToProfilePatterns());
        this.pathToProfileRelationships = Paths.get(applicationYaml.getPathToProfileRelationships());
        this.pathToTemplateModelsDirectory = Paths.get(applicationYaml.getPathToTemplateModelsDirectory());
    }

    public Path getDirectoryToSaveModels() {
        return directoryToSaveModels;
    }

    public void setDirectoryToSaveModels(Path directoryToSaveModels) {
        this.directoryToSaveModels = directoryToSaveModels;
    }

    public Path getDirectoryToExportModels() {
        return directoryToExportModels;
    }

    public void setDirectoryToExportModels(Path directoryToExportModels) {
        this.directoryToExportModels = directoryToExportModels;
    }

    public Path getPathToProfileConcern() {
        return pathToProfileConcern;
    }

    public void setPathToProfileConcern(Path pathToProfileConcern) {
        this.pathToProfileConcern = pathToProfileConcern;
    }

    public Path getPathToTemplateModelsDirectory() {
        return pathToTemplateModelsDirectory;
    }

    public void setPathToTemplateModelsDirectory(Path pathToTemplateModelsDirectory) {
        this.pathToTemplateModelsDirectory = pathToTemplateModelsDirectory;
    }

    public Path getPathToProfileRelationships() {
        return pathToProfileRelationships;
    }

    public void setPathToProfileRelationships(Path pathToProfileRelationships) {
        this.pathToProfileRelationships = pathToProfileRelationships;
    }

    public Path getPathToProfilePatterns() {
        return pathToProfilePatterns;
    }

    public void setPathToProfilePatterns(Path pathToProfilePatterns) {
        this.pathToProfilePatterns = pathToProfilePatterns;
    }

    public Path getPathToProfileSmarty() {
        return pahtToProfileSmarty;
    }

    public void setPathToProfileSmarty(Path pahtToProfileSmarty) {
        this.pahtToProfileSmarty = pahtToProfileSmarty;
    }

    public Path getPathToNewConfigurations() {
        return pathToNewConfigurations;
    }

    public void setPathToNewConfigurations(Path pathToNewConfigurations) {
        this.pathToNewConfigurations = pathToNewConfigurations;
    }

    public Path getDirTarget() {
        return dirTarget;
    }

    public void setDirTarget(Path dirTarget) {
        this.dirTarget = dirTarget;
    }

    public Path getDirExportTarget() {
        return dirExportTarget;
    }

    public void setDirExportTarget(Path dirExportTarget) {
        this.dirExportTarget = dirExportTarget;
    }

    public int getArchiveSize() {
        return archiveSize;
    }

    public void setArchiveSize(int archiveSize) {
        this.archiveSize = archiveSize;
    }

    public Path getPathLastOptimizationInput() {
        return pathLastOptimizationInput;
    }

    public void setPathLastOptimizationInput(Path lastOptimizationInput) {
        this.pathLastOptimizationInput = lastOptimizationInput;
    }

    public Path getDirectoryToInteraction() {
        return directoryToInteraction;
    }

    public void setDirectoryToInteraction(Path directoryToInteraction) {
        this.directoryToInteraction = directoryToInteraction;
    }

    public Path getPahtToProfileSmarty() {
        return pahtToProfileSmarty;
    }

    public void setPahtToProfileSmarty(Path pahtToProfileSmarty) {
        this.pahtToProfileSmarty = pahtToProfileSmarty;
    }

    public Path getPathPapyrus() {
        return pathPapyrus;
    }

    public void setPathPapyrus(Path pathPapyrus) {
        this.pathPapyrus = pathPapyrus;
    }
    
    

}
