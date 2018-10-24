package arquitetura.main;

import br.ufpr.dinf.gres.loglog.LogLog;

public class GuiLogs {

    private static LogLog logger;

    public static LogLog getLogger() {
        return logger;
    }

    public static void setLogger(LogLog ll) {
        logger = ll;
    }

}
