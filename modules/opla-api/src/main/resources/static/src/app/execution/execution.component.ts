import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../optimization-dto";

@Component({
  selector: 'app-execution',
  templateUrl: './execution.component.html',
  styleUrls: ['./execution.component.css']
})
export class ExecutionComponent implements OnInit {

  options: FormGroup;
  hideRequiredControl = new FormControl(false);
  floatLabelControl = new FormControl('auto');
  @Input() optimizationDto: OptimizationDto;

  constructor(fb: FormBuilder) {
    this.options = fb.group({
      hideRequired: this.hideRequiredControl,
      floatLabel: this.floatLabelControl,
      mutation: new FormControl(),
      mutationProbability: new FormControl(),
      crossover: new FormControl(),
      crossoverProbability: new FormControl(),
      numberRuns: new FormControl(),
      maxEvaluations: new FormControl(),
      populationSize: new FormControl(),
      archiveSize: new FormControl(),
      maxInteractions: new FormControl(),
      firstInteraction: new FormControl(),
      intervalInteraction: new FormControl(),
      inputArchitecture: new FormControl(),
      outputDirectory: new FormControl()
    });
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

  ngOnInit() {
  }
}
