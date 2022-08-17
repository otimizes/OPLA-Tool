import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";
import {MachineLearningModel} from "./MachineLearningModel";

export class MLPLearningModel extends MachineLearningModel {

  decay: boolean;
  hiddenLayers: string;
  learningRate: string;
  momentum: string;
  trainingTime: string;

  constructor(decay: string, hiddenLayers: string, learningRate: string, momentum: string, trainingTime: string) {
    super(MachineLearningAlgorithm.MLP);
    this.decay = (decay === "true");
    this.hiddenLayers = hiddenLayers;
    this.learningRate = learningRate;
    this.momentum = momentum;
    this.trainingTime = trainingTime;
  }
}
