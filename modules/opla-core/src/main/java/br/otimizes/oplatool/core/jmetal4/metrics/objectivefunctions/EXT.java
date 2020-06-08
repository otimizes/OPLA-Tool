package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.extensibility.ExtensVarComponent;

/**
 * PLA Extensibility
 *
 * It aims to indicate the degree of extensibility of the LPS.
 * (1 / ExtensPLA)
 */
public class EXT extends ObjectiveFunctionImplementation {

    public EXT(Architecture architecture) {
        super(architecture);
        double ExtensibilityFitness = 0;
        double Extensibility;
        ExtensibilityFitness = getValue(architecture);
        if (ExtensibilityFitness == 0)
            Extensibility = 1000;
        else
            Extensibility = 1 / ExtensibilityFitness;
        this.setResults(Extensibility);
    }

    public float getValue(Architecture architecture) {
        float result = 0;
        for (Package component : architecture.getAllPackages())
            result += new ExtensVarComponent(component).getValue();

        return result;
    }
}
