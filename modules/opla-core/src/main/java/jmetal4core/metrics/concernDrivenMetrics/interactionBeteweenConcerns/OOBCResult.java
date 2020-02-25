package jmetal4.metrics.concernDrivenMetrics.interactionBeteweenConcerns;

import arquitetura.representation.Concern;
import arquitetura.representation.Method;

import java.util.HashSet;
import java.util.Set;

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
