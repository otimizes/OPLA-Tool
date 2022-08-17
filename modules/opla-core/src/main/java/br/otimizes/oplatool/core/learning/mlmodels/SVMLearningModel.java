package br.otimizes.oplatool.core.learning.mlmodels;

import weka.core.SelectedTag;

public class SVMLearningModel {
    private SelectedTag SVMType;
    private double coef0;
    private double cost;
    private int degree;
    private double eps;
    private double gamma;
    private SelectedTag kernelType;
    private double loss;
    private boolean normalize;
    private double nu;
    private boolean shrinking;

    public SVMLearningModel(SelectedTag SVMType, double coef0, double cost, int degree, double eps, double gamma, SelectedTag kernelType, double loss, boolean normalize, double nu, boolean shrinking) {
        this.SVMType = SVMType;
        this.coef0 = coef0;
        this.cost = cost;
        this.degree = degree;
        this.eps = eps;
        this.gamma = gamma;
        this.kernelType = kernelType;
        this.loss = loss;
        this.normalize = normalize;
        this.nu = nu;
        this.shrinking = shrinking;
    }

    public SelectedTag getSVMType() {
        return SVMType;
    }

    public void setSVMType(SelectedTag SVMType) {
        this.SVMType = SVMType;
    }

    public double getCoef0() {
        return coef0;
    }

    public void setCoef0(double coef0) {
        this.coef0 = coef0;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public double getEps() {
        return eps;
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public double getGamma() {
        return gamma;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public SelectedTag getKernelType() {
        return kernelType;
    }

    public void setKernelType(SelectedTag kernelType) {
        this.kernelType = kernelType;
    }

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
        this.loss = loss;
    }

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public double getNu() {
        return nu;
    }

    public void setNu(double nu) {
        this.nu = nu;
    }

    public boolean isShrinking() {
        return shrinking;
    }

    public void setShrinking(boolean shrinking) {
        this.shrinking = shrinking;
    }
}
