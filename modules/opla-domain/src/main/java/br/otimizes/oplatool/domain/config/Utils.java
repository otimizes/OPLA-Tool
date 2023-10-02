package br.otimizes.oplatool.domain.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Logger;

import org.apache.commons.lang.WordUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * @author elf
 */
public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

    public static String extractSolutionIdFromSolutionFileName(String fileName) {
        return fileName.substring(fileName.indexOf("-") + 1);
    }

    public static String capitalize(String word) {
        return WordUtils.capitalize(word);
    }

    public static void copy(String source, String target) throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(source);
        try (FileOutputStream out = new FileOutputStream(target)) {
            byte[] buffer = new byte[1024];
            int len = in.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = in.read(buffer);
            }
        }
        LOGGER.info(String.format("File copy from %s to %s", source, target));
    }

    private static String getFileName(File inputFile, String outputFolder) {
        return outputFolder + File.separator +
                inputFile.getName().substring(0, inputFile.getName().lastIndexOf('.'));
    }

    public static void createPath(String uriPath) {
        FileUtils.createDirectory(Paths.get(uriPath));
    }

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }

        return ext;
    }

    public static boolean selectedSolutionIsNonDominated(String fileName) {
        return fileName.startsWith("VAR_All");
    }

    public static List<Map.Entry<String, Double>> shortMap(SortedMap<String, Double> resultsEds) {
        List<Map.Entry<String, Double>> edsValues = Lists.newArrayList(resultsEds.entrySet());
        Ordering<Map.Entry<String, Double>> byMapValues = new Ordering<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> left, Map.Entry<String, Double> right) {
                return left.getValue().compareTo(right.getValue());
            }
        };
        edsValues.sort(byMapValues);
        return edsValues;
    }

    public static boolean isDigit(String text) {
        try {
            Integer.parseInt(text);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String getProfilesUsedForSelectedExperiment(String selectedExperiment,
                                                              String directoryToExportModels) {
        try {
            String[] extensions = {"uml"};
            StringBuilder names = new StringBuilder();

            StringBuilder path = new StringBuilder();
            path.append(directoryToExportModels);
            path.append(selectedExperiment);
            path.append("/resources/");
            System.out.println(path.toString());
            List<File> files = (List<File>) FileUtils.listFiles(new File(path.toString()), extensions, false);

            for (File file : files) {
                names.append(file.getName().toLowerCase());
                names.append(", ");
            }

            return names.deleteCharAt(names.lastIndexOf(",")).toString().trim();
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return "-";
    }

    public static void createPathsOPLATool() {
        UserHome.createDefaultOPLAPathIfDontExists();
        Utils.copyFileApplicationYaml();
        Utils.copyFileGuiSettings();
        UserHome.createProfilesPath();
        UserHome.createTemplatePath();
        UserHome.createOutputPath();
        UserHome.createBinsHVPath();
        UserHome.createTempPath();

    }

    public static void copyFileApplicationYaml() {
        Path target = Paths.get(UserHome.getOPLAUserHome()).resolve(FileConstants.APPLICATION_YAML_NAME);
        if (!Files.exists(target)) {
            URL url = ClassLoader.getSystemResource(FileConstants.BASE_RESOURCES + FileConstants.LOCAL_YAML_PATH);
            FileUtils.copy(url, target);
        }
    }

    public static void copyFileGuiSettings() {
        Path target = Paths.get(UserHome.getOPLAUserHome()).resolve(FileConstants.GUI_SETTINGS);
        if (!Files.exists(target)) {
            URL url = ClassLoader.getSystemResource(FileConstants.BASE_RESOURCES + FileConstants.LOCAL_GUI_PATH);
            FileUtils.copy(url, target);
        }
    }

    public static void createDataBaseIfNotExists() {
        LOGGER.info("Verifying database");
        try {
            String pathEmptyDbFile = FileConstants.PATH_EMPTY_DB + FileConstants.FILE_SEPARATOR + FileConstants.EMPTY_DB_NAME;
            FileUtils.createDirectory(Paths.get(UserHome.getOPLAUserHome() + FileConstants.DB_DIR));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void executeCommand(String command) throws IOException {
        Runtime.getRuntime().exec(command);
    }

    public static void executePapyrus(String location, String plas) {
        try {
            LOGGER.info(location + " --launcher.openFile " + plas);
            Utils.executeCommand(location + " --launcher.openFile " + plas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
