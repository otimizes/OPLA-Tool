package patterns.models.ps;

import arquitetura.representation.Element;
import patterns.designpatterns.DesignPattern;

import java.util.List;

public interface PS {

    public DesignPattern getPSOf();

    public boolean isPSOf(DesignPattern designPattern);

    public List<Element> getParticipants();

}
