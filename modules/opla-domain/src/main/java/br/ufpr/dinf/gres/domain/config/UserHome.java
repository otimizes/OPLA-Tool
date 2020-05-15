package br.ufpr.dinf.gres.domain.config;

import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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

    public static void copyTemplates() {
        URI uriTemplatesDir = null;
        try {
            uriTemplatesDir = ClassLoader.getSystemResource(FileConstants.BASE_RESOURCES + FileConstants.TEMPLATES_DIR).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String simplesUmlPath = FileConstants.SIMPLES_UML_NAME;
        String simplesDiPath = FileConstants.SIMPLES_DI_NAME;
        String simplesNotationPath = FileConstants.SIMPLES_NOTATION_NAME;

        Path externalPathSimplesUml = Paths.get(UserHome.getPathToTemplates() + simplesUmlPath);
        Path externalPathSimplesDi = Paths.get(UserHome.getPathToTemplates() + simplesDiPath);
        Path externalPathSimplesNotation = Paths.get(UserHome.getPathToTemplates() + simplesNotationPath);

        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesUmlPath), externalPathSimplesUml);
        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesDiPath), externalPathSimplesDi);
        FileUtils.copy(Paths.get(uriTemplatesDir.getSchemeSpecificPart()).resolve(simplesNotationPath), externalPathSimplesNotation);
    }

    public static void createProfilesPath() {
        Utils.createPath(getOplaUserHome() + "profiles" + FileConstants.FILE_SEPARATOR);
    }

    public static void createBinsHVPath() {
        Utils.createPath(getOplaUserHome() + FileConstants.BINS_DIR + FileConstants.FILE_SEPARATOR);
        URL systemResource = ClassLoader.getSystemResource(FileConstants.BASE_RESOURCES + FileConstants.HV_FILE);
        try {
            Path path = Paths.get(getOplaUserHome() + FileConstants.BINS_DIR + FileConstants.FILE_SEPARATOR + FileConstants.HV_FILE);
            FileUtils.copy(Paths.get(systemResource.toURI()), path);
            File hvDir = new File(getOplaUserHome() + FileConstants.BINS_DIR + FileConstants.FILE_SEPARATOR + FileConstants.HV_DIR);
            boolean mkdirs = hvDir.mkdirs();

            if (mkdirs) {
                final TarGZipUnArchiver ua = new TarGZipUnArchiver();
                ConsoleLoggerManager manager = new ConsoleLoggerManager();
                manager.initialize();
                ua.enableLogging(manager.getLoggerForComponent("bla"));
                ua.setSourceFile(path.toFile());
                ua.setDestDirectory(hvDir);
                ua.extract();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
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
