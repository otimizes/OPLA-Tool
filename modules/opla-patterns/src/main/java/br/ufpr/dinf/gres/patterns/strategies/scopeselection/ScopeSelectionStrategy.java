package br.ufpr.dinf.gres.patterns.strategies.scopeselection;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Patterns;
import br.ufpr.dinf.gres.patterns.models.Scope;

public interface ScopeSelectionStrategy {

    public Scope selectScope(Architecture architecture, Patterns designPattern);

}