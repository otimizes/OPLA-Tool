package jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Concern;

import java.util.ArrayList;


public abstract class ConcernDiffusionMetric<T> {
    private final Architecture architecture;
    private final ArrayList<T> results = new ArrayList<T>();

    public ConcernDiffusionMetric(Architecture architecture) {
        this.architecture = architecture;

        for (Concern concern : architecture.getAllConcerns()) {
            getResults().add(getElementForConcern(concern));
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public ArrayList<T> getResults() {
        return results;
    }

    protected abstract T getElementForConcern(Concern concern);
}
