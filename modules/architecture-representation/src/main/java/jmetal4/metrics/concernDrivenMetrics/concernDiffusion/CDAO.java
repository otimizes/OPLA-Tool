package jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;


public class CDAO extends ConcernDiffusionMetric<CDAOResult> {

    public CDAO(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAOResult getElementForConcern(Concern concern) {
        return new CDAOResult(concern, getArchitecture());
    }
}
