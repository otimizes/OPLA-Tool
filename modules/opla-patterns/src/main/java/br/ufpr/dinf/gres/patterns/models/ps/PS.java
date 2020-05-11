package br.ufpr.dinf.gres.patterns.models.ps;

import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;

import java.util.List;

public interface PS {

    DesignPattern getPSOf();

    boolean isPSOf(DesignPattern designPattern);

    List<Element> getParticipants();

}
