package br.ufpr.dinf.gres.opla.view.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import arquitetura.io.FileUtils;

/**
 * @author elf
 */
public class UserHome {

    /**
     * User Home directory Ex: C:/User/oplatool/ or /home/user/oplatool/
     *
     * @return
     */
    public static String getOplaUserHome() {
        return Constants.USER_HOME + Constants.FILE_SEPARATOR + "oplatool" + Constants.FILE_SEPARATOR;
    }

    public static Path getApplicationYamlConfig() {
        return Paths.get(getOplaUserHome()).resolve("application.yaml");
    }

    public static String getGuiSettingsFilePath() {
        return getOplaUserHome() + "guisettings.yml";
    }

    public static void createDefaultOplaPathIfDontExists() {
        FileUtils.createDirectory(Paths.get(getOplaUserHome()));
    }

    public static void createProfilesPath() {
        Utils.createPath(getOplaUserHome() + "profiles" + Constants.FILE_SEPARATOR);
    }

    public static void createBinsHVPath() {
        Utils.createPath(getOplaUserHome() + "bins" + Constants.FILE_SEPARATOR);
//        Utils.createPath(getOplaUserHome() + "bins" + Constants.FILE_SEPARATOR + "hv" + Constants.FILE_SEPARATOR);
    }

    public static void createTemplatePath() {
        Utils.createPath(getOplaUserHome() + "templates" + Constants.FILE_SEPARATOR);
    }

    public static void createOutputPath() {
        Utils.createPath(getOplaUserHome() + "output" + Constants.FILE_SEPARATOR);
    }

    public static void createTempPath() {
        Utils.createPath(getOplaUserHome() + "temp" + Constants.FILE_SEPARATOR);
    }

    public static String getPathToDb() {
        return getOplaUserHome() + "db" + Constants.FILE_SEPARATOR + "oplatool.db";
    }
    
    public static String getPathToTemplates() {
    	return getOplaUserHome() + "templates" + Constants.FILE_SEPARATOR;
    }

    public static String getPathToConfigFile() {
        return getOplaUserHome() + "application.yaml";
    }
}
