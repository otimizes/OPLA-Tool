package br.ufpr.dinf.gres.domain.view.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import br.ufpr.dinf.gres.architecture.io.FileUtils;

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
        return br.ufpr.dinf.gres.domain.view.util.Constants.USER_HOME + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR + "oplatool" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR;
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
        br.ufpr.dinf.gres.domain.view.util.Utils.createPath(getOplaUserHome() + "profiles" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR);
    }

    public static void createBinsHVPath() {
        br.ufpr.dinf.gres.domain.view.util.Utils.createPath(getOplaUserHome() + "bins" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR);
//        Utils.createPath(getOplaUserHome() + "bins" + Constants.FILE_SEPARATOR + "hv" + Constants.FILE_SEPARATOR);
    }

    public static void createTemplatePath() {
        br.ufpr.dinf.gres.domain.view.util.Utils.createPath(getOplaUserHome() + "templates" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR);
    }

    public static void createOutputPath() {
        br.ufpr.dinf.gres.domain.view.util.Utils.createPath(getOplaUserHome() + "output" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR);
    }

    public static void createTempPath() {
        br.ufpr.dinf.gres.domain.view.util.Utils.createPath(getOplaUserHome() + "temp" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR);
    }

    public static String getPathToDb() {
        return getOplaUserHome() + "db" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR + "oplatool.db";
    }
    
    public static String getPathToTemplates() {
    	return getOplaUserHome() + "templates" + br.ufpr.dinf.gres.domain.view.util.Constants.FILE_SEPARATOR;
    }

    public static String getPathToConfigFile() {
        return getOplaUserHome() + "application.yaml";
    }
}
