package br.otimizes.oplatool.domain;

import br.otimizes.oplatool.domain.config.ApplicationFileConfig;
import br.otimizes.oplatool.domain.config.ApplicationYamlConfig;
import br.otimizes.oplatool.domain.config.FileConstants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OPLAThreadScope {

    public static ThreadLocal<String> token = new ThreadLocal<>();

    public static ThreadLocal<String> hash = ThreadLocal.withInitial(() -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = simpleDateFormat.format(new Date());

        Long mainThread = OPLAThreadScope.mainThreadId.get();
        return OPLAThreadScope.token.get() + FileConstants.FILE_SEPARATOR + mainThread;
    });

    public static ThreadLocal<Long> mainThreadId = new ThreadLocal<>();

    public static ThreadLocal<Integer> currentGeneration = new ThreadLocal<>();

    public static ThreadLocal<ApplicationYamlConfig> config = ThreadLocal.withInitial(ApplicationFileConfig::getInstance);

    public static ThreadLocal<String> userDir = new ThreadLocal<>();
    public static ThreadLocal<String> pla = new ThreadLocal<>();

    public static void setConfig(ApplicationYamlConfig config) {
        config.setDirectoryToExportModels(config.getDirectoryToExportModels() + OPLAThreadScope.hash.get() + FileConstants.FILE_SEPARATOR);
        config.setDirectoryToSaveModels(config.getDirectoryToSaveModels() + OPLAThreadScope.hash.get() + FileConstants.FILE_SEPARATOR);
        OPLAThreadScope.config.set(config);
    }
}
