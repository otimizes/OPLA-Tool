package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;

public class CDAClass extends ConcernDiffusionMetric<CDAClassResult> {

    public CDAClass(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAClassResult getElementForConcern(Concern concern) {
        return new CDAClassResult(concern, getArchitecture());
    }
}
