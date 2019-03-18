package br.ufpr.inf.opla.patterns.strategies.scopeselection.impl;

import arquitetura.representation.Architecture;
import arquitetura.representation.Element;
import arquitetura.representation.Patterns;
import br.ufpr.inf.opla.patterns.models.Scope;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.ufpr.inf.opla.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;
import br.ufpr.inf.opla.patterns.util.ElementUtil;

import java.util.Set;

public class ElementsWithSameDesignPatternSelection implements ScopeSelectionStrategy {

    private final ScopeSelectionStrategy selectionStrategy;

    public ElementsWithSameDesignPatternSelection() {
        selectionStrategy = new RandomScopeSelection();
    }

    public ElementsWithSameDesignPatternSelection(ScopeSelectionStrategy strategy) {
        selectionStrategy = strategy;
    }

    @Override
    public Scope selectScope(Architecture architecture, Patterns designPattern) {
        Scope scope = selectionStrategy.selectScope(architecture, designPattern);
        for (int i = 0; i < scope.getElements().size(); i++) {
            Element element = scope.getElements().get(i);
            Set<String> appliedDesignPatterns = ElementUtil.getAppliedDesignPatterns(element);
            if ((appliedDesignPatterns.size() == 1 && !appliedDesignPatterns.contains(designPattern.getName()))
                    || (appliedDesignPatterns.size() == 2 && !appliedDesignPatterns.contains(designPattern.getName()) && !appliedDesignPatterns.contains(Patterns.ADAPTER.getName()))
                    || (appliedDesignPatterns.size() > 2)) {
                scope.getElements().remove(i);
                i--;
            }
        }
        return scope;
    }

}
