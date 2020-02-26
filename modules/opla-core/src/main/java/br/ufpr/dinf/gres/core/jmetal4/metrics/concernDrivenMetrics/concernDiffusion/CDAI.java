package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;

public class CDAI extends ConcernDiffusionMetric<CDAIResult> {

    public CDAI(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAIResult getElementForConcern(Concern concern) {
        return new CDAIResult(concern, getArchitecture());
    }
}
