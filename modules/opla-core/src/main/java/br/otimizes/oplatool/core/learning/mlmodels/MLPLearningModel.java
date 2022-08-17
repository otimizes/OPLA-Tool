package br.otimizes.oplatool.core.learning.mlmodels;

public class MLPLearningModel {
    private boolean decay;
    private String hiddenLayers;
    private double learningRate;
    private double momentum;
    private int trainingTime;

    public MLPLearningModel(boolean decay, String hiddenLayers, double learningRate, double momentum, int trainingTime) {
        this.decay = decay;
        this.hiddenLayers = hiddenLayers;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.trainingTime = trainingTime;
    }

    public boolean isDecay() {
        return decay;
    }

    public void setDecay(boolean decay) {
        this.decay = decay;
    }

    public String getHiddenLayers() {
        return hiddenLayers;
    }

    public void setHiddenLayers(String hiddenLayers) {
        this.hiddenLayers = hiddenLayers;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }
}
