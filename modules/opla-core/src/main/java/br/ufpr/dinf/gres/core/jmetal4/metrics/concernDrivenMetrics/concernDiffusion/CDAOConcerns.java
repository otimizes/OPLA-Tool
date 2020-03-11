package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;


public class CDAOConcerns extends ConcernDiffusionMetric<CDAOResult> {

    public CDAOConcerns(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAOResult getElementForConcern(Concern concern) {
        return new CDAOResult(concern, getArchitecture());
    }
}
