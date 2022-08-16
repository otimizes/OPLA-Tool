import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class MachineLearningModel {
  algorithm: MachineLearningAlgorithm;

  //MLP
  mlp_decay: boolean;
  mlp_hiddenLayers: string;
  mlp_learningRate: string;
  mlp_momentum: string;
  mlp_trainingTime: string;

  //LMS
  lms_sampleSize: string;

  //SVM
  svm_SVMType: string;
  svm_coef0: string;
  svm_cost: string;
  svm_degree: string;
  svm_eps: string;
  svm_gamma: string;
  svm_kernelType: string;
  svm_loss: string;
  svm_normalize: boolean;
  svm_nu: string;
  svm_shrinking: boolean;

  //KSTAR
  kstar_entropicAutoBlend: boolean;
  kstar_globalBlend: string;
  kstar_missingMode: string;

  //RANDOM_FOREST
  randomforest_bagSizePercent: string;
  randomforest_breakTiesRandomly: boolean;
  randomforest_computeAttributeImportance: boolean;
  randomforest_maxDepth: string;
  randomforest_numFeatures: string;
  randomforest_numIterations: string;

  //RANDOM_TREE
  randomtree_kValue: string;
  randomtree_breakTiesRandomly: boolean;
  randomtree_maxDepth: string;
  randomtree_minNum: string;
  randomtree_minVariancePop: string;
  randomtree_numFolds: string;

  //ENSEMBLE

  //Vou refatorar - Fernando
  constructor(algorithm: MachineLearningAlgorithm, config: any[]) {

    this.algorithm = algorithm;
    switch (algorithm) {
      case MachineLearningAlgorithm.MLP:
        this.mlp_decay = config[0];
        this.mlp_hiddenLayers = config[1];
        this.mlp_learningRate = config[2];
        this.mlp_momentum = config[3];
        this.mlp_trainingTime = config[4];
        break;
      case MachineLearningAlgorithm.LMS:
        this.lms_sampleSize = config[0];
        break;
      case MachineLearningAlgorithm.SVM:
        this.svm_SVMType = config[0];
        this.svm_coef0 = config[1];
        this.svm_cost = config[2];
        this.svm_degree = config[3];
        this.svm_eps = config[4];
        this.svm_gamma = config[5];
        this.svm_kernelType = config[6];
        this.svm_loss = config[7];
        this.svm_normalize = config[8];
        this.svm_nu = config [9];
        this.svm_shrinking = config[10];
        break;
      case MachineLearningAlgorithm.KSTAR:
        this.kstar_entropicAutoBlend = config[0];
        this.kstar_globalBlend = config[1];
        this.kstar_missingMode = config[2];
        break;
      case MachineLearningAlgorithm.RANDOM_FOREST:
        this.randomforest_bagSizePercent = config [0];
        this.randomforest_breakTiesRandomly = config [1];
        this.randomforest_computeAttributeImportance = config [2];
        this.randomforest_maxDepth = config [3];
        this.randomforest_numFeatures = config [4];
        this.randomforest_numIterations = config [5];
        break;
      case MachineLearningAlgorithm.RANDOM_TREE:
        this.randomtree_kValue = config[0];
        this.randomtree_breakTiesRandomly = config[1];
        this.randomtree_maxDepth = config[2];
        this.randomtree_minNum = config[3];
        this.randomtree_minVariancePop = config[4];
        this.randomtree_numFolds = config[5];
    }
  }
}
