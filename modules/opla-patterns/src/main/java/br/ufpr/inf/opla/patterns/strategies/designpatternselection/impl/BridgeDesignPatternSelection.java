package br.ufpr.inf.opla.patterns.strategies.designpatternselection.impl;

import br.ufpr.inf.opla.patterns.designpatterns.Bridge;
import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

public class BridgeDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Bridge.getInstance();
    }

}
