package jmetal4.metrics.PLAMetrics.extensibility;

import arquitetura.representation.Variability;


public class ExtensVariabilityClass {

    private final Variability variability;

    public ExtensVariabilityClass(Variability variability) {
        this.variability = variability;
    }

    public float getValue() {
        float result = 0;
        result += new ExtensVarPointClass(variability.getVariationPoint()).getValue();
        return result;
    }
}
