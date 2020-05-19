package br.ufpr.dinf.gres.patterns.strategies.designpatternselection;

import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;

/**
 * The Interface DesignPatternSelectionStrategy.
 */
public interface DesignPatternSelectionStrategy {

    /**
     * Select design pattern.
     *
     * @return the design pattern
     */
    DesignPattern selectDesignPattern();

}