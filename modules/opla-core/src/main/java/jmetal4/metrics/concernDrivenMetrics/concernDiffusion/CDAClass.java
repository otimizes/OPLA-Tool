package jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;

public class CDAClass extends ConcernDiffusionMetric<CDAClassResult> {

    public CDAClass(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAClassResult getElementForConcern(Concern concern) {
        return new CDAClassResult(concern, getArchitecture());
    }
}
