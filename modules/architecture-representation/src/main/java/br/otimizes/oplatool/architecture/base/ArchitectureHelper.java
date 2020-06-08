package br.otimizes.oplatool.architecture.base;

import br.otimizes.oplatool.architecture.helpers.ModelHelper;
import br.otimizes.oplatool.architecture.helpers.ModelHelperFactory;

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