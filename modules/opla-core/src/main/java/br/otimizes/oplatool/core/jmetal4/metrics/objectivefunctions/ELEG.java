package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.ATMRElegance;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.ECElegance;
import br.otimizes.oplatool.core.jmetal4.metrics.conventionalMetrics.NACElegance;

/**
 * Elegance
 * <p>
 * Aims to provide indicators of the elegance of object-oriented design.
 * (NAC + EC + ATMR)
 */
public class ELEG extends ObjectiveFunctionImplementation {

    public ELEG(Architecture architecture) {
        super(architecture);
        Double atmr = new ATMRElegance(architecture).getResults();
        Double ece = new ECElegance(architecture).getResults();
        Double nac = new NACElegance(architecture).getResults();
        this.setResults(atmr + ece + nac);
    }

}
