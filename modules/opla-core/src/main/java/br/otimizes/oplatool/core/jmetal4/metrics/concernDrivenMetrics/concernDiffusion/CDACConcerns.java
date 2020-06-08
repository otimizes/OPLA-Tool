package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Concern;

/**
 * Concern Diffusion over Architectural Components result
 *
 */
public class CDACConcerns extends ConcernDiffusionMetric<CDACResult> {

    public CDACConcerns(Architecture architecture) {
        super(architecture);
    }

    @Override
    protected CDACResult getElementForConcern(Concern concern) {
        return new CDACResult(concern, getArchitecture());
    }
}
