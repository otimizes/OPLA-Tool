package br.ufpr.dinf.gres.patterns.models.ps.impl;

import br.ufpr.dinf.gres.architecture.representation.Concern;
import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.patterns.designpatterns.Bridge;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;
import br.ufpr.dinf.gres.patterns.models.AlgorithmFamily;
import br.ufpr.dinf.gres.patterns.models.ps.PSPLA;

import java.util.List;

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
