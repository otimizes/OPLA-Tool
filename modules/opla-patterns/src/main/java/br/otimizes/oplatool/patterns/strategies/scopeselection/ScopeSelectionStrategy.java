package br.otimizes.oplatool.patterns.strategies.scopeselection;

import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Patterns;
import br.otimizes.oplatool.patterns.models.Scope;

/**
 * The Interface ScopeSelectionStrategy.
 */
public interface ScopeSelectionStrategy {

    Scope selectScope(Architecture architecture, Patterns designPattern);
}