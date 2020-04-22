import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../dto/optimization-dto";

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
  }

  removePattern(obj) {
    this.optimizationDto.patterns.splice(this.optimizationDto.patterns.indexOf(obj), 1);
  }

}
