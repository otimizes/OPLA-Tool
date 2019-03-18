package br.ufpr.inf.opla.patterns.models.ps;

import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;

import java.util.List;

public interface PS {

    public DesignPattern getPSOf();

    public boolean isPSOf(DesignPattern designPattern);

    public List<Element> getParticipants();

}
