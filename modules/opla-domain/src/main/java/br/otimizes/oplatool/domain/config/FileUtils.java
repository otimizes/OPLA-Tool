package br.otimizes.oplatool.domain.config;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    /**
     * Execute the create of directory
     *
     * @param path
     */
    public static void createDirectory(Path path) {
        try {
            if (!Files.exists(path)) {
                LOGGER.info("Creating directory..." + path);
                Files.createDirectory(path);
                LOGGER.info("Directory " + path + " created " + path);
            } else {
                LOGGER.info("Directory " + path + " already exists");
            }
        } catch (IOException e) {
            LOGGER.info("Directory home not found " + path);
        }
    }

    public static String getURL(URI uri) {
        String s = uri.getSchemeSpecificPart()
                .replaceAll("/", FileConstants.FILE_SEPARATOR.replace("\\", "\\\\"));
        if (s.startsWith("\\")) s = s.substring(1);
        return s;
    }

    public static void copy(Path source, Path target) {
        try {
            URL url = null;
            if (source.toString().contains("!")) {
                url = new URL("jar:" + source.toString());
                org.apache.commons.io.FileUtils.copyURLToFile(url, target.toFile());
            } else Files.copy(source, target);
            LOGGER.info("Copia de " + source + " concluída com sucesso");
        } catch (IOException e) {
            LOGGER.info("Cannot copy file: " + source);
        }
    }

    public static void copy(String fileName, Path target) {
        try {
            Path source = Paths.get(fileName);

            LOGGER.info("Copiando de: " + fileName + " para " + target);
            Files.copy(source, target);
            LOGGER.info("Copia concluída com sucesso");
        } catch (IOException e) {
            LOGGER.info("Cannot copy the file");
        }
    }

    public static void moveFiles(String to, String from) {
        InputStream inStream = null;
        OutputStream outStream = null;

        try {

            File afile = new File(to);
            File bfile = new File(from);

            inStream = new FileInputStream(afile);
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            // copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }

            inStream.close();
            outStream.flush();
            outStream.close();

            afile.delete();

            // LOGGER.info("File is copied to "+ to + " from: "+ from + "
            // successful!");

        } catch (IOException e) {
            LOGGER.info("Erros when copying files. Here are message error: " + e.getMessage());
        }
    }


    public static void copyFile(File source, File destFile) {

        LOGGER.info("copyFile(File sourceFile, File destFile) - Enter");
        LOGGER.info("SourceFile: " + source);
        LOGGER.info("DestFile: " + destFile);

        FileOutputStream outputStream = null;
        FileChannel inputChannel = null, outputChannel = null;
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(destFile);
            inputChannel = inputStream.getChannel();
            outputChannel = outputStream.getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        } catch (IOException e) {
            throw new RuntimeException("Cannot copy the file: "
                    + e.getMessage());
        } finally {
            try {
                inputStream.close();
                outputChannel.close();
                outputStream.close();
            } catch (IOException e) {
            }
        }

        LOGGER.info("copyFile(File sourceFile, File destFile) - Exit");

    }
}