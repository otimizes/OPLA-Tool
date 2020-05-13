package br.ufpr.dinf.gres.architecture.io;

import br.ufpr.dinf.gres.domain.config.ApplicationFile;
import br.ufpr.dinf.gres.domain.config.ApplicationYamlConfig;
import br.ufpr.dinf.gres.domain.OPLAThreadScope;

public class OPLAConfigThreadScope {

    public static ThreadLocal<ApplicationYamlConfig> config = ThreadLocal.withInitial(() -> ApplicationFile.getInstance().getApplicationYaml());

    public static ThreadLocal<String> userDir = new ThreadLocal<>();
    public static ThreadLocal<String> pla = new ThreadLocal<>();

    public static void setConfig(ApplicationYamlConfig config) {
        config.setDirectoryToExportModels(config.getDirectoryToExportModels() + OPLAThreadScope.hash.get() + System.getProperty("file.separator"));
        config.setDirectoryToSaveModels(config.getDirectoryToSaveModels() + OPLAThreadScope.hash.get() + System.getProperty("file.separator"));
        OPLAConfigThreadScope.config.set(config);
    }
}
