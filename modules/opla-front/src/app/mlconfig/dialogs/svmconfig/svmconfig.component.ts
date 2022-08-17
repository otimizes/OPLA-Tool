import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "../../../dto/optimization-dto";
import {MachineLearningAlgorithm} from "../../mlmodels/MachineLearningAlgorithm";

@Component({
  selector: 'app-svmconfig',
  templateUrl: './svmconfig.component.html',
  styleUrls: ['../mlconfig-dialogs.css']
})
export class SvmconfigComponent implements OnInit {

  algorithm: MachineLearningAlgorithm = MachineLearningAlgorithm.SVM;
  SVMtype: string = "C_SVC";
  coef0: string = "40.0";
  cost: string = "1.0";
  degree: string = "3";
  eps: string = "0.001";
  gamma: string = "0.0";
  kernelType: string = "RADIAL_BASIS_FUNCTION";
  loss: string = "0.1";
  normalize: string = "false";
  nu: string = "0.5";
  shrinking: string = "true";
  formGroup: FormGroup;


  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      SVMType: new FormControl('', [Validators.required]),
      coef0: new FormControl('', [Validators.required, Validators.min(0)]),
      cost: new FormControl('', [Validators.required, Validators.min(0)]),
      degree: new FormControl('', [Validators.required, Validators.min(0)]),
      eps: new FormControl('', [Validators.required, Validators.min(0)]),
      gamma: new FormControl('', [Validators.required, Validators.min(0)]),
      kernelType: new FormControl('', [Validators.required]),
      loss: new FormControl('', [Validators.required, Validators.min(0)]),
      normalize: new FormControl('', [Validators.required]),
      nu: new FormControl('', [Validators.required, Validators.min(0)]),
      shrinking: new FormControl('', [Validators.required])
    })
  }

}
