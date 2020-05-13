package br.ufpr.dinf.gres.domain.config;

import java.nio.file.Path;
import java.nio.file.Paths;

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
        return FileConstants.USER_HOME + FileConstants.FILE_SEPARATOR + "oplatool" + FileConstants.FILE_SEPARATOR;
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
        Utils.createPath(getOplaUserHome() + "profiles" + FileConstants.FILE_SEPARATOR);
    }

    public static void createBinsHVPath() {
        Utils.createPath(getOplaUserHome() + "bins" + FileConstants.FILE_SEPARATOR);
//        Utils.createPath(getOplaUserHome() + "bins" + Constants.FILE_SEPARATOR + "hv" + Constants.FILE_SEPARATOR);
    }

    public static void createTemplatePath() {
        Utils.createPath(getOplaUserHome() + "templates" + FileConstants.FILE_SEPARATOR);
    }

    public static void createOutputPath() {
        Utils.createPath(getOplaUserHome() + "output" + FileConstants.FILE_SEPARATOR);
    }

    public static void createTempPath() {
        Utils.createPath(getOplaUserHome() + "temp" + FileConstants.FILE_SEPARATOR);
    }

    public static String getPathToDb() {
        return getOplaUserHome() + "db" + FileConstants.FILE_SEPARATOR + "oplatool.db";
    }

    public static String getPathToTemplates() {
        return getOplaUserHome() + "templates" + FileConstants.FILE_SEPARATOR;
    }

    public static String getPathToConfigFile() {
        return getOplaUserHome() + "application.yaml";
    }
}
