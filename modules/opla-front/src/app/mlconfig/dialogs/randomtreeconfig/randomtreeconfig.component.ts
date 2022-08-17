import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "../../../dto/optimization-dto";
import {MachineLearningAlgorithm} from "../../mlmodels/MachineLearningAlgorithm";

@Component({
  selector: 'app-randomtreeconfig',
  templateUrl: './randomtreeconfig.component.html',
  styleUrls: ['../mlconfig-dialogs.css']
})
export class RandomtreeconfigComponent implements OnInit {

  algorithm: MachineLearningAlgorithm = MachineLearningAlgorithm.RANDOM_TREE;
  kValue: string = "0";
  breakTiesRandomly: string = "false";
  maxDepth: string = "0";
  minNum: string = "1.0";
  minVariancePop: string = "0.001";
  numFolds: string = "0";
  formGroup: FormGroup

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      kValue: new FormControl('', [Validators.required, Validators.min(0)]),
      breakTiesRandomly: new FormControl('', [Validators.required]),
      maxDepth: new FormControl('', [Validators.required, Validators.min(0)]),
      minNum: new FormControl('', [Validators.required, Validators.min(0)]),
      minVariancePop: new FormControl('', [Validators.required, Validators.min(0)]),
      numFolds: new FormControl('', [Validators.required, Validators.min(0)])
    })
  }

}
