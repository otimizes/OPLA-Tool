package jmetal4.metrics.PLAMetrics.extensibility;

import arquitetura.representation.Class;
import arquitetura.representation.Package;

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
