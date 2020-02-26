package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.architecture.representation.Concern;

public class CDAClassResult extends ConcernDiffusionResult<Class> {

    public CDAClassResult(Concern concern, Architecture architecture) {
        super(concern, architecture);
    }

    @Override
    protected void loadElements(Architecture architecture) {
        for (Class cls : architecture.getAllClasses()) {
            if (classContainsConcern(cls) || (classContainsConcernViaMethod(cls)))
                getElements().add(cls);
        }
    }

}