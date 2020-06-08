package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Feature-based Cohesion for collection of component classes
 */
public class LCCClassCollection {

    private final Collection<LCCClassComponentResult> results = new ArrayList<>();

    public Collection<LCCClassComponentResult> getResults() {
        return results;
    }
}
