package jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import arquitetura.representation.*;
import arquitetura.representation.Package;

import java.util.Collection;
import java.util.HashMap;


public class IIBC {
    private final Architecture architecture;
    private final HashMap<Concern, IIBCResult> results = new HashMap<Concern, IIBCResult>();

    public IIBC(Architecture architecture) {
        this.architecture = architecture;

        for (Package component : architecture.getAllPackages()) {
            inspectInterfacesOperations(component, component.getImplementedInterfaces());
            inspectInterfacesOperations(component, component.getRequiredInterfaces());
        }
    }

    private void inspectInterfacesOperations(Package component, Collection<Interface> interfaces) {
        for (Interface i : interfaces) {
            inspectConcernsOfElement(i, i);
            for (Method operation : i.getOperations()) {
                inspectConcernsOfElement(operation, i);
            }
        }
    }

    private void inspectConcernsOfElement(Element element, Interface i) {
        for (Concern concern : element.getOwnConcerns()) {
            if (results.containsKey(concern))
                results.get(concern).addInterlacedConcerns(element, i);
            else
                results.put(concern, new IIBCResult(concern, element, i));
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public HashMap<Concern, IIBCResult> getResults() {
        return results;
    }
}
