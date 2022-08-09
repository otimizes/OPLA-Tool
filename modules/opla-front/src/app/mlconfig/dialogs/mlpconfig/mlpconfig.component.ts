import {Component, Inject, OnInit} from '@angular/core';
import {MachineLearningModelConfig} from "../../../dto/MachineLearningModelConfig";
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'app-mlpconfig',
  templateUrl: './mlpconfig.component.html',
  styleUrls: ['./mlpconfig.component.css']
})
export class MlpconfigComponent extends MachineLearningModelConfig implements OnInit {

  decay: boolean = true;
  hiddenLayers: string = "a";
  learningRate: string = "0.3";
  momentum: string = "0.2";
  trainingTime: string = "500";
  formGroup: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    super();
  }

  ngOnInit() {
    this.formGroup = new FormGroup({
      hiddenLayers: new FormControl('', [Validators.required]),
      learningRate: new FormControl('', [Validators.required, Validators.min(0)]),
      momentum: new FormControl('', [Validators.required, Validators.min(0)]),
      trainingTime: new FormControl('', [Validators.required, Validators.min(0)]),
    })
  }

  open() {
  }

  closeDialog() {
  }
}
