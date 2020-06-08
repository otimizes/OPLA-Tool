package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.*;
import br.otimizes.oplatool.architecture.representation.Package;

import java.util.Collection;
import java.util.HashMap;

/**
 * Operation-level Overlapping Between Concerns
 */
public class OOBCConcerns {

    private final Architecture architecture;
    private final HashMap<Concern, OOBCResult> results = new HashMap<Concern, OOBCResult>();

    public OOBCConcerns(Architecture architecture) {
        this.architecture = architecture;

        for (Package component : architecture.getAllPackages()) {
            inspectInterfacesOperations(component.getImplementedInterfaces());
            inspectInterfacesOperations(component.getRequiredInterfaces());
        }
    }

    private void inspectInterfacesOperations(Collection<Interface> interfaces) {
        for (Interface i : interfaces) {
            for (Method operation : i.getMethods()) {
                inspectConcernsOfOperation(operation);
            }
        }
    }

    private void inspectConcernsOfOperation(Method operation) {
        for (Concern concern : operation.getOwnConcerns()) {
            if (results.containsKey(concern))
                results.get(concern).addInterlacedConcerns(operation);
            else
                results.put(concern, new OOBCResult(concern, operation));
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public HashMap<Concern, OOBCResult> getResults() {
        return results;
    }
}
