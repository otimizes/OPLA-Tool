package patterns.strategies.scopeselection;

import arquitetura.representation.Architecture;
import arquitetura.representation.Patterns;
import patterns.models.Scope;

public interface ScopeSelectionStrategy {

    public Scope selectScope(Architecture architecture, Patterns designPattern);

}