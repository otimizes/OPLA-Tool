package patterns.models.ps.impl;

import arquitetura.representation.Element;
import patterns.designpatterns.DesignPattern;
import patterns.designpatterns.Strategy;
import patterns.models.AlgorithmFamily;
import patterns.models.ps.PSPLA;

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
