package br.ufpr.dinf.gres.patterns.strategies.designpatternselection.impl;

import br.ufpr.dinf.gres.patterns.designpatterns.Bridge;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

/**
 * The Class BridgeDesignPatternSelection.
 */
public class BridgeDesignPatternSelection implements DesignPatternSelectionStrategy {

    /**
     * Select design pattern.
     *
     * @return the design pattern
     */
    @Override
    public DesignPattern selectDesignPattern() {
        return Bridge.getInstance();
    }

}
