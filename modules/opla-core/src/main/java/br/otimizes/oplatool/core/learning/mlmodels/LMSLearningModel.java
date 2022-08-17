package br.otimizes.oplatool.core.learning.mlmodels;

public class LMSLearningModel {
    private int sampleSize;

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
