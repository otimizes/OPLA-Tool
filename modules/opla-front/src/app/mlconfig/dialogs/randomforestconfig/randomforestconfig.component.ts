import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "../../../dto/optimization-dto";
import {MachineLearningAlgorithm} from "../../mlmodels/MachineLearningAlgorithm";

@Component({
  selector: 'app-randomforestconfig',
  templateUrl: './randomforestconfig.component.html',
  styleUrls: ['../mlconfig-dialogs.css']
})
export class RandomforestconfigComponent implements OnInit {

  algorithm: MachineLearningAlgorithm = MachineLearningAlgorithm.RANDOM_FOREST
  randomforest_bagSizePercent: string = "100";
  randomforest_breakTiesRandomly: boolean = false;
  randomforest_computeAttributeImportance: boolean = false;
  randomforest_maxDepth: string = "0";
  randomforest_numFeatures: string = "0";
  randomforest_numIterations: string = "100";
  formGroup: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      randomforest_bagSizePercent: new FormControl('', [Validators.required, Validators.min(0)]),
      randomforest_breakTiesRandomly: new FormControl('', [Validators.required]),
      randomforest_computeAttributeImportance: new FormControl('', [Validators.required]),
      randomforest_maxDepth: new FormControl('', [Validators.required, Validators.min(0)]),
      randomforest_numFeatures: new FormControl('', [Validators.required, Validators.min(0)]),
      randomforest_numIterations: new FormControl('', [Validators.required, Validators.min(0)])
    })
  }

}
