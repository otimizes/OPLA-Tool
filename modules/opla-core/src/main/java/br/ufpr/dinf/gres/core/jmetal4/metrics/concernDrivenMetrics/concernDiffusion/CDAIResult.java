package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Package;


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
