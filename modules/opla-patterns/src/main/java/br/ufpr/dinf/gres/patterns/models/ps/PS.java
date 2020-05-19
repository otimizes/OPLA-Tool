package br.ufpr.dinf.gres.patterns.models.ps;

import java.util.List;

import br.ufpr.dinf.gres.architecture.representation.Element;
import br.ufpr.dinf.gres.patterns.designpatterns.DesignPattern;

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
