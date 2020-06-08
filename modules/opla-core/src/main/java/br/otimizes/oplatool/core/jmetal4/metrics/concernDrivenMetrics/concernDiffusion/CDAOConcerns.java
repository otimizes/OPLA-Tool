package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;

/**
 * Concern Diffusion over Architectural operations concerns
 */
public class CDAOConcerns extends ConcernDiffusionMetric<CDAOResult> {

    public CDAOConcerns(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAOResult getElementForConcern(Concern concern) {
        return new CDAOResult(concern, getArchitecture());
    }
}
