package jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import arquitetura.representation.*;
import arquitetura.representation.Package;

import java.util.HashSet;
import java.util.Set;


public class CIBCResult {

    private final Concern concern;
    private final Set<Concern> interlacedConcerns = new HashSet<Concern>();

    public CIBCResult(Concern concern, Element context, Package component) {
        this.concern = concern;
        addInterlacedConcerns(context, component);
    }

    public void addInterlacedConcerns(Element context, Package component) {
        if (context instanceof Package)
            inspectComponent(component);
        else if (context instanceof Interface) {
            inspectImplementedInterfaces(component);
            inspectRequiredInterfaces(component);
        } else
            inspectOperations(component);

        interlacedConcerns.remove(concern);
    }

    private void inspectComponent(Package component) {
        getInterlacedConcerns().addAll(component.getOwnConcerns());
        inspectImplementedInterfaces(component);
        inspectRequiredInterfaces(component);
    }

    private void inspectImplementedInterfaces(Package component) {
        for (Interface i : component.getImplementedInterfaces()) {
            getInterlacedConcerns().addAll(i.getOwnConcerns());
            inspectOperations(i);
        }
    }

    private void inspectRequiredInterfaces(Package component) {
        for (Interface i : component.getRequiredInterfaces()) {
            getInterlacedConcerns().addAll(i.getOwnConcerns());
            inspectOperations(i);
        }
    }

    private void inspectOperations(Interface i) {
        for (Method operation : i.getOperations()) {
            getInterlacedConcerns().addAll(operation.getOwnConcerns());
        }
    }

    private void inspectOperations(Package component) {
        for (Interface i : component.getImplementedInterfaces()) {
            inspectOperations(i);
        }
        for (Interface i : component.getRequiredInterfaces()) {
            inspectOperations(i);
        }
    }

    public Concern getConcern() {
        return concern;
    }

    @Override
    public String toString() {
        return concern.getName() + ": " + getInterlacedConcerns().size();
    }

    public Set<Concern> getInterlacedConcerns() {
        return interlacedConcerns;
    }
}
