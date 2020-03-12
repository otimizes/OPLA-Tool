package br.ufpr.dinf.gres.core.jmetal4.metrics.all;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ATMRElegance;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ECElegance;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.NACElegance;

public class Elegance extends BaseMetricResults {

    public Elegance(Architecture architecture) {
        super(architecture);
        Double atmr = new ATMRElegance(architecture).getResults();
        Double ece = new ECElegance(architecture).getResults();
        Double nac = new NACElegance(architecture).getResults();
        this.setResults(atmr + ece + nac);
    }

}
