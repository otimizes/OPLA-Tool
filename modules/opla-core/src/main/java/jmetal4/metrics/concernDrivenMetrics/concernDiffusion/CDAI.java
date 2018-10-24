package jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;

public class CDAI extends ConcernDiffusionMetric<CDAIResult> {

    public CDAI(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAIResult getElementForConcern(Concern concern) {
        return new CDAIResult(concern, getArchitecture());
    }
}
