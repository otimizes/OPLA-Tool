package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Class;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;

public class LCCClass extends BaseMetricResults {

    public LCCClass(Architecture architecture) {
        super(architecture);
        double sumLCCClass = 0.0;
        LCCClassCollection result = new LCCClassCollection();
        for (Class cls : architecture.getAllClasses()) {
            result.getResults().add(new LCCClassComponentResult(cls));
        }

        for (LCCClassComponentResult cls : result.getResults()) {
            sumLCCClass += cls.numberOfConcerns();

        }
        this.setResults(sumLCCClass);
    }

}
