import {Component, Inject, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {OptimizationDto} from "../../../dto/optimization-dto";
import {MachineLearningAlgorithm} from "../../mlmodels/MachineLearningAlgorithm";

@Component({
  selector: 'app-kstarconfig',
  templateUrl: './kstarconfig.component.html',
  styleUrls: ['../mlconfig-dialogs.css']
})
export class KstarconfigComponent implements OnInit {

  algorithm: MachineLearningAlgorithm = MachineLearningAlgorithm.KSTAR;
  entropicAutoBlend: string = "false";
  globalBlend: string = "20";
  missingMode: string = "average_column_entropy_curves";
  formGroup: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      entropicAutoBlend: new FormControl('', [Validators.required]),
      globalBlend: new FormControl('', [Validators.required, Validators.min(0)]),
      missingMode: new FormControl('', [Validators.required])
    })
  }

}
