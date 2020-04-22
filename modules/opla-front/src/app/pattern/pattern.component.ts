import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../dto/optimization-dto";
import {MatRadioChange} from "@angular/material/radio";

@Component({
  selector: 'app-pattern',
  templateUrl: './pattern.component.html',
  styleUrls: ['./pattern.component.css']
})
export class PatternComponent implements OnInit {

  @Input() formGroup: FormGroup;
  hideRequiredControl = new FormControl(false);
  floatLabelControl = new FormControl('auto');
  @Input() optimizationDto: OptimizationDto;

  constructor(fb: FormBuilder) {

  }

  ngOnInit() {
  }

  changePatterns(obj: string, checked: boolean) {
    if (checked) {
      this.addPattern(obj);
    } else {
      this.removePattern(obj);
    }
  }

  addPattern(obj) {
    if (!this.optimizationDto.patterns.includes(obj)) {
      this.optimizationDto.patterns.push(obj);
    }
    this.cleanOperators()
  }

  cleanOperators() {
    if (this.optimizationDto.patterns.length > 0) {
      if (!this.optimizationDto.mutationOperators.includes('DesignPatterns'))
        this.optimizationDto.mutationOperators.push('DesignPatterns');
    } else {
      this.optimizationDto.mutationOperators.splice(this.optimizationDto.mutationOperators.indexOf('DesignPatterns', 1));
    }
  }

  removePattern(obj) {
    this.optimizationDto.patterns.splice(this.optimizationDto.patterns.indexOf(obj), 1);
    this.cleanOperators()
  }

  changeScopeSelection(event: MatRadioChange) {
    this.optimizationDto.scopeSelection = event.value
  }
}
