/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpr.dinf.gres.core.jmetal4.metrics;

import br.ufpr.dinf.gres.domain.entity.ExecutionResults;
import br.ufpr.dinf.gres.domain.entity.ExperimentResults;

/**
 * @author elf
 */
public class Dc extends Metrics {

    private double cdac;
    private double cdai;
    private double cdao;

    public Dc(String idSolution, ExecutionResults executionResults, ExperimentResults experiement) {
        super.setExecutionResults(executionResults);
        super.setExperiement(experiement);
        super.setIdSolution(idSolution);
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

    @Override
    public String toString() {
        return "DC [cdac=" + cdac + ", cdai=" + cdai + ", cdao=" + cdao + "]";
    }

    public Double evaluateDcFitness() {
        return this.cdac + this.cdai + this.cdao;
    }

}