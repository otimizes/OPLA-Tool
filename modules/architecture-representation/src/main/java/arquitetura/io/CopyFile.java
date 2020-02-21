package arquitetura.io;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class CopyFile {

    static Logger LOGGER = LogManager.getLogger(CopyFile.class.getName());

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