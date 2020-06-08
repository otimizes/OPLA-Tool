package br.otimizes.oplatool.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Interface;
import br.otimizes.oplatool.architecture.representation.Method;

import java.util.HashSet;
import java.util.Set;

/**
 * Interface-level Interlacing Between Concerns result
 */
public class IIBCResult {
    private final Concern concern;
    private final Set<Concern> interlacedConcerns = new HashSet<Concern>();

    public IIBCResult(Concern concern, Element context, Interface i) {
        this.concern = concern;
        addInterlacedConcerns(context, i);
    }

    public void addInterlacedConcerns(Element context, Interface i) {
        if (context.equals(i))
            addInterlacedConcerns(i);
        else if (context instanceof Method)
            inspectOperations(i);
        interlacedConcerns.remove(concern);
    }

    public void addInterlacedConcerns(Interface i) {
        getInterlacedConcerns().addAll(i.getOwnConcerns());
        inspectOperations(i);
    }

    private void inspectOperations(Interface i) {
        for (Method operation : i.getMethods()) {
            getInterlacedConcerns().addAll(operation.getOwnConcerns());
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
