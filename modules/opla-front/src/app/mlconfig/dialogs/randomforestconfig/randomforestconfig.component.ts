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
  bagSizePercent: string = "100";
  breakTiesRandomly: string = "false";
  computeAttributeImportance: string = "false";
  maxDepth: string = "0";
  numFeatures: string = "0";
  numIterations: string = "100";
  formGroup: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      bagSizePercent: new FormControl('', [Validators.required, Validators.min(0)]),
      breakTiesRandomly: new FormControl('', [Validators.required]),
      computeAttributeImportance: new FormControl('', [Validators.required]),
      maxDepth: new FormControl('', [Validators.required, Validators.min(0)]),
      numFeatures: new FormControl('', [Validators.required, Validators.min(0)]),
      numIterations: new FormControl('', [Validators.required, Validators.min(0)])
    })
  }

}
