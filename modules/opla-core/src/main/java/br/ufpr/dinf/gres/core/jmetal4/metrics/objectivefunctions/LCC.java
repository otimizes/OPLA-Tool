package br.ufpr.dinf.gres.core.jmetal4.metrics.objectivefunctions;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Package;
import br.ufpr.dinf.gres.core.jmetal4.metrics.ObjectiveFunctionBase;
import br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Feature-based Cohesion (Santos et al., 2015)
 * <p>
 * It aims to measure the cohesion based on characteristics by means
 * of the number of characteristics with which a given package is
 * associated.
 * <p>
 * (LCC)
 */
public class LCC extends ObjectiveFunctionBase {

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
