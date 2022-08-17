package br.otimizes.oplatool.core.learning.mlmodels;

import weka.classifiers.AbstractClassifier;

public interface MachineLearningAlgorithm {
    AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel);
}
