package br.otimizes.oplatool.core.learning.mlmodels;

public class LMSLearningModel extends MachineLearningModel {
    private int sampleSize;

    public LMSLearningModel() {
    }

    public LMSLearningModel(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }
}
