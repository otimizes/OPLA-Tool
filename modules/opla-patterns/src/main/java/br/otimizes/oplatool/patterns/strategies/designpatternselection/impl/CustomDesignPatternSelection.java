/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.patterns.strategies.designpatternselection.impl;

import java.util.Random;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.strategies.designpatternselection.DesignPatternSelectionStrategy;

/**
 * The Class CustomDesignPatternSelection.
 *
 * @author giovaniguizzo
 */
public class CustomDesignPatternSelection implements DesignPatternSelectionStrategy {

    /** The available patterns. */
    private String[] availablePatterns = null;

    /**
     * Instantiates a new custom design pattern selection.
     *
     * @param selectedPatterns Design br.otimizes.oplatool.patterns selecionados na GUI.
     */
    public CustomDesignPatternSelection(String... selectedPatterns) {
        availablePatterns = selectedPatterns;
    }

    /**
     * Select design pattern.
     *
     * @return the design pattern
     */
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
