/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.otimizes.oplatool.patterns.models.ps.impl;

import java.util.List;
import java.util.Objects;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.designpatterns.Mediator;
import br.otimizes.oplatool.patterns.models.ps.PS;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;

/**
 * The Class PSMediator.
 *
 * @author giovaniguizzo
 */
public class PSMediator implements PS {

    /** The participants. */
    private List<Element> participants;
    
    /** The concern. */
    private Concern concern;

    /**
     * Instantiates a new PS mediator.
     *
     * @param participants the participants
     * @param concern the concern
     */
    public PSMediator(List<Element> participants, Concern concern) {
        this.participants = participants;
        this.concern = concern;
    }

    /**
     * Gets the PS of.
     *
     * @return the PS of
     */
    @Override
    public DesignPattern getPSOf() {
        return Mediator.getInstance();
    }

    /**
     * Checks if is PS of.
     *
     * @param designPattern the design pattern
     * @return true, if is PS of
     */
    @Override
    public boolean isPSOf(DesignPattern designPattern) {
        return Mediator.getInstance().equals(designPattern);
    }

    /**
     * Gets the participants.
     *
     * @return the list of participants
     */
    @Override
    public List<Element> getParticipants() {
        return participants;
    }

    /**
     * Sets the participants.
     *
     * @param participants the new participants
     */
    public void setParticipants(List<Element> participants) {
        this.participants = participants;
    }

    /**
     * Gets the concern.
     *
     * @return the concern
     */
    public Concern getConcern() {
        return concern;
    }

    /**
     * Sets the concern.
     *
     * @param concern the new concern
     */
    public void setConcern(Concern concern) {
        this.concern = concern;
    }

    /**
     * Hash code.
     *
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.concern);
        return hash;
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PSMediator other = (PSMediator) obj;
        return Objects.equals(this.concern, other.concern);
    }

}
