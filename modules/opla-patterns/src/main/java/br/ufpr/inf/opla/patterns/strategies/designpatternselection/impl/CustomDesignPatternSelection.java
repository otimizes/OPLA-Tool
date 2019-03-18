/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.strategies.designpatternselection.impl;

import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

import java.util.Random;

/**
 * @author giovaniguizzo
 */
public class CustomDesignPatternSelection implements DesignPatternSelectionStrategy {

    private String[] availablePatterns = null;

    /**
     * @param selectedPatterns Design patterns selecionados na GUI.
     */
    public CustomDesignPatternSelection(String... selectedPatterns) {
        availablePatterns = selectedPatterns;
    }

    @Override
    public DesignPattern selectDesignPattern() {
        int index = new Random().nextInt(availablePatterns.length);
        DesignPattern designPattern = null;
        for (DesignPattern dp : DesignPattern.IMPLEMENTED) {
            if (dp.getName().equalsIgnoreCase(availablePatterns[index])) {
                designPattern = dp;
                break;
            }
        }
        return designPattern;
    }

}
