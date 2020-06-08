package br.otimizes.oplatool.patterns.strategies.designpatternselection;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;

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