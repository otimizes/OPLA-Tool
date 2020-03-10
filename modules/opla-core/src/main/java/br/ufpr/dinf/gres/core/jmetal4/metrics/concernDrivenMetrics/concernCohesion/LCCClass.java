package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import java.util.ArrayList;
import java.util.Collection;

public class LCCClass {

    private final Collection<LCCClassComponentResult> results = new ArrayList<>();

    public Collection<LCCClassComponentResult> getResults() {
        return results;
    }
}
