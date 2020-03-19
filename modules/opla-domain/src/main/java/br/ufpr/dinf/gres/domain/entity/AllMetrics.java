package br.ufpr.dinf.gres.domain.entity;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.*;

import java.util.ArrayList;
import java.util.List;

public class AllMetrics {

    private List<CMObjectiveFunction> cm = new ArrayList<>();
    private List<ELEGObjectiveFunction> eleg = new ArrayList<>();
    private List<FMObjectiveFunction> fm = new ArrayList<>();
    private List<EXTObjectiveFunction> ext = new ArrayList<>();
    private List<ACOMPObjectiveFunction> acomp = new ArrayList<>();
    private List<ACLASSObjectiveFunction> aclass = new ArrayList<>();
    private List<LCCObjectiveFunction> lcc = new ArrayList<>();
    private List<TAMObjectiveFunction> tam = new ArrayList<>();
    private List<COEObjectiveFunction> coe = new ArrayList<>();
    private List<DCObjectiveFunction> dc = new ArrayList<>();
    private List<ECObjectiveFunction> ec = new ArrayList<>();
    private List<WOCSCLASSObjectiveFunction> wocsclass = new ArrayList<>(); //addYni
    private List<WOCSINTERFACEObjectiveFunction> wocsinterface = new ArrayList<>(); //addYni
    private List<CBCSObjectiveFunction> rcc = new ArrayList<>(); //addYni
    private List<SVObjectiveFunction> sv = new ArrayList<>(); //addYni
    private List<SDObjectiveFunction> sd = new ArrayList<>(); //addYni
    private List<TVObjectiveFunction> tv = new ArrayList<>(); //addYni

    public List<CMObjectiveFunction> getCm() {
        return cm;
    }

    public void setCm(List<CMObjectiveFunction> cm) {
        this.cm = cm;
    }

    public List<ELEGObjectiveFunction> getEleg() {
        return eleg;
    }

    public void setEleg(List<ELEGObjectiveFunction> eleg) {
        this.eleg = eleg;
    }

    public List<FMObjectiveFunction> getFm() {
        return fm;
    }

    public void setFm(List<FMObjectiveFunction> fm) {
        this.fm = fm;
    }

    public List<EXTObjectiveFunction> getExt() {
        return ext;
    }

    public void setExt(List<EXTObjectiveFunction> ext) {
        this.ext = ext;
    }

    public List<LCCObjectiveFunction> getLcc() {
        return lcc;
    }

    public void setLcc(List<LCCObjectiveFunction> lcc) {
        this.lcc = lcc;
    }

    public List<ACOMPObjectiveFunction> getAcomp() {
        return acomp;
    }

    public void setAcomp(List<ACOMPObjectiveFunction> acomp) {
        this.acomp = acomp;
    }

    public List<ACLASSObjectiveFunction> getAclass() {
        return aclass;
    }

    public void setAclass(List<ACLASSObjectiveFunction> aclass) {
        this.aclass = aclass;
    }

    public List<TAMObjectiveFunction> getTam() {
        return tam;
    }

    public void setTam(List<TAMObjectiveFunction> tam) {
        this.tam = tam;
    }

    public List<COEObjectiveFunction> getCoe() {
        return coe;
    }

    public void setCoe(List<COEObjectiveFunction> coe) {
        this.coe = coe;
    }

    public List<DCObjectiveFunction> getDc() {
        return dc;
    }

    public void setDc(List<DCObjectiveFunction> dc) {
        this.dc = dc;
    }

    public List<ECObjectiveFunction> getEc() {
        return ec;
    }

    public void setEc(List<ECObjectiveFunction> ec) {
        this.ec = ec;
    }

    public List<WOCSCLASSObjectiveFunction> getWocsclass() {
        return wocsclass;
    }

    public void setWocsclass(List<WOCSCLASSObjectiveFunction> wocsc) {
        this.wocsclass = wocsc;
    }

    public List<WOCSINTERFACEObjectiveFunction> getWocsinterface() {
        return wocsinterface;
    }

    public void setWocsinterface(List<WOCSINTERFACEObjectiveFunction> wocsi) {
        this.wocsinterface = wocsi;
    }

    public List<CBCSObjectiveFunction> getRcc() {
        return rcc;
    }

    public void setRcc(List<CBCSObjectiveFunction> rcc) {
        this.rcc = rcc;
    }

    public List<SVObjectiveFunction> getSv() {
        return sv;
    }

    public void setSv(List<SVObjectiveFunction> sv) {
        this.sv = sv;
    }

    public List<SDObjectiveFunction> getSd() {
        return sd;
    }

    public void setSd(List<SDObjectiveFunction> sd) {
        this.sd = sd;
    }

    public List<TVObjectiveFunction> getTv() {
        return tv;
    }

    public void setTv(List<TVObjectiveFunction> tv) {
        this.tv = tv;
    }

    @Override
    public String toString() {
        return "AllMetrics{" +
                "\nconventional=" + cm +
                ", \nelegance=" + eleg +
                ", \nfeatureDriven=" + fm +
                ", \nplaExtensibility=" + ext +
                ", \nacomp=" + acomp +
                ", \naclass=" + aclass +
                ", \ntam=" + tam +
                ", \ncoe=" + coe +
                ", \ndc=" + dc +
                ", \nec=" + ec +
                "\n}";
    }


    public void remove(Integer id) {
        if (this.cm.size() > id) this.cm.remove(id);
        if (this.eleg.size() > id) this.eleg.remove(id);
        if (this.fm.size() > id) this.fm.remove(id);
        if (this.ext.size() > id) this.ext.remove(id);
        if (this.acomp.size() > id) this.acomp.remove(id);
        if (this.aclass.size() > id) this.aclass.remove(id);
        if (this.tam.size() > id) this.tam.remove(id);
        if (this.coe.size() > id) this.coe.remove(id);
        if (this.dc.size() > id) this.dc.remove(id);
        if (this.ec.size() > id) this.ec.remove(id);
        if (this.wocsclass.size() > id) this.wocsclass.remove(id);
        if (this.lcc.size() > id) this.lcc.remove(id);
        if (this.wocsinterface.size() > id) this.wocsinterface.remove(id);
        if (this.rcc.size() > id) this.rcc.remove(id);
        if (this.sv.size() > id) this.sv.remove(id);
        if (this.sd.size() > id) this.sd.remove(id);
        if (this.tv.size() > id) this.tv.remove(id);
    }
}
