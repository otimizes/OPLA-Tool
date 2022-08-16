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
  kstar_entropicAutoBlend: boolean = false;
  kstar_globalBlend: string = "20";
  kstar_missingMode: string = "average_column_entropy_curves";
  formGroup: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.formGroup = new FormGroup({
      kstar_entropicAutoBlend: new FormControl('', [Validators.required]),
      kstar_globalBlend: new FormControl('', [Validators.required, Validators.min(0)]),
      kstar_missingMode: new FormControl('', [Validators.required])
    })
  }

}
