package br.ufpr.dinf.gres.core.jmetal4.metrics.extensibility;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class PLAExtensibility extends BaseMetricResults {

    public PLAExtensibility(Architecture architecture) {
        super(architecture);
        double ExtensibilityFitness = 0;
        double Extensibility;
        PLAExtensibility PLAExtens = new PLAExtensibility(architecture);
        ExtensibilityFitness = PLAExtens.getValue(architecture);
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
