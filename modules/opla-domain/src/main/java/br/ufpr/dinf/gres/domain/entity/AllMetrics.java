package br.ufpr.dinf.gres.domain.entity;

import br.ufpr.dinf.gres.domain.entity.objectivefunctions.*;

import java.util.ArrayList;
import java.util.List;

public class AllMetrics {

    private List<CMObjectiveFunction> conventional = new ArrayList<>();
    private List<ELEGObjectiveFunction> elegance = new ArrayList<>();
    private List<FMObjectiveFunction> fm = new ArrayList<>();
    private List<EXTObjectiveFunction> plaExtensibility = new ArrayList<>();
    private List<ACOMPObjectiveFunction> acomp = new ArrayList<>();
    private List<ACLASSObjectiveFunction> aclass = new ArrayList<>();
    private List<LCCObjectiveFunction> lcc = new ArrayList<>();
    private List<TAMObjectiveFunction> tam = new ArrayList<>();
    private List<COEObjectiveFunction> coe = new ArrayList<>();
    private List<DCObjectiveFunction> dc = new ArrayList<>();
    private List<ECObjectiveFunction> ec = new ArrayList<>();
    private List<WOCSCLASSObjectiveFunction> wocsclass = new ArrayList<>(); //addYni
    private List<WOCSINTERFACEObjectiveFunction> wocsinterface = new ArrayList<>(); //addYni
    private List<RCCObjectiveFunction> cbcs = new ArrayList<>(); //addYni
    private List<SVObjectiveFunction> svc = new ArrayList<>(); //addYni
    private List<SDObjectiveFunction> ssc = new ArrayList<>(); //addYni
    private List<TVObjectiveFunction> av = new ArrayList<>(); //addYni

    public List<CMObjectiveFunction> getConventional() {
        return conventional;
    }

    public void setConventional(List<CMObjectiveFunction> conventional) {
        this.conventional = conventional;
    }

    public List<ELEGObjectiveFunction> getElegance() {
        return elegance;
    }

    public void setElegance(List<ELEGObjectiveFunction> elegance) {
        this.elegance = elegance;
    }

    public List<FMObjectiveFunction> getFm() {
        return fm;
    }

    public void setFm(List<FMObjectiveFunction> fm) {
        this.fm = fm;
    }

    public List<EXTObjectiveFunction> getPlaExtensibility() {
        return plaExtensibility;
    }

    public void setPlaExtensibility(List<EXTObjectiveFunction> plaExtensibility) {
        this.plaExtensibility = plaExtensibility;
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

    public List<RCCObjectiveFunction> getCbcs() {
        return cbcs;
    }

    public void setCbcs(List<RCCObjectiveFunction> cbcs) {
        this.cbcs = cbcs;
    }

    public List<SVObjectiveFunction> getSvc() {
        return svc;
    }

    public void setSvc(List<SVObjectiveFunction> svc) {
        this.svc = svc;
    }

    public List<SDObjectiveFunction> getSsc() {
        return ssc;
    }

    public void setSsc(List<SDObjectiveFunction> ssc) {
        this.ssc = ssc;
    }

    public List<TVObjectiveFunction> getAv() {
        return av;
    }

    public void setAv(List<TVObjectiveFunction> av) {
        this.av = av;
    }

    @Override
    public String toString() {
        return "AllMetrics{" +
                "\nconventional=" + conventional +
                ", \nelegance=" + elegance +
                ", \nfeatureDriven=" + fm +
                ", \nplaExtensibility=" + plaExtensibility +
                ", \nacomp=" + acomp +
                ", \naclass=" + aclass +
                ", \ntam=" + tam +
                ", \ncoe=" + coe +
                ", \ndc=" + dc +
                ", \nec=" + ec +
                "\n}";
    }


    public void remove(Integer id) {
        if (this.conventional.size() > id) this.conventional.remove(id);
        if (this.elegance.size() > id) this.elegance.remove(id);
        if (this.fm.size() > id) this.fm.remove(id);
        if (this.plaExtensibility.size() > id) this.plaExtensibility.remove(id);
        if (this.acomp.size() > id) this.acomp.remove(id);
        if (this.aclass.size() > id) this.aclass.remove(id);
        if (this.tam.size() > id) this.tam.remove(id);
        if (this.coe.size() > id) this.coe.remove(id);
        if (this.dc.size() > id) this.dc.remove(id);
        if (this.ec.size() > id) this.ec.remove(id);
        if (this.wocsclass.size() > id) this.wocsclass.remove(id);
        if (this.lcc.size() > id) this.lcc.remove(id);
        if (this.wocsinterface.size() > id) this.wocsinterface.remove(id);
        if (this.cbcs.size() > id) this.cbcs.remove(id);
        if (this.svc.size() > id) this.svc.remove(id);
        if (this.ssc.size() > id) this.ssc.remove(id);
        if (this.av.size() > id) this.av.remove(id);
    }
}
