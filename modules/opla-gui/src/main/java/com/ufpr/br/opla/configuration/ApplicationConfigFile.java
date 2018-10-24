package com.ufpr.br.opla.configuration;

import java.io.FileNotFoundException;

/**
 * @author elf
 */
public class ApplicationConfigFile {

    private static ManagerApplicationConfig instance = null;

    protected ApplicationConfigFile() {
    }

    public static ManagerApplicationConfig getInstance() throws FileNotFoundException {
        if (instance == null) {
            instance = new ManagerApplicationConfig();
        }
        return instance;
    }
}
