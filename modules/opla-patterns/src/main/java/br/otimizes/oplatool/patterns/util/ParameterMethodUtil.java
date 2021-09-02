package br.otimizes.oplatool.patterns.util;

import br.otimizes.oplatool.architecture.representation.ParameterMethod;

/**
 * The Class ParameterMethodUtil.
 */
public class ParameterMethodUtil {

    private ParameterMethodUtil() {
    }

    public static ParameterMethod cloneParameter(ParameterMethod parameter) {
        return new ParameterMethod(parameter.getName(), parameter.getType(), parameter.getDirection());
    }
}
