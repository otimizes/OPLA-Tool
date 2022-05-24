import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {OptimizationDto} from "../dto/optimization-dto";
import {FormControl, FormGroup} from "@angular/forms";
import {OptimizationService} from "../services/optimization.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatListOption, MatSelectionList} from "@angular/material/list";
import {SelectionModel} from "@angular/cdk/collections";

@Component({
  selector: 'app-mlconfig',
  templateUrl: './mlconfig.component.html',
  styleUrls: ['./mlconfig.component.css']
})
export class MlconfigComponent implements OnInit {

  @Input() optimizationOptions: any;
  @Input() optimizationDto: OptimizationDto;
  @Input() formGroup: FormGroup;
  @ViewChild('fileInput', {static: false}) fileInput;
  models: string[] = ["mlp500.model", "lms.model", "svm.model", "kstar.model", "mlp2500.model", "myensemble.model", "reallybignameforafile_model.model"];

  floatLabelControl = new FormControl('auto');

  constructor(protected service: OptimizationService,private snackBar: MatSnackBar) { }

  ngOnInit() {

  }

  onFileSelected() {
    this.service.uploadPLA(this.fileInput.nativeElement.files)
      .subscribe(res => {
        OptimizationService.setPLA(res);
        this.snackBar.open("The input PLA and the profiles were updated", null, {
          duration: 2000
        })
      })
  }

  selectMlConfig() {
    this.fileInput.nativeElement.click();
  }

}
