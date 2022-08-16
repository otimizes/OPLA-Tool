import {Component, Input, OnInit} from '@angular/core';
import {OptimizationDto} from "../../dto/optimization-dto";
import {MachineLearningModel} from "../mlmodels/MachineLearningModel";
import {MachineLearningAlgorithm} from "../mlmodels/MachineLearningAlgorithm";

@Component({
  selector: 'app-modeloptions',
  templateUrl: './modeloptions.component.html',
  styleUrls: ['./modeloptions.component.css']
})
export class ModeloptionsComponent implements OnInit {

  @Input() algorithm: MachineLearningAlgorithm;
  @Input() optimizationDto: OptimizationDto;
  @Input() modelParameters: any[] = []

  constructor() { }

  ngOnInit() {
  }

  confirm() {
    // console.log(this.modelParameters)
    this.optimizationDto.machineLearningModels = [];
    this.optimizationDto.machineLearningModels[0] = new MachineLearningModel(this.algorithm, this.modelParameters)
    // console.log(this.optimizationDto.machineLearningModels)
  }

  reset() {

  }

  addToEnsemble() {
    this.optimizationDto.machineLearningModels.push(new MachineLearningModel(this.algorithm, this.modelParameters))
  }
}
