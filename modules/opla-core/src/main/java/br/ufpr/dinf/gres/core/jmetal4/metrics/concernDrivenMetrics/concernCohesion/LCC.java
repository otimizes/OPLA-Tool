package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import java.util.ArrayList;
import java.util.Collection;

public class LCC {

    private final Collection<LCCComponentResult> results = new ArrayList<>();

    public Collection<LCCComponentResult> getResults() {
        return results;
    }
}
