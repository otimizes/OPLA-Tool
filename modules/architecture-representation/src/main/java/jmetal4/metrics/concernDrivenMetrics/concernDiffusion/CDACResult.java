package jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;
import arquitetura.representation.Package;


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
