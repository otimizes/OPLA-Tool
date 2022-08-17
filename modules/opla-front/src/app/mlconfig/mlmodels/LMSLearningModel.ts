import {MachineLearningModel} from "./MachineLearningModel";
import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class LMSLearningModel extends MachineLearningModel{

  sampleSize: string;

  constructor(sampleSize: string) {
    super(MachineLearningAlgorithm.LMS);
    this.sampleSize = sampleSize;
  }
}
