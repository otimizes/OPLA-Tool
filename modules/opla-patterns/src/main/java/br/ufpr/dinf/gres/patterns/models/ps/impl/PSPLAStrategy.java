package br.ufpr.dinf.gres.patterns.models.ps.impl;

import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.designpatterns.Strategy;
import br.ufpr.dinf.gres.patterns.models.AlgorithmFamily;
import br.ufpr.dinf.gres.patterns.models.ps.PSPLA;

import java.util.List;

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
