/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.br.opla.configuration;

import java.io.FileNotFoundException;

/**
 * @author elf
 */
public class ApplicationFile {

    private static ManagerApplicationConfig instance = null;

    protected ApplicationFile() {
    }

    public static ManagerApplicationConfig getInstance() throws FileNotFoundException {
        if (instance == null)
            instance = new ManagerApplicationConfig();

        return instance;
    }
}
