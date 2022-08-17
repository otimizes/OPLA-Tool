import {MachineLearningModel} from "./MachineLearningModel";
import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class SVMLearningModel extends MachineLearningModel {

  SVMType: string;
  coef0: string;
  cost: string;
  degree: string;
  eps: string;
  gamma: string;
  kernelType: string;
  loss: string;
  normalize: boolean;
  nu: string;
  shrinking: boolean;

  constructor(SVMType: string, coef0: string, cost: string, degree: string, eps: string, gamma: string, kernelType: string, loss: string, normalize: string, nu: string, shrinking: string) {
    super(MachineLearningAlgorithm.SVM);
    this.SVMType = SVMType;
    this.coef0 = coef0;
    this.cost = cost;
    this.degree = degree;
    this.eps = eps;
    this.gamma = gamma;
    this.kernelType = kernelType;
    this.loss = loss;
    this.normalize = (normalize === "true");
    this.nu = nu;
    this.shrinking = (shrinking === "true");
  }
}


