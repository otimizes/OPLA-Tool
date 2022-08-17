import {MachineLearningModel} from "./MachineLearningModel";
import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class VoteLearningModel extends MachineLearningModel{
  combinationRule: string;
  classifiers: MachineLearningModel[] = [];

  constructor() {
    super(MachineLearningAlgorithm.VOTE);
  }
}
