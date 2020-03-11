package br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class Elegance extends BaseMetricResults {

    public Elegance(Architecture architecture) {
        super(architecture);
        Double atmr = new ATMRElegance(architecture).getResults();
        Double ece = new ECElegance(architecture).getResults();
        Double nac = new NACElegance(architecture).getResults();
        this.setResults(atmr + ece + nac);
    }

}
