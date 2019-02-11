package arquitetura.base;

import arquitetura.helpers.ModelHelper;
import arquitetura.helpers.ModelHelperFactory;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public abstract class ArchitectureHelper {

    private static ModelHelper modelHelper;

    protected ModelHelper getModelHelper() {
        modelHelper = ModelHelperFactory.getModelHelper();
        return modelHelper;
    }

}