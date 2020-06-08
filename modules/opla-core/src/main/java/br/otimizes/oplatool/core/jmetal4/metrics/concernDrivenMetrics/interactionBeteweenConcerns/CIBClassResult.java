package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Class;
import br.otimizes.oplatool.architecture.representation.Package;

import java.util.HashSet;
import java.util.Set;

/**
 *   Component-level Interlacing Between Concerns for classes result
 */
public class CIBClassResult {
    private final Concern concern;
    private final Set<Concern> interlacedConcerns = new HashSet<Concern>();

    public CIBClassResult(Concern concern, Element context, Class cls) {
        this.concern = concern;
        addInterlacedConcerns(context, cls);
    }

    public void addInterlacedConcerns(Element context, Class cls) {
        if (context instanceof Class)
            inspectClass(cls);
            //TODO arrumar para a representacao do Edipo
        /*else if (context instanceof Interface){
            inspectImplementedInterfaces(component);
			inspectRequiredInterfaces(component);
		}*/
        else
            inspectMethods(cls);

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
        for (Method operation : i.getMethods()) {
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

    private void inspectClass(Class cls) {
        getInterlacedConcerns().addAll(cls.getOwnConcerns());
        inspectMethods(cls);
    }

    private void inspectMethods(Class cls) {
        for (Method method : cls.getAllMethods()) {
            getInterlacedConcerns().addAll(method.getOwnConcerns());
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
