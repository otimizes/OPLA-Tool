package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.concernDiffusion;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract concern diffusion result
 * @param <T> type of element
 */
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
        for (Method operation : i.getMethods()) {
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
