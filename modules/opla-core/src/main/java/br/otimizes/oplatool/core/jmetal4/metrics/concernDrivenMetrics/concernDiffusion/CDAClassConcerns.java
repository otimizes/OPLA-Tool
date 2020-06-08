package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;

/**
 *   Concern Diffusion over Architectural Components classes concerns
 */
public class CDAClassConcerns extends ConcernDiffusionMetric<CDAClassResult> {

    public CDAClassConcerns(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDAClassResult getElementForConcern(Concern concern) {
        return new CDAClassResult(concern, getArchitecture());
    }
}
