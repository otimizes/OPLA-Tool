package br.ufpr.dinf.gres.patterns.strategies.designpatternselection.impl;

import br.ufpr.dinf.gres.patterns.designpatterns.Bridge;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

public class BridgeDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Bridge.getInstance();
    }

}
