package br.ufpr.dinf.gres.domain.config;

import java.io.*;
import java.net.URL;
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
                LOGGER.info("Criando diretório..." + path);
                Files.createDirectory(path);
                LOGGER.info("Diretório " + path + " criado " + path);
            } else {
                LOGGER.info("Diretório " + path + " já existente");
            }
        } catch (IOException e) {
            LOGGER.info("Não foi possível criar o diretório home" + path);
        }
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
            LOGGER.info("Não foi possível copiar o arquivo: " + source);
        }
    }

    public static void copy(String fileName, Path target) {
        try {
            Path source = Paths.get(fileName);

            LOGGER.info("Copiando de: " + fileName + " para " + target);
            Files.copy(source, target);
            LOGGER.info("Copia concluída com sucesso");
        } catch (IOException e) {
            LOGGER.info("Não foi possível copiar o arquivo");
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
}