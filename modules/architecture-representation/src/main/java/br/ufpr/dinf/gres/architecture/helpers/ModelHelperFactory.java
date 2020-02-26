package br.ufpr.dinf.gres.architecture.helpers;

import org.apache.log4j.Logger;

/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public class ModelHelperFactory {
    private static final Logger LOGGER = Logger.getLogger(ModelHelperFactory.class);

    private static ThreadLocal<ModelHelper> instance = new ThreadLocal<>();

    public static ModelHelper getModelHelper() {
        if (instance.get() == null) {
            instance.set(new ModelHelper());
        }

        return instance.get();
    }
}