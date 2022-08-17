import {Component, Input, OnInit} from '@angular/core';
import {OptimizationDto} from "../../dto/optimization-dto";
import {MachineLearningModel} from "../mlmodels/MachineLearningModel";
import {MachineLearningAlgorithm} from "../mlmodels/MachineLearningAlgorithm";
import {VoteLearningModel} from "../mlmodels/VoteLearningModel";
import {MLPLearningModel} from "../mlmodels/MLPLearningModel";
import {KStarLearningModel} from "../mlmodels/KStarLearningModel";
import {LMSLearningModel} from "../mlmodels/LMSLearningModel";
import {RandomForestLearningModel} from "../mlmodels/RandomForestLearningModel";
import {RandomTreeLearningModel} from "../mlmodels/RandomTreeLearningModel";
import {SVMLearningModel} from "../mlmodels/SVMLearningModel";

@Component({
  selector: 'app-modeloptions',
  templateUrl: './modeloptions.component.html',
  styleUrls: ['./modeloptions.component.css']
})
export class ModeloptionsComponent implements OnInit {

  @Input() algorithm: MachineLearningAlgorithm;
  @Input() optimizationDto: OptimizationDto;
  @Input() parameters: any = {};

  constructor() {
  }

  ngOnInit() {
  }

  confirm() {
    this.optimizationDto.machineLearningModel = this.switchModel();
    console.log(this.optimizationDto.machineLearningModel);
  }

  reset() {

  }

  switchModel(): MachineLearningModel{
    let alg;
    switch (this.algorithm){
      case MachineLearningAlgorithm.MLP:
        alg = new MLPLearningModel(this.parameters.decay, this.parameters.hiddenLayers,
          this.parameters.learningRate, this.parameters.momentum, this.parameters.trainingTime);
        break;

      case MachineLearningAlgorithm.RANDOM_FOREST:
        alg = new RandomForestLearningModel(this.parameters.bagSizePercent,
          this.parameters.breakTiesRandomly, this.parameters.computeAttributeImportance,
          this.parameters.maxDepth, this.parameters.numFeatures, this.parameters.numIterations)
        break;

      case MachineLearningAlgorithm.KSTAR:
        alg = new KStarLearningModel(this.parameters.entropicAutoBlend, this.parameters.globalBlend, this.parameters.missingMode)
        break;

      case MachineLearningAlgorithm.LMS:
        alg = new LMSLearningModel(this.parameters.sampleSize);
        break;

      case MachineLearningAlgorithm.RANDOM_TREE:
        alg = new RandomTreeLearningModel(this.parameters.kValue, this.parameters.breakTiesRandomly,
          this.parameters.maxDepth, this.parameters.minNum, this.parameters.minVariancePop, this.parameters.numFolds)
        break;

      case MachineLearningAlgorithm.SVM:
        alg = new SVMLearningModel(this.parameters.SVMtype, this.parameters.coef0, this.parameters.cost, this.parameters.degree,
          this.parameters.eps, this.parameters.gamma, this.parameters.kernelType, this.parameters.loss, this.parameters.normalize,
          this.parameters.nu, this.parameters.shrinking)
        break;
    }
    return alg;
  }

  addToEnsemble() {
    let vote : MachineLearningModel = new VoteLearningModel();
    if (!(this.optimizationDto.machineLearningModel instanceof VoteLearningModel)){
       this.optimizationDto.machineLearningModel = vote;
    } else {
      vote = this.optimizationDto.machineLearningModel;
    }
    vote.classifiers.push(this.switchModel());
    console.log(this.optimizationDto.machineLearningModel);
  }
}
