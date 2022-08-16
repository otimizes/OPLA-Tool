package br.otimizes.oplatool.core.learning.mlmodels;

public class MachineLearningModel {

    public MachineLearningAlgorithms algorithm;

    //MLP
    public boolean mlp_decay;
    public String mlp_hiddenLayers;
    public double mlp_learningRate;
    public double mlp_momentum;
    public double mlp_trainingTime;

    //LMS
    public double lms_sampleSize;

    //SVM
    public String svm_SVMType;
    public double svm_coef0;
    public double svm_cost;
    public double svm_degree;
    public double svm_eps;
    public double svm_gamma;
    public String svm_kernelType;
    public double svm_loss;
    public boolean svm_normalize;
    public double svm_nu;
    public boolean svm_shrinking;

    //KSTAR
    public boolean kstar_entropicAutoBlend;
    public double kstar_globalBlend;
    public String kstar_missingMode;

    //RANDOM_FOREST
    public double randomforest_bagSizePercent;
    public boolean randomforest_breakTiesRandomly;
    public boolean randomforest_computeAttributeImportance;
    public double randomforest_maxDepth;
    public double randomforest_numFeatures;
    public double randomforest_numIterations;

    //RANDOM_TREE
    public double randomtree_kValue;
    public boolean randomtree_breakTiesRandomly;
    public double randomtree_maxDepth;
    public double randomtree_minNum;
    public double randomtree_minVariancePop;
    public double randomtree_numFolds;

    @Override
    public String toString() {
        return "MachineLearningModel{" +
                "algorithm=" + algorithm +
                '}';
    }

    public MachineLearningModel() {

    }
}
