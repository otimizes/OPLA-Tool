package br.ufpr.dinf.gres.core.jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Method;

import java.util.HashSet;
import java.util.Set;

/**
 * Operation-level Overlapping Between Concerns result
 */
public class OOBCResult {

    private final Concern concern;
    private final Set<Concern> interlacedConcerns = new HashSet<Concern>();

    public OOBCResult(Concern concern, Method context) {
        this.concern = concern;

        addInterlacedConcerns(context);
    }

    public void addInterlacedConcerns(Method context) {
        getInterlacedConcerns().addAll(context.getOwnConcerns());
        interlacedConcerns.remove(concern);
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
