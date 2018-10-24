package br.ufpr.inf.opla.patterns.models.ps;

import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;

public interface PSPLA extends PS {

    public DesignPattern getPSPLAOf();

    public boolean isPSPLAOf(DesignPattern designPattern);

}
