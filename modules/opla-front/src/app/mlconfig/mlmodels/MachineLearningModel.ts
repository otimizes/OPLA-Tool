import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class MachineLearningModel {
  [x: string]: any;
  algorithm: MachineLearningAlgorithm;
  type: string;

  constructor(algorithm: MachineLearningAlgorithm) {
    this.algorithm = algorithm;
    this.type = this.constructor.name;
  }
}
