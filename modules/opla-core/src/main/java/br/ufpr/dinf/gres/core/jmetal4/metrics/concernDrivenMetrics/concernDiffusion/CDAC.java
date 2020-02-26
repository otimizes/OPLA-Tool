package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;

public class CDAC extends ConcernDiffusionMetric<CDACResult> {

    public CDAC(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDACResult getElementForConcern(Concern concern) {
        return new CDACResult(concern, getArchitecture());
    }
}
