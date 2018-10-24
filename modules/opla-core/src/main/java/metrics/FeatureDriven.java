package metrics;

import results.Execution;
import results.Experiment;

/**
 * @author elf
 */
public class FeatureDriven extends Metrics {

    private double cdac;
    private double cdai;
    private double cdao;
    private double cibc;
    private double iibc;
    private double oobc;
    private double lcc;
    private double lccClass;
    private double cdaClass;
    private double cibClass;

    public FeatureDriven(String idSolution, Execution execution, Experiment experiement) {
        super.setExecution(execution);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
    }

    public double getMsiAggregation() {
        return this.lcc + this.cdac + this.cdai + this.cdao + this.cibc + this.iibc + this.oobc;
    }

    public double evaluateMSIFitness() {
        return this.lcc + this.lccClass + this.cdac + this.cdaClass + this.cdai + this.cdao + this.cibc + this.cibClass
                + this.iibc + this.oobc;
    }

    public double getCdac() {
        return cdac;
    }

    public void setCdac(double cdac) {
        this.cdac = cdac;
    }

    public double getCdai() {
        return cdai;
    }

    public void setCdai(double cdai) {
        this.cdai = cdai;
    }

    public double getCdao() {
        return cdao;
    }

    public void setCdao(double cdao) {
        this.cdao = cdao;
    }

    public double getCibc() {
        return cibc;
    }

    public void setCibc(double cibc) {
        this.cibc = cibc;
    }

    public double getIibc() {
        return iibc;
    }

    public void setIibc(double iibc) {
        this.iibc = iibc;
    }

    public double getOobc() {
        return oobc;
    }

    public void setOobc(double oobc) {
        this.oobc = oobc;
    }

    public double getLcc() {
        return lcc;
    }

    public void setLcc(double lcc) {
        this.lcc = lcc;
    }

    public double getLccClass() {
        return lccClass;
    }

    public void setLccClass(double lccClass) {
        this.lccClass = lccClass;
    }

    public double getCdaClass() {
        return cdaClass;
    }

    public void setCdaClass(double cdaClass) {
        this.cdaClass = cdaClass;
    }

    public double getCibClass() {
        return cibClass;
    }

    public void setCibClass(double cibClass) {
        this.cibClass = cibClass;
    }

}