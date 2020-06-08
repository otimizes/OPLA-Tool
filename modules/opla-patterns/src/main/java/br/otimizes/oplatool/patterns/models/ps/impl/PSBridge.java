package br.otimizes.oplatool.patterns.models.ps.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.otimizes.oplatool.patterns.designpatterns.Bridge;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.ps.PS;
import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;

/**
 * The Class PSBridge.
 */
public class PSBridge implements PS {

    /** The contexts. */
    private final List<Element> contexts;
    
    /** The algorithm family. */
    private final AlgorithmFamily algorithmFamily;
    
    /** The common concerns. */
    private final List<Concern> commonConcerns;

    /**
     * Instantiates a new PS bridge.
     *
     * @param contexts the contexts
     * @param algorithmFamily the algorithm family
     * @param commonConcerns the list of common concerns
     */
    public PSBridge(List<Element> contexts, AlgorithmFamily algorithmFamily, List<Concern> commonConcerns) {
        this.contexts = contexts;
        this.algorithmFamily = algorithmFamily;
        this.commonConcerns = commonConcerns;
    }

    /**
     * Gets the PS of.
     *
     * @return the PS of
     */
    @Override
    public DesignPattern getPSOf() {
        return Bridge.getInstance();
    }

    /**
     * Checks if is PS of.
     *
     * @param designPattern the design pattern
     * @return true, if is PS of
     */
    @Override
    public boolean isPSOf(DesignPattern designPattern) {
        return Bridge.getInstance().equals(designPattern);
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
     * Gets the common concerns.
     *
     * @return the list of common concerns
     */
    public List<Concern> getCommonConcerns() {
        return commonConcerns;
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
        final PSBridge other = (PSBridge) obj;
        return Objects.equals(this.algorithmFamily, other.algorithmFamily);
    }
}
