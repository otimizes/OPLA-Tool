import {MachineLearningModel} from "./MachineLearningModel";
import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class RandomTreeLearningModel extends MachineLearningModel{

  kValue: string;
  breakTiesRandomly: boolean;
  maxDepth: string;
  minNum: string;
  minVariancePop: string;
  numFolds: string;


  constructor(kValue: string, breakTiesRandomly: string, maxDepth: string, minNum: string, minVariancePop: string, numFolds: string) {
    super(MachineLearningAlgorithm.RANDOM_TREE);
    this.kValue = kValue;
    this.breakTiesRandomly = (breakTiesRandomly === "true");
    this.maxDepth = maxDepth;
    this.minNum = minNum;
    this.minVariancePop = minVariancePop;
    this.numFolds = numFolds;
  }
}


