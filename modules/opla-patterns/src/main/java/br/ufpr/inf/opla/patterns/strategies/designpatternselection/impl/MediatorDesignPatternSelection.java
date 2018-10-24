package br.ufpr.inf.opla.patterns.strategies.designpatternselection.impl;

import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.designpatterns.Mediator;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

public class MediatorDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Mediator.getInstance();
    }

}
