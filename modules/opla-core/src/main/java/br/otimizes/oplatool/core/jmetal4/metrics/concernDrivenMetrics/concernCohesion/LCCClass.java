package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.core.jmetal4.metrics.ObjectiveFunctionImplementation;

/**
 * Feature-based Cohesion for class
 */
public class LCCClass extends ObjectiveFunctionImplementation {

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
