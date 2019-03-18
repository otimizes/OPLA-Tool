package br.ufpr.inf.opla.patterns.strategies.designpatternselection.defaultstrategy;

import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

import java.util.Random;

public class RandomDesignPatternSelection implements DesignPatternSelectionStrategy {

    @Override
    public DesignPattern selectDesignPattern() {
        int index = new Random().nextInt(DesignPattern.IMPLEMENTED.length);
        return DesignPattern.IMPLEMENTED[index];
    }

}
