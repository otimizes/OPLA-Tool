package br.otimizes.oplatool.core.jmetal4.metrics.objectivefunctions;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Package;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;
import br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion.LCCComponentResult;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Lack of Concern-based Cohesion (Santos et al., 2015)
 * <p>
 * It aims to measure the cohesion based on characteristics by means
 * of the number of characteristics with which a given package is
 * associated.
 * <p>
 * (LCC)
 */
public class LCC extends ObjectiveFunctionImplementation {

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
