package br.ufpr.dinf.gres.core.jmetal4.metrics.PLAMetrics.extensibility;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;

public class ExtensPLA {

    private final Architecture architecture;

    public ExtensPLA(Architecture architecture) {
        this.architecture = architecture;
    }

    public float getValue() {
        float result = 0;
        for (Package component : architecture.getAllPackages())
            result += new ExtensVarComponent(component).getValue();

        return result;
    }
}
