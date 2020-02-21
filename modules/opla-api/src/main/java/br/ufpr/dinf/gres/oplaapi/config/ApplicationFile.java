package br.ufpr.dinf.gres.oplaapi.config;

import org.apache.log4j.Logger;

/**
 * @author elf
 */
public class ApplicationFile {

    private static final Logger LOGGER = Logger.getLogger(ApplicationFile.class);

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
