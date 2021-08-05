package br.otimizes.oplatool.architecture.representation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Patterns Operations class
 */
public class PatternsOperations {

    private Set<String> patternsStereotypes = new HashSet<>();

    public PatternsOperations(Set<String> patterns) {
        this.patternsStereotypes = patterns;
    }

    public PatternsOperations() {
    }

    public Set<String> getAllPatterns() {
        return Collections.unmodifiableSet(patternsStereotypes);
    }

    public void setPatternsStereotypes(Set<String> patternsStereotypes) {
        this.patternsStereotypes = patternsStereotypes;
    }

    public void applyPattern(Patterns pattern) {
        this.patternsStereotypes.add(pattern.toString());
    }

    public boolean hasPatternApplied() {
        return !this.patternsStereotypes.isEmpty();
    }

    public boolean removePattern(Patterns pattern) {
        return this.patternsStereotypes.remove(pattern.toString());
    }
}
