package jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import arquitetura.representation.*;
import arquitetura.representation.Package;

import java.util.Collection;
import java.util.HashMap;


public class CIBC {

    private final Architecture architecture;
    private final HashMap<Concern, CIBCResult> results = new HashMap<Concern, CIBCResult>();

    public CIBC(Architecture architecture) {

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
            for (Method operation : i.getOperations()) {
                inspectConcernsOfElement(operation, component);
            }
        }
    }

    private void inspectConcernsOfElement(Element element, arquitetura.representation.Package component) {
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
