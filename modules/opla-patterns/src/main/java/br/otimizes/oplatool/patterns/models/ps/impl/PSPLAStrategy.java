package br.otimizes.oplatool.patterns.models.ps.impl;

import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.designpatterns.Strategy;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.ps.PSPLA;

import java.util.List;

/**
 * The Class PSPLAStrategy.
 */
public class PSPLAStrategy extends PSStrategy implements PSPLA {

    public PSPLAStrategy(List<Element> contexts, AlgorithmFamily algorithmFamily) {
        super(contexts, algorithmFamily);
    }

    @Override
    public DesignPattern getPSPLAOf() {
        return Strategy.getInstance();
    }

    @Override
    public boolean isPSPLAOf(DesignPattern designPattern) {
        return Strategy.getInstance().equals(designPattern);
    }
}
