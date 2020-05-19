package br.ufpr.dinf.gres.patterns.strategies.designpatternselection.defaultstrategy;

import java.util.Random;

import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

/**
 * The Class RandomDesignPatternSelection.
 */
public class RandomDesignPatternSelection implements DesignPatternSelectionStrategy {

    /**
     * Select design pattern.
     *
     * @return the design pattern
     */
    @Override
    public DesignPattern selectDesignPattern() {
        int index = new Random().nextInt(DesignPattern.IMPLEMENTED.length);
        return DesignPattern.IMPLEMENTED[index];
    }

}
