package br.otimizes.oplatool.patterns.strategies.designpatternselection.impl;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.designpatterns.Mediator;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

/**
 * The Class MediatorDesignPatternSelection.
 */
public class MediatorDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        return Mediator.getInstance();
    }
}
