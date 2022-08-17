package br.otimizes.oplatool.core.learning.mlmodels;

public class RandomForestLearningModel {
    private int bagSizePercent;
    private boolean breakTiesRandomly;
    private boolean computeAttributeImportance;
    private int maxDepth;
    private int numFeatures;
    private int numIterations;

    public RandomForestLearningModel(int bagSizePercent, boolean breakTiesRandomly, boolean computeAttributeImportance, int maxDepth, int numFeatures, int numIterations) {
        this.bagSizePercent = bagSizePercent;
        this.breakTiesRandomly = breakTiesRandomly;
        this.computeAttributeImportance = computeAttributeImportance;
        this.maxDepth = maxDepth;
        this.numFeatures = numFeatures;
        this.numIterations = numIterations;
    }

    public int getBagSizePercent() {
        return bagSizePercent;
    }

    public void setBagSizePercent(int bagSizePercent) {
        this.bagSizePercent = bagSizePercent;
    }

    public boolean isBreakTiesRandomly() {
        return breakTiesRandomly;
    }

    public void setBreakTiesRandomly(boolean breakTiesRandomly) {
        this.breakTiesRandomly = breakTiesRandomly;
    }

    public boolean isComputeAttributeImportance() {
        return computeAttributeImportance;
    }

    public void setComputeAttributeImportance(boolean computeAttributeImportance) {
        this.computeAttributeImportance = computeAttributeImportance;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getNumFeatures() {
        return numFeatures;
    }

    public void setNumFeatures(int numFeatures) {
        this.numFeatures = numFeatures;
    }

    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }
}
