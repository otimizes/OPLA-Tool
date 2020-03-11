package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;

public class CDAClassConcerns extends ConcernDiffusionMetric<CDAClassResult> {

    public CDAClassConcerns(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAClassResult getElementForConcern(Concern concern) {
        return new CDAClassResult(concern, getArchitecture());
    }
}
