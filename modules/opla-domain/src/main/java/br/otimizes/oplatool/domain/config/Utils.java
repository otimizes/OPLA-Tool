package br.otimizes.oplatool.domain.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.apache.commons.lang.WordUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.logging.Logger;

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
        try {
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
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * This method is used to get the tar file name from the gz file
     * by removing the .gz part from the input file
     *
     * @param inputFile
     * @param outputFolder
     * @return
     */
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

        Collections.sort(edsValues, byMapValues);

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

    /**
     * @param selectedExperiment
     * @param directoryToExportModels
     */
    public static String getProfilesUsedForSelectedExperiment(String selectedExperiment,
                                                              String directoryToExportModels) {
        try {
            String[] exts = {"uml"};
            StringBuilder names = new StringBuilder();

            StringBuilder path = new StringBuilder();
            path.append(directoryToExportModels);
            path.append(selectedExperiment);
            path.append("/resources/");
            System.out.println(path.toString());
            List<File> files = (List<File>) FileUtils.listFiles(new File(path.toString()), exts, false);

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

    public static void createPathsOplaTool() {
        UserHome.createDefaultOplaPathIfDontExists();

        Path pathApplicationYaml = Paths.get(UserHome.getOplaUserHome()).resolve(FileConstants.APPLICATION_YAML_NAME);

        if (!Files.exists(pathApplicationYaml)) {
            FileUtils.copy(FileConstants.LOCAL_YAML_PATH, pathApplicationYaml);
        }

        Utils.copyFileGuiSettings();

        UserHome.createProfilesPath();
        UserHome.createTemplatePath();
        UserHome.createOutputPath();
        UserHome.createBinsHVPath();
        UserHome.createTempPath(); // Manipulation dir. apenas para uso intenro

    }

    public static void copyFileGuiSettings() {
        Path target = Paths.get(UserHome.getOplaUserHome()).resolve(FileConstants.GUI_SETTINGS);
        if (!Files.exists(target)) {
            try {
                URI uri = ClassLoader.getSystemResource(FileConstants.BASE_RESOURCES + FileConstants.LOCAL_GUI_PATH).toURI();
                FileUtils.copy(Paths.get(uri.getSchemeSpecificPart()), target);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createDataBaseIfNotExists() {
        LOGGER.info("Verificando diretorio da base de dados");

        try {
            String pathEmptyDbFile = FileConstants.PATH_EMPTY_DB + FileConstants.FILE_SEPARATOR + FileConstants.EMPTY_DB_NAME;
            FileUtils.createDirectory(Paths.get(UserHome.getOplaUserHome() + FileConstants.DB_DIR));
//                FileUtils.copy(Paths.get(uri.getSchemeSpecificPart()), pathDb);
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
