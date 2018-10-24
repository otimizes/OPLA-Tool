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


}