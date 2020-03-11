package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyOut;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.RelationalCohesion;

public class COE extends BaseMetricResults {

    public COE(Architecture architecture) {
        super(architecture);
        double sumCohesion = 0.0;
        double Cohesion = 0.0;

        RelationalCohesion cohesion = new RelationalCohesion(architecture);
        sumCohesion = cohesion.getResults();
        Cohesion = (1 / sumCohesion);
        this.setResults(Cohesion);
    }

}
