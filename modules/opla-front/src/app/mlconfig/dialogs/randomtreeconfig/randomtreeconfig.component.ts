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
  randomtree_kValue: string = "0";
  randomtree_breakTiesRandomly: boolean = false;
  randomtree_maxDepth: string = "0";
  randomtree_minNum: string = "1.0";
  randomtree_minVariancePop: string = "0.001";
  randomtree_numFolds: string = "0";
  formGroup: FormGroup

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      randomtree_kValue: new FormControl('', [Validators.required, Validators.min(0)]),
      randomtree_breakTiesRandomly: new FormControl('', [Validators.required]),
      randomtree_maxDepth: new FormControl('', [Validators.required, Validators.min(0)]),
      randomtree_minNum: new FormControl('', [Validators.required, Validators.min(0)]),
      randomtree_minVariancePop: new FormControl('', [Validators.required, Validators.min(0)]),
      randomtree_numFolds: new FormControl('', [Validators.required, Validators.min(0)])
    })
  }

}
