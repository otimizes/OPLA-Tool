package jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;

import java.util.ArrayList;
import java.util.Collection;


public class LCC {

    private final Architecture architecture;
    private final Collection<LCCComponentResult> results = new ArrayList<LCCComponentResult>();

    public LCC(Architecture architecture) {
        this.architecture = architecture;
        for (Package component : architecture.getAllPackages()) {
            getResults().add(new LCCComponentResult(component));
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public Collection<LCCComponentResult> getResults() {
        return results;
    }
}
