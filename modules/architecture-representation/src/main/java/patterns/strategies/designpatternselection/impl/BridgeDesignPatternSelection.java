package patterns.strategies.designpatternselection.impl;

import patterns.designpatterns.Bridge;
import patterns.designpatterns.DesignPattern;
import patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

public class BridgeDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Bridge.getInstance();
    }

}
