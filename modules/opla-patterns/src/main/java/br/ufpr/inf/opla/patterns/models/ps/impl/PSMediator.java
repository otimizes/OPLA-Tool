/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.inf.opla.patterns.models.ps.impl;

import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import br.ufpr.inf.opla.patterns.designpatterns.DesignPattern;
import br.ufpr.inf.opla.patterns.designpatterns.Mediator;
import br.ufpr.inf.opla.patterns.models.ps.PS;

import java.util.List;
import java.util.Objects;

/**
 * @author giovaniguizzo
 */
public class PSMediator implements PS {

    private List<Element> participants;
    private Concern concern;

    public PSMediator(List<Element> participants, Concern concern) {
        this.participants = participants;
        this.concern = concern;
    }

    @Override
    public DesignPattern getPSOf() {
        return Mediator.getInstance();
    }

    @Override
    public boolean isPSOf(DesignPattern designPattern) {
        return Mediator.getInstance().equals(designPattern);
    }

    @Override
    public List<Element> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Element> participants) {
        this.participants = participants;
    }

    public Concern getConcern() {
        return concern;
    }

    public void setConcern(Concern concern) {
        this.concern = concern;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.concern);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PSMediator other = (PSMediator) obj;
        if (!Objects.equals(this.concern, other.concern)) {
            return false;
        }
        return true;
    }

}
