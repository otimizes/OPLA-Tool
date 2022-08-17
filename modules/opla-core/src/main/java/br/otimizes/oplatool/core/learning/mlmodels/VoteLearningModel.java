package br.otimizes.oplatool.core.learning.mlmodels;

import weka.classifiers.Classifier;
import weka.core.SelectedTag;

public class VoteLearningModel {
    private SelectedTag combinationRule;
    private Classifier[] classifers;

    public VoteLearningModel(SelectedTag combinationRule, Classifier[] classifers) {
        this.combinationRule = combinationRule;
        this.classifers = classifers;
    }

    public SelectedTag getCombinationRule() {
        return combinationRule;
    }

    public void setCombinationRule(SelectedTag combinationRule) {
        this.combinationRule = combinationRule;
    }

    public Classifier[] getClassifers() {
        return classifers;
    }

    public void setClassifers(Classifier[] classifers) {
        this.classifers = classifers;
    }
}
