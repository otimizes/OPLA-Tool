package metrics;

import java.util.ArrayList;
import java.util.List;

public class AllMetrics {

    private List<Conventional> conventional = new ArrayList<>();
    private List<Elegance> elegance = new ArrayList<>();
    private List<FeatureDriven> featureDriven = new ArrayList<>();
    private List<PLAExtensibility> plaExtensibility = new ArrayList<>();
    private List<Acomp> acomp = new ArrayList<>();
    private List<Aclass> aclass = new ArrayList<>();
    private List<Tam> tam = new ArrayList<>();
    private List<Coe> coe = new ArrayList<>();
    private List<Dc> dc = new ArrayList<>();
    private List<Ec> ec = new ArrayList<>();
    private List<Wocsclass> wocsc = new ArrayList<>(); //addYni
    private List<Wocsinterface> wocsi = new ArrayList<>(); //addYni
    private List<Cbcs> cbcs = new ArrayList<>(); //addYni
    private List<Svc> svc = new ArrayList<>(); //addYni
    private List<Ssc> ssc = new ArrayList<>(); //addYni
    private List<Av> av = new ArrayList<>(); //addYni


    public List<Conventional> getConventional() {
        return conventional;
    }

    public void setConventional(List<Conventional> conventional) {
        this.conventional = conventional;
    }

    public List<Elegance> getElegance() {
        return elegance;
    }

    public void setElegance(List<Elegance> elegance) {
        this.elegance = elegance;
    }

    public List<FeatureDriven> getFeatureDriven() {
        return featureDriven;
    }

    public void setFeatureDriven(List<FeatureDriven> featureDriven) {
        this.featureDriven = featureDriven;
    }

    public List<PLAExtensibility> getPlaExtensibility() {
        return plaExtensibility;
    }

    public void setPlaExtensibility(List<PLAExtensibility> plaExtensibility) {
        this.plaExtensibility = plaExtensibility;
    }

    public List<Acomp> getAcomp() {
        return acomp;
    }

    public void setAcomp(List<Acomp> acomp) {
        this.acomp = acomp;
    }

    public List<Aclass> getAclass() {
        return aclass;
    }

    public void setAclass(List<Aclass> aclass) {
        this.aclass = aclass;
    }

    public List<Tam> getTam() {
        return tam;
    }

    public void setTam(List<Tam> tam) {
        this.tam = tam;
    }

    public List<Coe> getCoe() {
        return coe;
    }

    public void setCoe(List<Coe> coe) {
        this.coe = coe;
    }

    public List<Dc> getDc() {
        return dc;
    }

    public void setDc(List<Dc> dc) {
        this.dc = dc;
    }

    public List<Ec> getEc() {
        return ec;
    }

    public void setEc(List<Ec> ec) {
        this.ec = ec;
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


    public List<Wocsclass> getWocsclass() {
        return wocsc;
    }

    public void setgetWocsclass(List<Wocsclass> wocsc) {
        this.wocsc = wocsc;
    }

    public List<Wocsinterface> getWocsinterface() {
        return wocsi;
    }

    public void setWocsinterface(List<Wocsinterface> wocsi) {
        this.wocsi = wocsi;
    }

    public List<Cbcs> getCbcs() {
        return cbcs;
    }

    public void setCbcs(List<Cbcs> cbcs) {
        this.cbcs = cbcs;
    }

    public List<Svc> getSvc() {
        return svc;
    }

    public void setSvc(List<Svc> svc) {
        this.svc = svc;
    }

    public List<Ssc> getSsc() {
        return ssc;
    }

    public void setSsc(List<Ssc> ssc) {
        this.ssc = ssc;
    }

    public List<Av> getAv() {
        return av;
    }

    public void setAv(List<Av> av) {
        this.av = av;
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