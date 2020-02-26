package br.ufpr.dinf.gres.patterns.util;

import br.ufpr.dinf.gres.architecture.representation.ParameterMethod;

public class ParameterMethodUtil {

    private ParameterMethodUtil() {
    }

    public static ParameterMethod cloneParameter(ParameterMethod parameter) {
        return new ParameterMethod(parameter.getName(), parameter.getType(), parameter.getDirection());
    }

}
