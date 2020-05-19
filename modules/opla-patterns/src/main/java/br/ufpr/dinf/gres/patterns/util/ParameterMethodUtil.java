package br.ufpr.dinf.gres.patterns.util;

import br.ufpr.dinf.gres.architecture.representation.ParameterMethod;

/**
 * The Class ParameterMethodUtil.
 */
public class ParameterMethodUtil {

    /**
     * Instantiates a new parameter method util.
     */
    private ParameterMethodUtil() {
    }

    /**
     * Clone parameter.
     *
     * @param parameter the parameter
     * @return the parameter method
     */
    public static ParameterMethod cloneParameter(ParameterMethod parameter) {
        return new ParameterMethod(parameter.getName(), parameter.getType(), parameter.getDirection());
    }

}
