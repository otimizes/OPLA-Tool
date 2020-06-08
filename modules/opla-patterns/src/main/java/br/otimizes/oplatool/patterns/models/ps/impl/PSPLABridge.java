package br.otimizes.oplatool.patterns.models.ps.impl;

import java.util.List;

import br.otimizes.oplatool.patterns.designpatterns.Bridge;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.ps.PSPLA;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;

/**
 * The Class PSPLABridge.
 */
public class PSPLABridge extends PSBridge implements PSPLA {

    /**
     * Instantiates a new PSPLA bridge.
     *
     * @param contexts the contexts
     * @param algorithmFamily the algorithm family
     * @param commonConcerns the common concerns
     */
    public PSPLABridge(List<Element> contexts, AlgorithmFamily algorithmFamily, List<Concern> commonConcerns) {
        super(contexts, algorithmFamily, commonConcerns);
    }

    /**
     * Gets the PSPLA of.
     *
     * @return the PSPLA of
     */
    @Override
    public DesignPattern getPSPLAOf() {
        return Bridge.getInstance();
    }

    /**
     * Checks if is PSPLA of.
     *
     * @param designPattern the design pattern
     * @return true, if is PSPLA of
     */
    @Override
    public boolean isPSPLAOf(DesignPattern designPattern) {
        return Bridge.getInstance().equals(designPattern);
    }

}
