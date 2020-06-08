package br.otimizes.oplatool.patterns.strategies.scopeselection.impl;

import java.util.Set;

import br.otimizes.oplatool.patterns.models.Scope;
import br.otimizes.oplatool.patterns.strategies.scopeselection.ScopeSelectionStrategy;
import br.otimizes.oplatool.patterns.util.ElementUtil;
import br.otimizes.oplatool.architecture.representation.Architecture;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.architecture.representation.Patterns;
import br.otimizes.oplatool.patterns.strategies.scopeselection.defaultstrategy.RandomScopeSelection;

/**
 * The Class ElementsWithSameDesignPatternSelection.
 */
public class ElementsWithSameDesignPatternSelection implements ScopeSelectionStrategy {

    /** The selection strategy. */
    private final ScopeSelectionStrategy selectionStrategy;

    /**
     * Instantiates a new elements with same design pattern selection.
     */
    public ElementsWithSameDesignPatternSelection() {
        selectionStrategy = new RandomScopeSelection();
    }

    /**
     * Instantiates a new elements with same design pattern selection.
     *
     * @param strategy the strategy
     */
    public ElementsWithSameDesignPatternSelection(ScopeSelectionStrategy strategy) {
        selectionStrategy = strategy;
    }

    /**
     * Select scope.
     *
     * @param architecture the architecture
     * @param designPattern the design pattern
     * @return the scope
     */
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
