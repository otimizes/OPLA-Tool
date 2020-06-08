package br.otimizes.oplatool.patterns.models.ps;

import java.util.List;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.architecture.representation.Element;

/**
 * The Interface PS.
 */
public interface PS {

    /**
     * Gets the PS of.
     *
     * @return the PS 
     */
    DesignPattern getPSOf();

    /**
     * Checks if is PS of.
     *
     * @param designPattern the design pattern
     * @return true, if is PS of
     */
    boolean isPSOf(DesignPattern designPattern);

    /**
     * Gets the participants.
     *
     * @return the list of participants
     */
    List<Element> getParticipants();

}
