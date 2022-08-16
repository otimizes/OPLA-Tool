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
  svm_SVMType: string = "c_svc";
  svm_coef0: string = "40.0";
  svm_cost: string = "1.0";
  svm_degree: string = "3";
  svm_eps: string = "0.001";
  svm_gamma: string = "0.0";
  svm_kernelType: string = "radial_basis_function";
  svm_loss: string = "0.1";
  svm_normalize: boolean = false;
  svm_nu: string = "0.5";
  svm_shrinking: boolean = true;
  formGroup: FormGroup;


  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      svm_SVMType: new FormControl('', [Validators.required]),
      svm_coef0: new FormControl('', [Validators.required, Validators.min(0)]),
      svm_cost: new FormControl('', [Validators.required, Validators.min(0)]),
      svm_degree: new FormControl('', [Validators.required, Validators.min(0)]),
      svm_eps: new FormControl('', [Validators.required, Validators.min(0)]),
      svm_gamma: new FormControl('', [Validators.required, Validators.min(0)]),
      svm_kernelType: new FormControl('', [Validators.required]),
      svm_loss: new FormControl('', [Validators.required, Validators.min(0)]),
      svm_normalize: new FormControl('', [Validators.required]),
      svm_nu: new FormControl('', [Validators.required, Validators.min(0)]),
      svm_shrinking: new FormControl('', [Validators.required])
    })
  }

}
