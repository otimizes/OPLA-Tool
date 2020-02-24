package arquitetura.base;

import arquitetura.helpers.ModelHelper;
import arquitetura.helpers.ModelHelperFactory;

/**
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class ArchitectureHelper {

    protected ModelHelper getModelHelper() {
        return ModelHelperFactory.getModelHelper();
    }

}