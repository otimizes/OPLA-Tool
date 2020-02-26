package br.ufpr.dinf.gres.architecture.representation;

/**
 * Possíveis br.ufpr.dinf.gres.patterns a serem aplicados em classes e interfaces.
 *
 * @author edipofederle
 */
public enum Patterns {
    MEDIATOR("mediator"),
    STRATEGY("strategy"),
    FACADE("facade"),
    BRIDGE("bridge"),
    ADAPTER("adapter");

    private final String patternName;

    Patterns(String name) {
        patternName = name;
    }

    public String getName() {
        return patternName;
    }

}
