package br.otimizes.oplatool.patterns.strategies.scopeselection.impl;

import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Patterns;

/**
 * The Class WholeArchitectureScopeSelection.
 */
public class WholeArchitectureScopeSelection implements ScopeSelectionStrategy {

    /**
     * Select scope.
     *
     * @param architecture the architecture
     * @param pattern the pattern
     * @return the scope
     */
    @Override
    public Scope selectScope(Architecture architecture, Patterns pattern) {
        Scope scope = new Scope();
        scope.getElements().addAll(architecture.getElements());
        return scope;
    }

}
