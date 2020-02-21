package arquitetura.io;

import arquitetura.config.ApplicationYamlConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OPLAThreadScope {

    public static ThreadLocal<String> hash = ThreadLocal.withInitial(() -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = simpleDateFormat.format(new Date());


        return format.concat("-" + String.valueOf(Math.round(Math.random() * 10000)));
    });

    public static ThreadLocal<ApplicationYamlConfig> config = new ThreadLocal<>();

    public static void setConfig(ApplicationYamlConfig config) {
        config.setDirectoryToExportModels(config.getDirectoryToExportModels() + OPLAThreadScope.hash.get() + "/");
        config.setDirectoryToSaveModels(config.getDirectoryToSaveModels() + OPLAThreadScope.hash.get() + "/");
        OPLAThreadScope.config.set(config);
    }
}
