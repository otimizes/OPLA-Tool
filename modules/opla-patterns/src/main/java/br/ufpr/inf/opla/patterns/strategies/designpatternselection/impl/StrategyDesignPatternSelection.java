package br.ufpr.inf.opla.patterns.strategies.designpatternselection.impl;

import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.designpatterns.Strategy;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

public class StrategyDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Strategy.getInstance();
    }

}
