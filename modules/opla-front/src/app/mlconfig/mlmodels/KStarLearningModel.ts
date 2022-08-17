import {MachineLearningModel} from "./MachineLearningModel";
import {MachineLearningAlgorithm} from "./MachineLearningAlgorithm";

export class KStarLearningModel extends MachineLearningModel{

  entropicAutoBlend: boolean;
  globalBlend: string;
  missingMode: string;

  constructor(entropicAutoBlend: string, globalBlend: string, missingMode: string) {
    super(MachineLearningAlgorithm.KSTAR);
    this.entropicAutoBlend = (entropicAutoBlend === "true");
    this.globalBlend = globalBlend;
    this.missingMode = missingMode;
  }
}
