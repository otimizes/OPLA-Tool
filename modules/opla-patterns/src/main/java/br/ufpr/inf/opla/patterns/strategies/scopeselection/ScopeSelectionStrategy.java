package br.ufpr.inf.opla.patterns.strategies.scopeselection;

import arquitetura.representation.Architecture;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.models.Scope;

public interface ScopeSelectionStrategy {

    public Scope selectScope(Architecture architecture, Patterns designPattern);

}