package br.otimizes.oplatool.core.learning.mlmodels;

public class MachineLearningModel {

    public MachineLearningAlgorithms algorithm;
    //MLP
    public boolean decay;
    public String hiddenLayers;
    public double learningRate;
    public double momentum;
    public double trainingTime;

    //RANDOM_TREE
    public double kValue;
    public boolean breakTiesRandomly;
    public double maxDepth;
    public double minNum;
    public double minVariancePop;
    public double numFolds;


    @Override
    public String toString() {
        return "MachineLearningModel{" +
                "algorithm=" + algorithm +
                '}';
    }

    public MachineLearningModel() {

    }
}
