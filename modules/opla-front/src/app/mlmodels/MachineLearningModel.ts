import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class MachineLearningModel{
  algorithm: MachineLearningAlgorithm;
  decay: boolean;
  hiddenLayers: string;
  learningRate: string;
  momentum: string;
  trainingTime: string;


  constructor(algorithm: MachineLearningAlgorithm, config: any[]) {
    this.algorithm = algorithm;
    this.decay = config[0];
    this.hiddenLayers = config[1];
    this.learningRate = config[2];
    this.momentum = config[3];
    this.trainingTime = config[4];
  }


}
