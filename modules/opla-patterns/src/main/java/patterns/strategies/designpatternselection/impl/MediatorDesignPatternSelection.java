package patterns.strategies.designpatternselection.impl;

import patterns.designpatterns.DesignPattern;
import patterns.designpatterns.Mediator;
import patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

public class MediatorDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Mediator.getInstance();
    }

}
