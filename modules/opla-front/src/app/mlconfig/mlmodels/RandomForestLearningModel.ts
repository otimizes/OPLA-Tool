import {MachineLearningModel} from "./MachineLearningModel";
import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class RandomForestLearningModel extends MachineLearningModel{

  bagSizePercent: string;
  breakTiesRandomly: boolean;
  computeAttributeImportance: boolean;
  maxDepth: string;
  numFeatures: string;
  numIterations: string;

  constructor(bagSizePercent: string, breakTiesRandomly: string, computeAttributeImportance: string, maxDepth: string, numFeatures: string, numIterations: string) {
    super(MachineLearningAlgorithm.RANDOM_FOREST);
    this.bagSizePercent = bagSizePercent;
    this.breakTiesRandomly = (breakTiesRandomly === "true");
    this.computeAttributeImportance = (computeAttributeImportance === "true");
    this.maxDepth = maxDepth;
    this.numFeatures = numFeatures;
    this.numIterations = numIterations;
  }
}
