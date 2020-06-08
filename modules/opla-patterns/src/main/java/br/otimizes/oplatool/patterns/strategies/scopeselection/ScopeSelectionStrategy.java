package br.otimizes.oplatool.patterns.strategies.scopeselection;

import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Patterns;

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