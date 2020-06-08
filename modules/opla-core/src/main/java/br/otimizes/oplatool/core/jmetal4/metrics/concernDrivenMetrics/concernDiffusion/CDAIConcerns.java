package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;

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
