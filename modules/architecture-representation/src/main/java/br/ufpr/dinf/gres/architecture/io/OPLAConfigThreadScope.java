package br.ufpr.dinf.gres.architecture.io;

import br.ufpr.dinf.gres.architecture.config.ApplicationFile;
import br.ufpr.dinf.gres.architecture.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;

public class OPLAConfigThreadScope {

    public static ThreadLocal<ApplicationYamlConfig> config = ThreadLocal.withInitial(() -> ApplicationFile.getInstance().getApplicationYaml());

    public static ThreadLocal<String> userDir = new ThreadLocal<>();
    public static ThreadLocal<String> pla = new ThreadLocal<>();

    public static void setConfig(ApplicationYamlConfig config) {
        config.setPathToProfile(config.getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + config.getPathToProfile());
        config.setPathToProfileRelationships(config.getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + config.getPathToProfileRelationships());
        config.setPathToProfilePatterns(config.getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + config.getPathToProfilePatterns());
        config.setPathToProfileConcern(config.getDirectoryToExportModels() + OPLAThreadScope.token.get() + System.getProperty("file.separator") + config.getPathToProfileConcern());
        config.setDirectoryToExportModels(config.getDirectoryToExportModels() + OPLAThreadScope.hash.get() + System.getProperty("file.separator"));
        config.setDirectoryToSaveModels(config.getDirectoryToSaveModels() + OPLAThreadScope.hash.get() + System.getProperty("file.separator"));
        OPLAConfigThreadScope.config.set(config);
    }
}
