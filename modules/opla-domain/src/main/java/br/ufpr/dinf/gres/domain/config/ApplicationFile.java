package br.ufpr.dinf.gres.domain.config;

import java.util.logging.Logger;

/**
 * @author elf
 */
public class ApplicationFile {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFile.class.getName());

    private static ManagerApplicationConfig instance = null;

    protected ApplicationFile() {
    }

    public static ManagerApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ManagerApplicationConfig();
        }
        return instance;
    }
}
