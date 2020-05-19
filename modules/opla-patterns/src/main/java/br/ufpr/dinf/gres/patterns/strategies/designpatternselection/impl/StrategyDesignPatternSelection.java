package br.ufpr.dinf.gres.patterns.strategies.designpatternselection.impl;

import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.designpatterns.Strategy;
import br.ufpr.dinf.gres.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

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
