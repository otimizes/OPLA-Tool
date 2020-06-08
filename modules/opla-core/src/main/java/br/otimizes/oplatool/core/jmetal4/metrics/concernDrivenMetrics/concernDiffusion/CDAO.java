package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Concern Diffusion over Architectural Operations (Sant'Anna, 2008)
 *
 * Measures the number of operations that contribute for realization of a interesting
 *
 */
public class CDAO extends ObjectiveFunctionImplementation {

    public CDAO(Architecture architecture) {
        super(architecture);
        double sumCDAO = 0.0;
        CDAOConcerns cdao = new CDAOConcerns(architecture);
        for (CDAOResult c : cdao.getResults()) {
            sumCDAO += c.getElements().size();
        }
        this.setResults(sumCDAO);
    }

}
