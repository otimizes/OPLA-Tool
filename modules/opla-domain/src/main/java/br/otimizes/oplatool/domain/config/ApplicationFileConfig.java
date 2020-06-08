package br.otimizes.oplatool.domain.config;

import java.util.logging.Logger;

/**
 * @author elf
 */
public class ApplicationFileConfig {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFileConfig.class.getName());

    private static ManagerApplicationFileConfig applicationFileConfig = null;
    private static ApplicationYamlConfig instance;

    protected ApplicationFileConfig() {
    }

    public static ManagerApplicationFileConfig getApplicationFileConfig() {
        if (applicationFileConfig == null) {
            applicationFileConfig = new ManagerApplicationFileConfig();
        }
        return applicationFileConfig;
    }

    public static ApplicationYamlConfig getInstance() {
        ManagerApplicationFileConfig applicationFileConfig = getApplicationFileConfig();
        instance = applicationFileConfig.getApplicationYaml();
        return instance;
    }
}
