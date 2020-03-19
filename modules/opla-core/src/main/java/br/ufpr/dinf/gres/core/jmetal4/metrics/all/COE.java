package br.ufpr.dinf.gres.core.jmetal4.metrics.all;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;

public class COE extends BaseMetricResults {

    public COE(Architecture architecture) {
        super(architecture);
        RelationalCohesion rc = new RelationalCohesion(architecture);
        this.setResults(rc.getResults() );
    }

}
