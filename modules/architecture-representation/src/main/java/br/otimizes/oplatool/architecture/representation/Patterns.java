package br.otimizes.oplatool.architecture.representation;

/**
 * Patterns to be applied in classes and interfaces
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
