import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class MachineLearningModel {
  [x: string]: any;
  algorithm: MachineLearningAlgorithm;

  constructor(algorithm: MachineLearningAlgorithm) {
    this.algorithm = algorithm;
  }
}
