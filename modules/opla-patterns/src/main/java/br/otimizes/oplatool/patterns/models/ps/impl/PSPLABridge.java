package br.otimizes.oplatool.patterns.models.ps.impl;

import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.patterns.designpatterns.Bridge;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.ps.PSPLA;

import java.util.List;

/**
 * The Class PSPLABridge.
 */
public class PSPLABridge extends PSBridge implements PSPLA {

    public PSPLABridge(List<Element> contexts, AlgorithmFamily algorithmFamily, List<Concern> commonConcerns) {
        super(contexts, algorithmFamily, commonConcerns);
    }

    @Override
    public DesignPattern getPSPLAOf() {
        return Bridge.getInstance();
    }

    @Override
    public boolean isPSPLAOf(DesignPattern designPattern) {
        return Bridge.getInstance().equals(designPattern);
    }
}
