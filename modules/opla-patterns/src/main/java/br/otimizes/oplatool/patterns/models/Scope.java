package br.otimizes.oplatool.patterns.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.otimizes.oplatool.patterns.designpatterns.DesignPattern;
import br.otimizes.oplatool.architecture.representation.Element;
import br.otimizes.oplatool.patterns.models.ps.PS;
import br.otimizes.oplatool.patterns.models.ps.PSPLA;

/**
 * The Class Scope.
 */
public class Scope {

    private final List<Element> elements;
    private final List<PS> ps;
    private final List<PSPLA> psPLA;

    public Scope() {
        this.elements = new ArrayList<>();
        this.ps = new ArrayList<>();
        this.psPLA = new ArrayList<>();
    }

    public List<Element> getElements() {
        return elements;
    }

    public List<PS> getPSs() {
        return ps;
    }

    public List<PSPLA> getPSsPLA() {
        return psPLA;
    }

    public List<PS> getPSs(DesignPattern designPattern) {
        List<PS> psList = new ArrayList<>();
        for (PS tempPS : ps) {
            if (tempPS.isPSOf(designPattern)) {
                psList.add(tempPS);
            }
        }
        return psList;
    }

    public List<PSPLA> getPSsPLA(DesignPattern designPattern) {
        List<PSPLA> psPLAList = new ArrayList<>();
        for (PSPLA tempPSPLA : psPLA) {
            if (tempPSPLA.isPSOf(designPattern)) {
                psPLAList.add(tempPSPLA);
            }
        }
        return psPLAList;
    }

    public void addPS(PS ps) {
        this.ps.add(ps);
    }

    public void addPSPLA(PSPLA psPla) {
        this.psPLA.add(psPla);
    }

    public boolean isPS() {
        return !ps.isEmpty();
    }

    public boolean isPSPLA() {
        return !psPLA.isEmpty();
    }

    @Override
    public int hashCode() {
        return 5;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Scope other = (Scope) obj;
        return Objects.equals(this.elements, other.elements);
    }
}
