package jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.*;
import arquitetura.representation.Class;
import arquitetura.representation.Package;

import java.util.HashSet;
import java.util.Set;

public abstract class ConcernDiffusionResult<T extends Element> {

    private final Concern concern;
    private final Set<T> elements = new HashSet<T>();

    public ConcernDiffusionResult(Concern concern, Architecture architecture) {
        this.concern = concern;
        loadElements(architecture);
    }

    protected boolean componentContainsConcern(Package component) {
        return component.containsConcern(concern);
    }

    protected boolean componentContaisConcernViaInterface(Package component) {
        for (Interface i : component.getImplementedInterfaces()) {
            if (interfaceContainsConcern(i) || interfaceContainsConcernViaOperation(i))
                return true;
        }
        return false;
    }

    protected boolean interfaceContainsConcern(Interface i) {
        return i.containsConcern(concern);
    }

    protected boolean interfaceContainsConcernViaOperation(Interface i) {
        for (Method operation : i.getOperations()) {
            if (operation.containsConcern(concern))
                return true;
        }
        return false;
    }

    public Concern getConcern() {
        return concern;
    }

    public Set<T> getElements() {
        return elements;
    }

    protected boolean classContainsConcern(Class cls) {
        return cls.containsConcern(concern);
    }

    protected boolean classContainsConcernViaMethod(Class cls) {
        for (Method method : cls.getAllMethods()) {
            if (method.containsConcern(concern))
                return true;
        }
        return false;
    }

    protected boolean classContainsConcernViaInterface(Class cls) {
        for (Interface i : cls.getImplementedInterfaces()) {
            if (interfaceContainsConcern(i) || interfaceContainsConcernViaOperation(i))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getConcern().getName() + ": " + elements.size();
    }

    protected abstract void loadElements(Architecture architecture);
}
