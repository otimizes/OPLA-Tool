package jmetal4.metrics.PLAMetrics.extensibility;

import arquitetura.representation.Class;
import arquitetura.representation.Variant;
import arquitetura.representation.VariationPoint;

public class ExtensVarPointClass {

    private VariationPoint variationPoint;

    public ExtensVarPointClass(VariationPoint variationPoint) {
        this.variationPoint = variationPoint;
    }

    public float getValue() {
        if (!(variationPoint.getVariationPointElement() instanceof Class)) return 0;

        Class variationPointElement = (Class) variationPoint.getVariationPointElement();
        float result = new ExtensClass(variationPointElement).getValue();

        for (Variant variant : variationPoint.getVariants())
            result += new ExtensClass((Class) variant.getVariantElement()).getValue();

        return result;
    }
}
