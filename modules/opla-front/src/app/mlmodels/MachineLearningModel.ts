import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class MachineLearningModel{
  algorithm: MachineLearningAlgorithm;

  //MLP
  decay: boolean;
  hiddenLayers: string;
  learningRate: string;
  momentum: string;
  trainingTime: string;

  //RANDOM_TREE
  kValue: string;
  breakTiesRandomly: boolean;
  maxDepth: string;
  minNum: string;
  minVariancePop: string;
  numFolds: string;


  constructor(algorithm: MachineLearningAlgorithm, config: any[]) {

    switch (algorithm){
      case MachineLearningAlgorithm.MLP:
          this.algorithm = algorithm;
          this.decay = config[0];
          this.hiddenLayers = config[1];
          this.learningRate = config[2];
          this.momentum = config[3];
          this.trainingTime = config[4];
          break;
      case MachineLearningAlgorithm.RANDOM_TREE:
          this.algorithm = algorithm;
          this.kValue = config[0];
          this.breakTiesRandomly = config[1];
          this.maxDepth = config[2];
          this.minNum = config[3];
          this.minVariancePop = config[4];
          this.numFolds = config[5];

    }
  }


}
