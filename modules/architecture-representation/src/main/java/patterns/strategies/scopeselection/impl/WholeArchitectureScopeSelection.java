package patterns.strategies.scopeselection.impl;

import arquitetura.representation.Architecture;
import arquitetura.representation.Patterns;
import patterns.models.Scope;
import patterns.strategies.scopeselection.ScopeSelectionStrategy;

public class WholeArchitectureScopeSelection implements ScopeSelectionStrategy {

    @Override
    public Scope selectScope(Architecture architecture, Patterns pattern) {
        Scope scope = new Scope();
        scope.getElements().addAll(architecture.getElements());
        return scope;
    }

}
