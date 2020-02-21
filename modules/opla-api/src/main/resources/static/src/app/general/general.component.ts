import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {OptimizationDto} from "../optimization-dto";

@Component({
  selector: 'app-general',
  templateUrl: './general.component.html',
  styleUrls: ['./general.component.css']
})
export class GeneralComponent implements OnInit {

  options: FormGroup;
  hideRequiredControl = new FormControl(false);
  floatLabelControl = new FormControl('auto');
  @Input("optimizationDto") optimizationDto: OptimizationDto;

  constructor(fb: FormBuilder) {
    this.options = fb.group({
      hideRequired: this.hideRequiredControl,
      floatLabel: this.floatLabelControl,
      pathToTemplateModelsDirectory: new FormControl(),
      directoryToSaveModels: new FormControl(),
      pathToProfileConcern: new FormControl(),
      pathToProfile: new FormControl(),
      pathToProfileRelationships: new FormControl(),
      pathToProfilePatterns: new FormControl(),
      smarty: new FormControl(),
      feature: new FormControl(),
      patterns: new FormControl(),
      relationships: new FormControl()
    });
  }

  ngOnInit() {
  }

}
