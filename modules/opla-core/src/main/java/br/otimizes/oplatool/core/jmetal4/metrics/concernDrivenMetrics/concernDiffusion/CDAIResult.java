package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Package;

/**
 * Concern Diffusion over Architectural Interfaces result
 */
public class CDAIResult extends ConcernDiffusionResult<Interface> {

    public CDAIResult(Concern concern, Architecture architecture) {
        super(concern, architecture);
    }

    private void inspectInterfaces(Package component) {
        for (Interface i : component.getImplementedInterfaces())
            addInterface(i);

        for (Interface i : component.getRequiredInterfaces())
            addInterface(i);
    }

    private void addInterface(Interface i) {
        if (interfaceContainsConcern(i) || interfaceContainsConcernViaOperation(i))
            getElements().add(i);
    }

    @Override
    protected void loadElements(Architecture architecture) {
        for (Package component : architecture.getAllPackages()) {
            if (componentContainsConcern(component))
                getElements().addAll(component.getImplementedInterfaces());
            else {
                inspectInterfaces(component);
            }
        }
    }
}
