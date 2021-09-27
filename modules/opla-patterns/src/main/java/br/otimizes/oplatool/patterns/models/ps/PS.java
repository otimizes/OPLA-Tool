package br.otimizes.oplatool.patterns.models.ps;

import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;

import java.util.List;

/**
 * The Interface PS.
 */
public interface PS {

    DesignPattern getPSOf();

    boolean isPSOf(DesignPattern designPattern);

    List<Element> getParticipants();
}
