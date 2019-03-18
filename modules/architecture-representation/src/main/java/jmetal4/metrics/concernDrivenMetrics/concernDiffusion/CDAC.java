package jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;

public class CDAC extends ConcernDiffusionMetric<CDACResult> {

    public CDAC(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDACResult getElementForConcern(Concern concern) {
        return new CDACResult(concern, getArchitecture());
    }
}
