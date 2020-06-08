package br.otimizes.oplatool.patterns.strategies.designpatternselection.impl;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.designpatterns.Strategy;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

/**
 * The Class StrategyDesignPatternSelection.
 */
public class StrategyDesignPatternSelection implements DesignPatternSelectionStrategy {

    /**
     * Select design pattern.
     *
     * @return the design pattern
     */
    @Override
    public DesignPattern selectDesignPattern() {
        return Strategy.getInstance();
    }

}
