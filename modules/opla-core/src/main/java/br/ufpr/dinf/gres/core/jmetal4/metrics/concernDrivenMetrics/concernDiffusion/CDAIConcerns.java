package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Concern;

/**
 * Concern Diffusion over Architectural Interfaces concerns
 */
public class CDAIConcerns extends ConcernDiffusionMetric<CDAIResult> {

    public CDAIConcerns(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAIResult getElementForConcern(Concern concern) {
        return new CDAIResult(concern, getArchitecture());
    }
}
