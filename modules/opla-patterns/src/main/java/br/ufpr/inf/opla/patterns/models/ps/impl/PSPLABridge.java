package br.ufpr.inf.opla.patterns.models.ps.impl;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.designpatterns.Bridge;
import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.models.AlgorithmFamily;
import br.ufpr.inf.opla.patterns.models.ps.PSPLA;

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
