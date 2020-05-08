package br.ufpr.dinf.gres.patterns.strategies.scopeselection.impl;

import br.ufpr.dinf.gres.architecture.representation.Architecture;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.architecture.representation.Patterns;
import br.ufpr.dinf.gres.patterns.models.Scope;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.ufpr.dinf.gres.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;
import br.ufpr.dinf.gres.patterns.util.ElementUtil;

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
            if ((appliedDesignPatterns.size() == 1 && !appliedDesignPatterns.contains(designPattern.toString()))
                    || (appliedDesignPatterns.size() == 2 && !appliedDesignPatterns.contains(designPattern.toString()) && !appliedDesignPatterns.contains(Patterns.ADAPTER.toString()))
                    || (appliedDesignPatterns.size() > 2)) {
                scope.getElements().remove(i);
                i--;
            }
        }
        return scope;
    }

}
