package br.otimizes.oplatool.patterns.models.ps.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.designpatterns.Strategy;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.ps.PS;
import br.otimizes.oplatool.architecture.representation.Element;

/**
 * The Class PSStrategy.
 */
public class PSStrategy implements PS {

    /** The contexts. */
    private final List<Element> contexts;
    
    /** The algorithm family. */
    private final AlgorithmFamily algorithmFamily;

    /**
     * Instantiates a new PS strategy.
     *
     * @param contexts the contexts
     * @param algorithmFamily the algorithm family
     */
    public PSStrategy(List<Element> contexts, AlgorithmFamily algorithmFamily) {
        this.contexts = contexts;
        this.algorithmFamily = algorithmFamily;
    }

    /**
     * Gets the PS of.
     *
     * @return the PS of
     */
    @Override
    public DesignPattern getPSOf() {
        return Strategy.getInstance();
    }

    /**
     * Checks if is PS of.
     *
     * @param designPattern the design pattern
     * @return true, if is PS of
     */
    @Override
    public boolean isPSOf(DesignPattern designPattern) {
        return Strategy.getInstance().equals(designPattern);
    }

    /**
     * Gets the participants.
     *
     * @return the list of participants
     */
    @Override
    public List<Element> getParticipants() {
        List<Element> participants = new ArrayList<>(contexts);
        participants.addAll(algorithmFamily.getParticipants());
        return participants;
    }

    /**
     * Gets the contexts.
     *
     * @return the list of contexts
     */
    public List<Element> getContexts() {
        return contexts;
    }

    /**
     * Gets the algorithm family.
     *
     * @return the algorithm family
     */
    public AlgorithmFamily getAlgorithmFamily() {
        return algorithmFamily;
    }

    /**
     * Hash code.
     *
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.algorithmFamily);
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
        final PSStrategy other = (PSStrategy) obj;
        return Objects.equals(this.algorithmFamily, other.algorithmFamily);
    }

}
