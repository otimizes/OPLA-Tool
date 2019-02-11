package jmetal4.metrics.concernDrivenMetrics.concernCohesion;

import arquitetura.representation.Concern;
import arquitetura.representation.Interface;
import arquitetura.representation.Method;
import arquitetura.representation.Package;

import java.util.Collection;
import java.util.HashSet;

public class LCCComponentResult {

    private final HashSet<Concern> allConcerns = new HashSet<Concern>();
    private final Package component;

    public LCCComponentResult(Package component) {
        this.component = component;
        addConcerns(component.getOwnConcerns());
        for (Interface i : component.getImplementedInterfaces()) {
            addConcerns(i.getOwnConcerns());

            for (Method method : i.getOperations()) {
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
