import {AfterContentChecked, Component, EventEmitter, Input, OnInit, Output, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../dto/optimization-dto";
import {OptimizationService} from "../services/optimization.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-execution',
  templateUrl: './execution.component.html',
  styleUrls: ['./execution.component.css']
})
export class ExecutionComponent implements OnInit, AfterContentChecked {

  @Input() formGroup: FormGroup;
  @Input() optimizationDto: OptimizationDto;
  @Output() optimizationDtoChange = new EventEmitter<OptimizationDto>();
  @Input() optimizationOptions: any;
  @ViewChild('fileInput', {static: false}) fileInput;
  floatLabelControl = new FormControl('auto');

  constructor(fb: FormBuilder, protected service: OptimizationService, private snackBar: MatSnackBar) {
    OptimizationService.onSelectPLA.asObservable().subscribe(list => {
      this.selectProfiles(list);
    });
  }

  selectProfiles(list) {
    if (list && list.length === 1) {
      this.optimizationDto.inputArchitecture = list[0];
    } else {
      for (let string of list) {
        if (string.includes("smarty.profile")) {
          this.optimizationDto.config.pathToProfile = string;
        } else if (string.includes("concerns.profile")) {
          this.optimizationDto.config.pathToProfileConcern = string;
        } else if (string.includes("patterns")) {
          this.optimizationDto.config.pathToProfilePatterns = string;
        } else if (string.includes("relationships.profile")) {
          this.optimizationDto.config.pathToProfileRelationships = string;
        }
      }
      this.optimizationDto.inputArchitecture = list.filter(t => !t.includes("simples") && !t.includes("profile") && !t.includes("model.uml") && t.endsWith(".uml"))[0];
    }
  }

  changeObjFunction(obj, selected) {
    if (selected) {
      this.addObjFunction(obj);
    } else {
      this.removeObjFunction(obj);
    }
    this.emit(this.optimizationDto)
  }

  addObjFunction(obj) {
    if (!this.optimizationDto.objectiveFunctions.includes(obj)) {
      this.optimizationDto.objectiveFunctions.push(obj);
    }
  }

  removeObjFunction(obj) {
    this.optimizationDto.objectiveFunctions.splice(this.optimizationDto.objectiveFunctions.indexOf(obj), 1);
  }


  changeOperators(element, obj, selected) {
    if (selected) {
      this.addOOperator(element, obj);
    } else {
      this.removeOperator(element, obj);
    }
    this.emit(this.optimizationDto)
  }

  addOOperator(element, obj) {
    if (!this.optimizationDto[element].includes(obj)) {
      this.optimizationDto[element].push(obj);
    }
  }

  removeOperator(element, obj) {
    this.optimizationDto[element].splice(this.optimizationDto[element].indexOf(obj), 1);
  }

  selectPLA() {
    this.fileInput.nativeElement.click();
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

  ngOnInit() {

  }

  ngAfterContentChecked(): void {
    if (OptimizationService.getPLA()) {
      this.selectProfiles(OptimizationService.getPLA());
    }
  }

  emit(optimizationDto: OptimizationDto) {
    this.optimizationDtoChange.emit(optimizationDto);
  }

  isObjFunctionChecked(obj: any) {
    return this.optimizationDto.objectiveFunctions.includes(obj);
  }

  setInteractionParams(checked: boolean, optimizationDto: OptimizationDto) {
    optimizationDto.maxInteractions = checked ? 3 : 0;
    optimizationDto.firstInteraction = checked ? 3 : 0;
    optimizationDto.intervalInteraction = checked ? 3 : 0;
  }
}
