package br.ufpr.dinf.gres.architecture.base;

import br.ufpr.dinf.gres.architecture.helpers.ModelHelper;
import br.ufpr.dinf.gres.architecture.helpers.ModelHelperFactory;

/**
 * Class that contain assist architecture methods
 *
 * @author edipofederle<edipofederle @ gmail.com>
 */
public abstract class ArchitectureHelper {

    protected ModelHelper getModelHelper() {
        return ModelHelperFactory.getModelHelper();
    }

}