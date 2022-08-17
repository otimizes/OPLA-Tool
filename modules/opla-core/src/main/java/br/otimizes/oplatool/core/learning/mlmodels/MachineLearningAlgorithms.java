package br.otimizes.oplatool.core.learning.mlmodels;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.LeastMedSq;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.KStar;
import weka.classifiers.meta.Vote;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;

public enum MachineLearningAlgorithms implements MachineLearningAlgorithm {
    MLP {
        @Override
        public AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel) {
            MultilayerPerceptron mlp = new MultilayerPerceptron();
/*            mlp.setDecay();
            mlp.setHiddenLayers(machineLearningModel.mlp_hiddenLayers);
            mlp.setTrainingTime(machineLearningModel.mlp_trainingTime);
            mlp.setLearningRate(machineLearningModel.mlp_learningRate);
            mlp.setMomentum(machineLearningModel.mlp_momentum);*/
            return mlp;
        }
    }, LMS {
        @Override
        public AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel) {
            LeastMedSq leastMedSq = new LeastMedSq();
//            leastMedSq.setSampleSize();
            return leastMedSq;
        }
    }, SVM {
        @Override
        public AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel) {
            LibSVM libSVM = new LibSVM();
//            libSVM.setSVMType();
//            libSVM.setCoef0();
//            libSVM.setCost();
//            libSVM.setDegree();
//            libSVM.setEps();
//            libSVM.setGamma();
//            libSVM.setKernelType();
//            libSVM.setLoss();
//            libSVM.setNormalize();
//            libSVM.setNu();
//            libSVM.setShrinking();
            return libSVM;
        }
    }, KSTAR {
        @Override
        public AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel) {
            KStar kStar = new KStar();
//            kStar.setEntropicAutoBlend();
//            kStar.setGlobalBlend();
//            kStar.setMissingMode();
            return kStar;
        }
    }, RANDOM_FOREST {
        @Override
        public AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel) {
            RandomForest randomForest = new RandomForest();
//            randomForest.setBagSizePercent();
//            randomForest.setBreakTiesRandomly();
//            randomForest.setComputeAttributeImportance();
//            randomForest.setMaxDepth();
//            randomForest.setNumFeatures();
//            randomForest.setNumIterations();
            return randomForest;
        }
    }, RANDOM_TREE {
        @Override
        public AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel) {
            RandomTree randomTree = new RandomTree();
//            randomTree.setKValue();
//            randomTree.setBreakTiesRandomly();
//            randomTree.setMaxDepth();
//            randomTree.setMinNum();
//            randomTree.setMinVarianceProp();
//            randomTree.setNumFolds();
            return randomTree;
        }
    }, ENSEMBLE {
        @Override
        public AbstractClassifier getAlgorithm(MachineLearningModel machineLearningModel) {
            Vote vote = new Vote();
//            vote.setCombinationRule();
//            vote.setClassifiers();
            return vote;
        }
    }
}
