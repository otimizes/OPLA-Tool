package patterns.strategies.designpatternselection.impl;

import patterns.designpatterns.DesignPattern;
import patterns.designpatterns.Strategy;
import patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

public class StrategyDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Strategy.getInstance();
    }

}
