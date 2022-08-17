import {Component, Inject, Input, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "../../../dto/optimization-dto";
import {MachineLearningAlgorithm} from "../../mlmodels/MachineLearningAlgorithm";

@Component({
  selector: 'app-lmsconfig',
  templateUrl: './lmsconfig.component.html',
  styleUrls: ['../mlconfig-dialogs.css']
})
export class LmsconfigComponent implements OnInit {

  algorithm: MachineLearningAlgorithm = MachineLearningAlgorithm.LMS;
  sampleSize: string = "4";
  formGroup: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      sampleSize: new FormControl('', [Validators.required, Validators.min(0)]),
    })
  }

}
