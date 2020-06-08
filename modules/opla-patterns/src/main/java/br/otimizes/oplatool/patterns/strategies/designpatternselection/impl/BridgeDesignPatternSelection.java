package br.otimizes.oplatool.patterns.strategies.designpatternselection.impl;

import br.otimizes.oplatool.patterns.designpatterns.Bridge;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

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
