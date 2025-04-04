package br.otimizes.oplatool.patterns.models.ps.impl;

import br.otimizes.oplatool.architecture.representation.Concern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.patterns.designpatterns.Bridge;
import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.patterns.models.AlgorithmFamily;
import br.otimizes.oplatool.patterns.models.ps.PS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Class PSBridge.
 */
public class PSBridge implements PS {

    private final List<Element> contexts;
    private final AlgorithmFamily algorithmFamily;
    private final List<Concern> commonConcerns;

    public PSBridge(List<Element> contexts, AlgorithmFamily algorithmFamily, List<Concern> commonConcerns) {
        this.contexts = contexts;
        this.algorithmFamily = algorithmFamily;
        this.commonConcerns = commonConcerns;
    }

    @Override
    public DesignPattern getPSOf() {
        return Bridge.getInstance();
    }

    @Override
    public boolean isPSOf(DesignPattern designPattern) {
        return Bridge.getInstance().equals(designPattern);
    }

    @Override
    public List<Element> getParticipants() {
        List<Element> participants = new ArrayList<>(contexts);
        participants.addAll(algorithmFamily.getParticipants());
        return participants;
    }

    public List<Element> getContexts() {
        return contexts;
    }

    public AlgorithmFamily getAlgorithmFamily() {
        return algorithmFamily;
    }

    public List<Concern> getCommonConcerns() {
        return commonConcerns;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.algorithmFamily);
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
        final PSBridge other = (PSBridge) obj;
        return Objects.equals(this.algorithmFamily, other.algorithmFamily);
    }
}
