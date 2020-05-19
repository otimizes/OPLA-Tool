package br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Patterns;
import br.ufpr.dinf.gres.patterns.models.Scope;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.ScopeSelectionStrategy;

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
