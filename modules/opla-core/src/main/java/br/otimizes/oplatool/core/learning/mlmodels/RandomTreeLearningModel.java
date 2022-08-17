package br.otimizes.oplatool.core.learning.mlmodels;

public class RandomTreeLearningModel {
    private int kValue;
    private boolean breakTiesRandomly;
    private int maxDepth;
    private double minNum;
    private double minVariancePop;
    private int numFolds;

    public RandomTreeLearningModel(int kValue, boolean breakTiesRandomly, int maxDepth, double minNum, double minVariancePop, int numFolds) {
        this.kValue = kValue;
        this.breakTiesRandomly = breakTiesRandomly;
        this.maxDepth = maxDepth;
        this.minNum = minNum;
        this.minVariancePop = minVariancePop;
        this.numFolds = numFolds;
    }

    public int getkValue() {
        return kValue;
    }

    public void setkValue(int kValue) {
        this.kValue = kValue;
    }

    public boolean isBreakTiesRandomly() {
        return breakTiesRandomly;
    }

    public void setBreakTiesRandomly(boolean breakTiesRandomly) {
        this.breakTiesRandomly = breakTiesRandomly;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public double getMinNum() {
        return minNum;
    }

    public void setMinNum(double minNum) {
        this.minNum = minNum;
    }

    public double getMinVariancePop() {
        return minVariancePop;
    }

    public void setMinVariancePop(double minVariancePop) {
        this.minVariancePop = minVariancePop;
    }

    public int getNumFolds() {
        return numFolds;
    }

    public void setNumFolds(int numFolds) {
        this.numFolds = numFolds;
    }
}
