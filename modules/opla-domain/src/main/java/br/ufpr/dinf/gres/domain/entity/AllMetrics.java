package br.ufpr.dinf.gres.domain.entity;

import br.ufpr.dinf.gres.domain.entity.metric.*;

import java.util.ArrayList;
import java.util.List;

public class AllMetrics {

    private List<ConventionalMetric> conventional = new ArrayList<>();
    private List<EleganceMetric> elegance = new ArrayList<>();
    private List<FeatureDrivenMetric> featureDriven = new ArrayList<>();
    private List<PLAExtensibilityMetric> plaExtensibility = new ArrayList<>();
    private List<AcompMetric> acomp = new ArrayList<>();
    private List<AclassMetric> aclass = new ArrayList<>();
    private List<TamMetric> tam = new ArrayList<>();
    private List<CoeMetric> coe = new ArrayList<>();
    private List<DcMetric> dc = new ArrayList<>();
    private List<EcMetric> ec = new ArrayList<>();
    private List<WocsclassMetric> wocsc = new ArrayList<>(); //addYni
    private List<WocsinterfaceMetric> wocsi = new ArrayList<>(); //addYni
    private List<CbcsMetric> cbcs = new ArrayList<>(); //addYni
    private List<SvcMetric> svc = new ArrayList<>(); //addYni
    private List<SscMetric> ssc = new ArrayList<>(); //addYni
    private List<AvMetric> av = new ArrayList<>(); //addYni

    public List<ConventionalMetric> getConventional() {
        return conventional;
    }

    public void setConventional(List<ConventionalMetric> conventional) {
        this.conventional = conventional;
    }

    public List<EleganceMetric> getElegance() {
        return elegance;
    }

    public void setElegance(List<EleganceMetric> elegance) {
        this.elegance = elegance;
    }

    public List<FeatureDrivenMetric> getFeatureDriven() {
        return featureDriven;
    }

    public void setFeatureDriven(List<FeatureDrivenMetric> featureDriven) {
        this.featureDriven = featureDriven;
    }

    public List<PLAExtensibilityMetric> getPlaExtensibility() {
        return plaExtensibility;
    }

    public void setPlaExtensibility(List<PLAExtensibilityMetric> plaExtensibility) {
        this.plaExtensibility = plaExtensibility;
    }

    public List<AcompMetric> getAcomp() {
        return acomp;
    }

    public void setAcomp(List<AcompMetric> acomp) {
        this.acomp = acomp;
    }

    public List<AclassMetric> getAclass() {
        return aclass;
    }

    public void setAclass(List<AclassMetric> aclass) {
        this.aclass = aclass;
    }

    public List<TamMetric> getTam() {
        return tam;
    }

    public void setTam(List<TamMetric> tam) {
        this.tam = tam;
    }

    public List<CoeMetric> getCoe() {
        return coe;
    }

    public void setCoe(List<CoeMetric> coe) {
        this.coe = coe;
    }

    public List<DcMetric> getDc() {
        return dc;
    }

    public void setDc(List<DcMetric> dc) {
        this.dc = dc;
    }

    public List<EcMetric> getEc() {
        return ec;
    }

    public void setEc(List<EcMetric> ec) {
        this.ec = ec;
    }

    public List<WocsclassMetric> getWocsclass() {
        return wocsc;
    }

    public void setWocsclass(List<WocsclassMetric> wocsc) {
        this.wocsc = wocsc;
    }

    public List<WocsinterfaceMetric> getWocsinterface() {
        return wocsi;
    }

    public void setWocsinterface(List<WocsinterfaceMetric> wocsi) {
        this.wocsi = wocsi;
    }

    public List<CbcsMetric> getCbcs() {
        return cbcs;
    }

    public void setCbcs(List<CbcsMetric> cbcs) {
        this.cbcs = cbcs;
    }

    public List<SvcMetric> getSvc() {
        return svc;
    }

    public void setSvc(List<SvcMetric> svc) {
        this.svc = svc;
    }

    public List<SscMetric> getSsc() {
        return ssc;
    }

    public void setSsc(List<SscMetric> ssc) {
        this.ssc = ssc;
    }

    public List<AvMetric> getAv() {
        return av;
    }

    public void setAv(List<AvMetric> av) {
        this.av = av;
    }

    @Override
    public String toString() {
        return "AllMetrics{" +
                "\nconventional=" + conventional +
                ", \nelegance=" + elegance +
                ", \nfeatureDriven=" + featureDriven +
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
        if (this.featureDriven.size() > id) this.featureDriven.remove(id);
        if (this.plaExtensibility.size() > id) this.plaExtensibility.remove(id);
        if (this.acomp.size() > id) this.acomp.remove(id);
        if (this.aclass.size() > id) this.aclass.remove(id);
        if (this.tam.size() > id) this.tam.remove(id);
        if (this.coe.size() > id) this.coe.remove(id);
        if (this.dc.size() > id) this.dc.remove(id);
        if (this.ec.size() > id) this.ec.remove(id);
        if (this.wocsc.size() > id) this.wocsc.remove(id);
        if (this.wocsi.size() > id) this.wocsi.remove(id);
        if (this.cbcs.size() > id) this.cbcs.remove(id);
        if (this.svc.size() > id) this.svc.remove(id);
        if (this.ssc.size() > id) this.ssc.remove(id);
        if (this.av.size() > id) this.av.remove(id);
    }
}
