package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;

public class COE extends BaseMetricResults {

    public COE(Architecture architecture) {
        super(architecture);
        double coeFitness = 0.0;

        RelationalCohesion rc = new RelationalCohesion(architecture);

        LCC lcc = new LCC(architecture);

        coeFitness = rc.getResults() + lcc.getResults();
        this.setResults(coeFitness);
    }
}
