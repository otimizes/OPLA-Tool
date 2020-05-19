package br.ufpr.dinf.gres.patterns.strategies.designpatternselection.impl;

import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.designpatterns.Mediator;
import br.ufpr.dinf.gres.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

/**
 * The Class MediatorDesignPatternSelection.
 */
public class MediatorDesignPatternSelection implements DesignPatternSelectionStrategy {

    /**
     * Select design pattern.
     *
     * @return the design pattern
     */
    @Override
    public DesignPattern selectDesignPattern() {
        return Mediator.getInstance();
    }

}
