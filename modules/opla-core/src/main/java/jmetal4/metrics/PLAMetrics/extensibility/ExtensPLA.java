package jmetal4.metrics.PLAMetrics.extensibility;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;

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
