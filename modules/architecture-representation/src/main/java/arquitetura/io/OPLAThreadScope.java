package arquitetura.io;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OPLAThreadScope {

    public static ThreadLocal<String> hash = ThreadLocal.withInitial(() -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = simpleDateFormat.format(new Date());

        return format.concat("-" + String.valueOf(Math.round(Math.random() * 10000)));
    });

    public static ThreadLocal<String> pathToProfile = new ThreadLocal<>(); //Smarty
    public static ThreadLocal<String> pathToProfileConcern = new ThreadLocal<>();
    public static ThreadLocal<String> pathToProfileRelationships = new ThreadLocal<>();
    public static ThreadLocal<String> pathToProfilePatterns = new ThreadLocal<>();
    public static ThreadLocal<String> pathToTemplateModelsDirectory = new ThreadLocal<>();
    public static ThreadLocal<String> directoryToSaveModels = new ThreadLocal<>();
    public static ThreadLocal<String> directoryToInteraction = new ThreadLocal<>();
    public static ThreadLocal<String> pathLastOptimizationInput = new ThreadLocal<>();
    public static ThreadLocal<String> pathPapyrus = new ThreadLocal<>();
    public static ThreadLocal<String> directoryToExportModels = new ThreadLocal<>();
}
