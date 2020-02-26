package br.ufpr.dinf.gres.patterns.models.ps;

import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;

import java.util.List;

public interface PS {

    public DesignPattern getPSOf();

    public boolean isPSOf(DesignPattern designPattern);

    public List<Element> getParticipants();

}
