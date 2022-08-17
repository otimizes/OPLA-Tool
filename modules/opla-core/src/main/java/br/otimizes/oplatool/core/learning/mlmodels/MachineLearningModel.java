package br.otimizes.oplatool.core.learning.mlmodels;

import weka.classifiers.AbstractClassifier;

import java.util.List;

public class MachineLearningModel{

    public MachineLearningAlgorithms algorithm;

    @Override
    public String toString() {
        return "MachineLearningModel{" +
                "algorithm=" + algorithm +
                '}';
    }

    public MachineLearningModel() {

    }
}
