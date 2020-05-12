package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Method;
import br.ufpr.dinf.gres.architecture.representation.Package;

import java.util.Collection;
import java.util.HashSet;

/**
 * Feature-based Cohesion for component result
 */
public class LCCComponentResult {

    private final HashSet<Concern> allConcerns = new HashSet<>();
    private final Package component;

    public LCCComponentResult(Package component) {
        this.component = component;
        addConcerns(component.getOwnConcerns());
        for (Interface i : component.getImplementedInterfaces()) {
            addConcerns(i.getOwnConcerns());

            for (Method method : i.getMethods()) {
                addConcerns(method.getOwnConcerns());
            }
        }
    }

    private void addConcerns(Collection<Concern> concerns) {
        allConcerns.addAll(concerns);
    }

    public int numberOfConcerns() {
        return allConcerns.size();
    }

    @Override
    public String toString() {
        return component.getName() + ": " + numberOfConcerns();
    }
}
