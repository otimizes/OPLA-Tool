package br.otimizes.oplatool.architecture.helpers;


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * Configure log level. If you dont wanna log anything on "production" use: {@code setLogLevel(Level.DEBUG}
 *
 * @author elf
 */
public class LogConfiguration {

    public static void setLogLevel(Level level) {

        @SuppressWarnings("unchecked")
        Enumeration<Logger> currentLoggers = LogManager.getCurrentLoggers();
        ArrayList<Logger> list = Collections.list(currentLoggers);
        List<Logger> loggers = list;
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers) {
            logger.setLevel(level);
        }
    }

}
