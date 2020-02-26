package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Package;


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
