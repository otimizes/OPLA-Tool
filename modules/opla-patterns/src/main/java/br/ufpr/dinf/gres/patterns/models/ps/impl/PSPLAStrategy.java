package br.ufpr.dinf.gres.patterns.models.ps.impl;

import java.util.List;

import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.designpatterns.Strategy;
import br.ufpr.dinf.gres.patterns.models.AlgorithmFamily;
import br.ufpr.dinf.gres.patterns.models.ps.PSPLA;

/**
 * The Class PSPLAStrategy.
 */
public class PSPLAStrategy extends PSStrategy implements PSPLA {

    /**
     * Instantiates a new PSPLA strategy.
     *
     * @param contexts the contexts
     * @param algorithmFamily the algorithm family
     */
    public PSPLAStrategy(List<Element> contexts, AlgorithmFamily algorithmFamily) {
        super(contexts, algorithmFamily);
    }

    /**
     * Gets the PSPLA of.
     *
     * @return the PSPLA of
     */
    @Override
    public DesignPattern getPSPLAOf() {
        return Strategy.getInstance();
    }

    /**
     * Checks if is PSPLA of.
     *
     * @param designPattern the design pattern
     * @return true, if is PSPLA of
     */
    @Override
    public boolean isPSPLAOf(DesignPattern designPattern) {
        return Strategy.getInstance().equals(designPattern);
    }

}
