package br.ufpr.dinf.gres.patterns.strategies.scopeselection;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Patterns;
import br.ufpr.dinf.gres.patterns.models.Scope;

/**
 * The Interface ScopeSelectionStrategy.
 */
public interface ScopeSelectionStrategy {

    /**
     * Select scope.
     *
     * @param architecture the architecture
     * @param designPattern the design pattern
     * @return the scope
     */
    Scope selectScope(Architecture architecture, Patterns designPattern);

}