package br.ufpr.dinf.gres.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OPLAThreadScope {

    public static ThreadLocal<String> token = new ThreadLocal<>();

    public static ThreadLocal<String> hash = ThreadLocal.withInitial(() -> {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String format = simpleDateFormat.format(new Date());

        return OPLAThreadScope.token.get() + System.getProperty("file.separator") + format.concat("-" + Math.round(Math.random() * 10000));
    });

    public static ThreadLocal<Long> mainThreadId = new ThreadLocal<>();

    public static ThreadLocal<Integer> currentGeneration = new ThreadLocal<>();
}
