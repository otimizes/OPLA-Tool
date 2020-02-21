package arquitetura.io;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

public class OPLAThreadScope {

    public static ThreadLocal<String> hash = ThreadLocal.withInitial(() -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = simpleDateFormat.format(new Date());

        return format.concat("-"+String.valueOf(Math.round(Math.random() * 10000)));
    });
}
