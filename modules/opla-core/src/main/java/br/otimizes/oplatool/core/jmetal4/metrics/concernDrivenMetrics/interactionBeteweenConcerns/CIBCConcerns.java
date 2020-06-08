package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Package;

import java.util.Collection;
import java.util.HashMap;

/**
 * Component-level Interlacing Between Concerns
 */
public class CIBCConcerns {

    private final Architecture architecture;
    private final HashMap<Concern, CIBCResult> results = new HashMap<>();

    public CIBCConcerns(Architecture architecture) {

        this.architecture = architecture;

        for (Package component : architecture.getAllPackages()) {
            inspectConcernsOfElement(component, component);
            inspectInterfaces(component, component.getImplementedInterfaces());
            inspectInterfaces(component, component.getRequiredInterfaces());
        }
    }

    private void inspectInterfaces(Package component, Collection<Interface> interfaces) {
        for (Interface i : interfaces) {
            inspectConcernsOfElement(i, component);
            for (Method operation : i.getMethods()) {
                inspectConcernsOfElement(operation, component);
            }
        }
    }

    private void inspectConcernsOfElement(Element element, Package component) {
        for (Concern concern : element.getOwnConcerns()) {
            if (results.containsKey(concern))
                results.get(concern).addInterlacedConcerns(element, component);
            else
                results.put(concern, new CIBCResult(concern, element, component));
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public HashMap<Concern, CIBCResult> getResults() {
        return results;
    }
}
