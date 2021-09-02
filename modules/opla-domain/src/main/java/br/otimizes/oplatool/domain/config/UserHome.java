package br.otimizes.oplatool.domain.config;

import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver;
import org.codehaus.plexus.logging.console.ConsoleLoggerManager;

import java.io.File;
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
     * @return OPLA User Home
     */
    public static String getOPLAUserHome() {
        return FileConstants.USER_HOME + FileConstants.FILE_SEPARATOR + FileConstants.BASE_DIR + FileConstants.FILE_SEPARATOR;
    }

    public static String getOPLAHV() {
        return FileConstants.USER_HOME + FileConstants.FILE_SEPARATOR + FileConstants.BASE_DIR + FileConstants.FILE_SEPARATOR + FileConstants.BINS_DIR +
                FileConstants.FILE_SEPARATOR + FileConstants.HV_DIR + FileConstants.FILE_SEPARATOR + FileConstants.HV_DIR;
    }

    public static Path getApplicationYamlConfig() {
        return Paths.get(getOPLAUserHome()).resolve(FileConstants.APPLICATION_YAML_NAME);
    }

    public static String getGuiSettingsFilePath() {
        return getOPLAUserHome() + FileConstants.GUI_SETTINGS;
    }

    public static void createDefaultOPLAPathIfDontExists() {
        FileUtils.createDirectory(Paths.get(getOPLAUserHome()));
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

        if (uriTemplatesDir != null) {
            FileUtils.copy(Paths.get(FileUtils.getURL(uriTemplatesDir)).resolve(simplesUmlPath), externalPathSimplesUml);
            FileUtils.copy(Paths.get(FileUtils.getURL(uriTemplatesDir)).resolve(simplesDiPath), externalPathSimplesDi);
            FileUtils.copy(Paths.get(FileUtils.getURL(uriTemplatesDir)).resolve(simplesNotationPath), externalPathSimplesNotation);
        }
    }

    public static void createProfilesPath() {
        Utils.createPath(getOPLAUserHome() + FileConstants.PROFILES_DIR + FileConstants.FILE_SEPARATOR);
    }

    public static void createBinsHVPath() {
        Utils.createPath(getOPLAUserHome() + FileConstants.BINS_DIR + FileConstants.FILE_SEPARATOR);
        URL systemResource = ClassLoader.getSystemResource(FileConstants.BASE_RESOURCES + FileConstants.HV_FILE);
        Path path = Paths.get(getOPLAUserHome() + FileConstants.BINS_DIR + FileConstants.FILE_SEPARATOR + FileConstants.HV_FILE);
        if (!path.toFile().exists()) {
            FileUtils.copy(Paths.get(systemResource.getPath()), path);
            File hvDir = new File(getOPLAUserHome() + FileConstants.BINS_DIR + FileConstants.FILE_SEPARATOR + FileConstants.HV_DIR);
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
        }
    }

    public static void createTemplatePath() {
        Utils.createPath(getOPLAUserHome() + FileConstants.TEMPLATES_DIR + FileConstants.FILE_SEPARATOR);
    }

    public static void createOutputPath() {
        Utils.createPath(getOPLAUserHome() + FileConstants.OUTPUT_DIR + FileConstants.FILE_SEPARATOR);
    }

    public static void createTempPath() {
        Utils.createPath(getOPLAUserHome() + FileConstants.TEMP_DIR + FileConstants.FILE_SEPARATOR);
    }

    public static String getPathToDb() {
        return getOPLAUserHome() + FileConstants.DB_DIR + FileConstants.FILE_SEPARATOR + FileConstants.EMPTY_DB_NAME;
    }

    public static String getPathToTemplates() {
        return getOPLAUserHome() + FileConstants.TEMPLATES_DIR + FileConstants.FILE_SEPARATOR;
    }

    public static String getPathToConfigFile() {
        return getOPLAUserHome() + FileConstants.APPLICATION_YAML_NAME;
    }
}
