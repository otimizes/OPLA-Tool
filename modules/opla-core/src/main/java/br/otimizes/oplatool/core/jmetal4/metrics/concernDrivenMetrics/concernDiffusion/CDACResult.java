package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Package;

/**
 *   Concern Diffusion over classes result
 */
public class CDACResult extends ConcernDiffusionResult<Package> {

    public CDACResult(Concern concern, Architecture architecture) {
        super(concern, architecture);
    }

    @Override
    protected void loadElements(Architecture architecture) {
        for (Package component : architecture.getAllPackages()) {
            if (componentContainsConcern(component) || componentContaisConcernViaInterface(component))
                getElements().add(component);
        }
    }
}
