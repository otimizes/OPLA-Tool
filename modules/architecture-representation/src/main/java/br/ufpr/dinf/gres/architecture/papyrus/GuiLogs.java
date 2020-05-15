package br.ufpr.dinf.gres.architecture.papyrus;

import br.ufpr.dinf.gres.loglog.LogLog;

/**
 * GUI logger
 */
public class GuiLogs {

    private static LogLog logger;

    public static LogLog getLogger() {
        return logger;
    }

    public static void setLogger(LogLog ll) {
        logger = ll;
    }

}
