package br.ufpr.dinf.gres.core.jmetal4.metrics.classical;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.BaseMetricResults;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyIn;
import br.ufpr.dinf.gres.core.jmetal4.metrics.conventionalMetrics.ClassDependencyOut;

import java.util.ArrayList;
import java.util.Collection;

public class LCC extends BaseMetricResults {

    public LCC(Architecture architecture) {
        super(architecture);
        double sumLCC = 0.0;
        Collection<LCCComponentResult> results = new ArrayList<>();
        for (Package component : architecture.getAllPackages()) {
            results.add(new LCCComponentResult(component));
        }

        for (LCCComponentResult component : results) {
            sumLCC += component.numberOfConcerns();

        }
        this.setResults(sumLCC);
    }

}
