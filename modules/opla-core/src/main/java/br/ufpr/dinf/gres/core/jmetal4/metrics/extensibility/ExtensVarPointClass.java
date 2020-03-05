package br.ufpr.dinf.gres.core.jmetal4.metrics.extensibility;

import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Variant;
import br.ufpr.dinf.gres.architecture.representation.VariationPoint;

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
