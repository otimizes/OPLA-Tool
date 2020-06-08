package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Concern;

/**
 *   Concern Diffusion over Architectural Components classes result
 */
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