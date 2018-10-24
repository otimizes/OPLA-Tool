package jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import arquitetura.representation.*;
import arquitetura.representation.Class;

import java.util.Collection;
import java.util.HashMap;

public class CIBClass {

    private final Architecture architecture;
    private final HashMap<Concern, CIBClassResult> results = new HashMap<Concern, CIBClassResult>();

    public CIBClass(Architecture architecture) {
        this.architecture = architecture;

        for (Class cls : architecture.getAllClasses()) {
            inspectConcernsOfElement(cls, cls);
            inspectMethods(cls);
            //TODO incluir as interfaces que as classes podem realizar
            inspectInterfaces(cls, cls.getRequiredInterfaces());
            inspectInterfaces(cls, cls.getImplementedInterfaces());
        }
    }

    private void inspectInterfaces(Class cls, Collection<Interface> interfaces) {
        for (Interface i : interfaces) {
            inspectConcernsOfElement(i, cls);
            for (Method operation : i.getOperations()) {
                inspectConcernsOfElement(operation, cls);
            }
        }
    }

    private void inspectMethods(Class cls) {

        for (Method method : cls.getAllMethods()) {
            inspectConcernsOfElement(method, cls);
        }
    }

    private void inspectConcernsOfElement(Element element, Class cls) {
        for (Concern concern : element.getOwnConcerns()) {
            if (results.containsKey(concern))
                results.get(concern).addInterlacedConcerns(element, cls);
            else
                results.put(concern, new CIBClassResult(concern, element, cls));
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public HashMap<Concern, CIBClassResult> getResults() {
        return results;
    }
}

