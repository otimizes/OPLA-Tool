package br.ufpr.dinf.gres.core.jmetal4.metrics.PLAMetrics.extensibility;

import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Package;

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
