package br.otimizes.oplatool.core.jmetal4.metrics.extensibility;

import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;

/**
 * Variability Extensibility
 * <p>
 * Measures the extensibility of the variability
 */
public class ExtensVarComponent {

    private final Package component;

    public ExtensVarComponent(Package component) {
        this.component = component;
    }

    public float getValue() {
        float result = 0;
        for (Class class_ : component.getAllClasses())
            result += new ExtensClass(class_).getValue();

        return result;
    }
}
