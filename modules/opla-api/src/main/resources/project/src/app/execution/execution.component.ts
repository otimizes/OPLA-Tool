import {AfterContentChecked, AfterViewInit, Component, Input, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {OptimizationDto} from "../optimization-dto";
import {AppService} from "../app.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-execution',
  templateUrl: './execution.component.html',
  styleUrls: ['./execution.component.css']
})
export class ExecutionComponent implements OnInit, AfterContentChecked {

  @Input() formGroup: FormGroup;
  @Input() optimizationDto: OptimizationDto;
  @Input() optimizationOptions: any;
  @ViewChild('fileInput', {static: false}) fileInput;
  hideRequiredControl = new FormControl(false);
  floatLabelControl = new FormControl('auto');

  constructor(fb: FormBuilder, protected service: AppService, private snackBar: MatSnackBar) {
    AppService.onSelectPLA.asObservable().subscribe(list => {
      this.selectProfiles(list);
      // this.plaFiles = list;
    });
  }

  selectProfiles(list) {
    for (let string of list) {
      if (string.includes("smarty.profile")) {
        this.optimizationDto.config.pathToProfile = string;
      } else if (string.includes("concerns.profile")) {
        this.optimizationDto.config.pathToProfileConcern = string;
      } else if (string.includes("patterns.profile")) {
        this.optimizationDto.config.pathToProfilePatterns = string;
      } else if (string.includes("relationships.profile")) {
        this.optimizationDto.config.pathToProfileRelationships = string;
      }
    }
    this.optimizationDto.inputArchitecture = list.filter(t => !t.includes("simples") && !t.includes("profile") && t.endsWith(".uml"))[0];
  }

  changeObjFunction(obj, selected) {
    if (selected) {
      this.addObjFunction(obj);
    } else {
      this.removeObjFunction(obj);
    }
  }

  addObjFunction(obj) {
    if (!this.optimizationDto.objectiveFunctions.includes(obj)) {
      this.optimizationDto.objectiveFunctions.push(obj);
    }
  }

  removeObjFunction(obj) {
    this.optimizationDto.objectiveFunctions.splice(this.optimizationDto.objectiveFunctions.indexOf(obj), 1);
  }


  changeOperators(obj, selected) {
    if (selected) {
      this.addOOperator(obj);
    } else {
      this.removeOperator(obj);
    }
  }

  addOOperator(obj) {
    if (!this.optimizationDto.mutationOperators.includes(obj)) {
      this.optimizationDto.mutationOperators.push(obj);
    }
  }

  removeOperator(obj) {
    this.optimizationDto.mutationOperators.splice(this.optimizationDto.mutationOperators.indexOf(obj), 1);
  }

  selectPLA() {
    this.fileInput.nativeElement.click();
  }

  onFileSelected() {
    // this.service.optimize(new OptimizationDto())
    this.service.uploadPLA(this.fileInput.nativeElement.files)
      .subscribe(res => {
        AppService.setPLA(res);
        this.snackBar.open("The input PLA and the profiles were updated", null, {
          duration: 2000
        })
      })
  }

  ngOnInit() {

  }

  ngAfterContentChecked(): void {
    if (AppService.getPLA()) {
      this.selectProfiles(AppService.getPLA());
    }
  }
}
