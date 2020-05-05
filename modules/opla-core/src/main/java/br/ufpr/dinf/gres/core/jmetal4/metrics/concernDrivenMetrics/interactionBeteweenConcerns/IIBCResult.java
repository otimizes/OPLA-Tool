package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Interface;
import br.ufpr.dinf.gres.architecture.representation.Method;

import java.util.HashSet;
import java.util.Set;

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
