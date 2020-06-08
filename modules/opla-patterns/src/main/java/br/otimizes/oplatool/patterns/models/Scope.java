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

    /** The elements. */
    private final List<Element> elements;
    
    /** The list of PSs. */
    private final List<PS> ps;
    
    /** The list of PSPLAs. */
    private final List<PSPLA> psPLA;

    /**
     * Instantiates a new scope.
     */
    public Scope() {
        this.elements = new ArrayList<>();
        this.ps = new ArrayList<>();
        this.psPLA = new ArrayList<>();
    }

    /**
     * Gets the elements.
     *
     * @return the list of elements
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Gets the PSs.
     *
     * @return the list of PSs
     */
    public List<PS> getPSs() {
        return ps;
    }

    /**
     * Gets the PSPLAs.
     *
     * @return the list of 
     */
    public List<PSPLA> getPSsPLA() {
        return psPLA;
    }

    /**
     * Gets the design pattern PSs
     *
     * @param designPattern the design pattern
     * @return the PSs
     */
    public List<PS> getPSs(DesignPattern designPattern) {
        List<PS> psList = new ArrayList<>();
        for (PS tempPS : ps) {
            if (tempPS.isPSOf(designPattern)) {
                psList.add(tempPS);
            }
        }
        return psList;
    }

    /**
     * Gets desing pattern PSPLAs
     *
     * @param designPattern the design pattern
     * @return the PSPLAs
     */
    public List<PSPLA> getPSsPLA(DesignPattern designPattern) {
        List<PSPLA> psPLAList = new ArrayList<>();
        for (PSPLA tempPSPLA : psPLA) {
            if (tempPSPLA.isPSOf(designPattern)) {
                psPLAList.add(tempPSPLA);
            }
        }
        return psPLAList;
    }

    /**
     * Adds the PS.
     *
     * @param ps the PS
     */
    public void addPS(PS ps) {
        this.ps.add(ps);
    }

    /**
     * Adds the PSPLA.
     *
     * @param psPla the PSPLA
     */
    public void addPSPLA(PSPLA psPla) {
        this.psPLA.add(psPla);
    }

    /**
     * Checks if is ps.
     *
     * @return true, if PS
     */
    public boolean isPS() {
        return !ps.isEmpty();
    }

    /**
     * Checks if is pspla.
     *
     * @return true, if PSPLA
     */
    public boolean isPSPLA() {
        return !psPLA.isEmpty();
    }

    /**
     * Hash code.
     *
     * @return the hashCode
     */
    @Override
    public int hashCode() {
        int hash = 5;
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
        final Scope other = (Scope) obj;
        return Objects.equals(this.elements, other.elements);
    }

}
