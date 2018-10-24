/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufpr.br.opla.configuration;

import arquitetura.io.FileUtils;
import com.ufpr.br.opla.utils.Utils;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

/**
 * @author elf
 */
public class UserHome {

    private static final String USER_HOME = System.getProperty("user.home");

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    /**
     * Ex: C:/User/oplatool/ or /USER_HOME/user/oplatool/
     */
    private static final String OPLA_HOME = USER_HOME + FILE_SEPARATOR + "oplatool" + FILE_SEPARATOR;

    /**
     * User Home directory
     *
     * @return
     */
    public static String getOplaUserHome() {
        return OPLA_HOME;
    }

    public static String getConfigurationFilePath() {
        return OPLA_HOME + FILE_SEPARATOR + "application.yaml";
    }

    public static String getGuiSettingsFilePath() {
        return OPLA_HOME + FILE_SEPARATOR + "guisettings.yml";
    }

    /**
     * Execute the creation of directory
     */
    public static void createDefaultOplaPathIfDontExists() {
        FileUtils.createDirectory(Paths.get(getOplaUserHome()));
    }

    public static void createProfilesPath() {
        Utils.createPath(OPLA_HOME + "profiles/");
    }

    public static void createTemplatePath() {
        Utils.createPath(OPLA_HOME + "templates/");
    }

    public static void createOutputPath() {
        Utils.createPath(OPLA_HOME + "output/");
    }

    public static void createTempPath() {
        Utils.createPath(OPLA_HOME + "temp/");
    }

    public static String getFileSeparator() {
        return FILE_SEPARATOR;
    }

    public static String getPathToDb() {
        return getOplaUserHome() + "db" + FILE_SEPARATOR + "oplatool.db";
    }

    public static String getPathToConfigFile() {
        return getOplaUserHome() + "application.yaml";
    }

}
